package de.blutmondgilde.stevesskills.skill.action;

import com.tterrag.registrate.util.entry.RegistryEntry;
import de.blutmondgilde.stevesskills.StevesSkills;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.RegistryBuilder;

import static de.blutmondgilde.stevesskills.StevesSkills.REGISTRATE;

public class SkillActions {
    public static final ResourceKey<Registry<SkillAction>> REGISTRY_KEY = REGISTRATE.makeRegistry("skill_actions", RegistryBuilder::new);
    public static RegistryEntry<SkillAction> TEST_ACTION = REGISTRATE.skillAction("test")
            .<LivingHurtEvent>addEventListener(event -> {
                if (event.getEntity() instanceof Player player) {
                    player.sendSystemMessage(Component.literal("You got damage... NOOB!"));
                }
            })
            .onRegister(skillAction -> {
                skillAction.registerEventListener();
                StevesSkills.getLogger().info("Registered TEST_ACTION!");
            })
            .register();

    public static void init() {
        StevesSkills.getLogger().info("Registered default Skill Actions.");
    }
}
