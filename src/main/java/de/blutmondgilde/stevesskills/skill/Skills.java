package de.blutmondgilde.stevesskills.skill;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import com.tterrag.registrate.util.entry.RegistryEntry;
import de.blutmondgilde.stevesskills.Config;
import de.blutmondgilde.stevesskills.StevesSkills;
import de.blutmondgilde.stevesskills.capability.skill.EntitySkills;
import de.blutmondgilde.stevesskills.capability.skill.EntitySkillsCapability;
import de.blutmondgilde.stevesskills.event.SkillEvent.UnlockedEvent;
import de.blutmondgilde.stevesskills.skill.wrapper.DefaultSkillWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.UUID;
import java.util.function.Supplier;

import static de.blutmondgilde.stevesskills.StevesSkills.REGISTRATE;

public class Skills {
    private static final UUID ENDURANCE_SKILL_MODIFIER_ID = UUID.fromString("8487c608-84b6-4413-a1ac-70772a47e40e");

    public static final ResourceKey<Registry<Skill>> REGISTRY_KEY = REGISTRATE.makeRegistry("skill", RegistryBuilder::new);
    public static final Supplier<IForgeRegistry<Skill>> REGISTRY_SUPPLIER = REGISTRATE.getForgeRegistrySupplier(REGISTRY_KEY);
    public static RegistryEntry<Skill> ENDURANCE = REGISTRATE.skill("endurance")
            .<LivingHurtEvent>addEventListener(self -> event -> {
                if (!event.getEntity().level().isClientSide()) {
                    EntitySkills skills = EntitySkillsCapability.from(event.getEntity());
                    DefaultSkillWrapper wrapper;
                    if (!skills.hasSkill(self)) {
                        SkillInstance instance = new SkillInstance(self);
                        wrapper = new DefaultSkillWrapper(instance);
                        skills.unlock(instance);
                    } else {
                        wrapper = skills.getSkillWrapper(self, DefaultSkillWrapper::new);
                    }
                    wrapper.addSkillExp(0.1);
                }
            })
            .<UnlockedEvent>addEventListener(self -> event -> {
                if (event.getSkillInstance().is(self) && event.getTarget() instanceof LivingEntity entity) {
                    AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
                    AttributeModifier modifier = instance.getModifier(ENDURANCE_SKILL_MODIFIER_ID);
                    double prevAmount = 0;
                    if (modifier != null) {
                        prevAmount = modifier.getAmount();
                        instance.removePermanentModifier(ENDURANCE_SKILL_MODIFIER_ID);
                    }

                    double newAmount = prevAmount;
                    try {
                        newAmount = Config.ENDURANCE_MEALTH_EXPRESSION.with("lvl", new DefaultSkillWrapper(event.getSkillInstance()).getSkillLevel())
                                .evaluate()
                                .getNumberValue()
                                .doubleValue();
                    } catch (EvaluationException | ParseException e) {
                        e.printStackTrace();
                    }

                    instance.addPermanentModifier(new AttributeModifier(ENDURANCE_SKILL_MODIFIER_ID, "endurance_modifier", newAmount, Operation.ADDITION));
                    double healAmount = newAmount - prevAmount;
                    entity.heal((float) healAmount);
                    entity.hurtMarked = true;
                }
            })
            .onRegister(Skill::registerEventListener)
            .register();

    public static void init() {
        StevesSkills.getLogger().info("Registered default skills.");
    }
}
