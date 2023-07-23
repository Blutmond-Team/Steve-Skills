package de.blutmondgilde.stevesskills.client.lang;

import net.minecraft.network.chat.MutableComponent;

import static de.blutmondgilde.stevesskills.StevesSkills.REGISTRATE;

public class StevesSkillsMessages {
    public static final MutableComponent SKILL_UNLOCKED = REGISTRATE.addMessageLang("skill.unlocked", "You unlocked the skill %s");
    public static final MutableComponent SKILL_EXP_GAINED = REGISTRATE.addMessageLang("skill.exp.gained", "You gained %s exp for the skill %s");

    public static void init() {

    }
}
