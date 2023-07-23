package de.blutmondgilde.stevesskills.skill;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static de.blutmondgilde.stevesskills.StevesSkills.REGISTRATE;

public class SkillNames {
    private static final String type = "skill";
    public static final MutableComponent ENDURANCE = REGISTRATE.addLang(type, Skills.ENDURANCE.getId(), "Endurance");

    public static void init() {
    }

    public static MutableComponent of(Skill skill) {
        return Component.translatable(Util.makeDescriptionId(type, skill.getId()));
    }
}
