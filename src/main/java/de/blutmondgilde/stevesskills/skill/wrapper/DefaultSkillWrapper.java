package de.blutmondgilde.stevesskills.skill.wrapper;

import de.blutmondgilde.stevesskills.skill.SkillInstance;

public class DefaultSkillWrapper extends SkillWrapper implements LevelableSkillWrapper {
    private static final String SKILL_LEVEL_KEY = "skill_level";
    private static final String SKILL_LEVEL_EXP_KEY = "skill_level_exp";

    public DefaultSkillWrapper(SkillInstance instance) {
        super(instance);
    }

    public int getSkillLevel() {
        return this.instance.getAdditionalInt(SKILL_LEVEL_KEY);
    }

    public void setSkillLevel(int level) {
        this.instance.setAdditionalInt(SKILL_LEVEL_KEY, level);
    }

    public double getSkillExp() {
        return this.instance.getAdditionalDouble(SKILL_LEVEL_EXP_KEY);
    }

    public void setSkillExp(double exp) {
        this.instance.setAdditionalDouble(SKILL_LEVEL_EXP_KEY, exp);
    }
}
