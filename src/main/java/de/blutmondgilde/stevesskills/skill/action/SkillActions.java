package de.blutmondgilde.stevesskills.skill.action;

import com.tterrag.registrate.util.entry.RegistryEntry;
import de.blutmondgilde.stevesskills.StevesSkills;
import de.blutmondgilde.stevesskills.capability.skill.EntitySkillsCapability;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.UUID;
import java.util.function.Supplier;

import static de.blutmondgilde.stevesskills.StevesSkills.REGISTRATE;

public class SkillActions {
    // TODO remove me
    private static final AttributeModifier temp = new AttributeModifier(UUID.fromString("8487c608-84b6-4413-a1ac-70772a47e40e"), "test_attribute", 10, Operation.ADDITION);
    public static final ResourceKey<Registry<SkillAction>> REGISTRY_KEY = REGISTRATE.makeRegistry("skill_actions", RegistryBuilder::new);
    public static final Supplier<IForgeRegistry<SkillAction>> REGISTRY_SUPPLIER = REGISTRATE.getForgeRegistrySupplier(REGISTRY_KEY);
    public static RegistryEntry<SkillAction> INCREASE_HEALTH = REGISTRATE.skillAction("test")
            .<PlayerLoggedInEvent>addEventListener(event -> {
                // TODO load attribute type from ConfiguredSkillAction
                // TODO load UUID from ConfiguredSkillAction
                // TODO load amount from ConfiguredSkillAction
                AttributeInstance instance = event.getEntity().getAttribute(Attributes.MAX_HEALTH);
                if (!instance.hasModifier(temp)) {
                    instance.addPermanentModifier(temp);
                    event.getEntity().hurtMarked = true;
                }

                if (EntitySkillsCapability.from(event.getEntity()) != null) {
                    event.getEntity().sendSystemMessage(Component.literal("You got the Cap!"));
                }
            })
            .onRegister(SkillAction::registerEventListener)
            .register();

    public static void init() {
        StevesSkills.getLogger().info("Registered default skill actions.");
    }
}
