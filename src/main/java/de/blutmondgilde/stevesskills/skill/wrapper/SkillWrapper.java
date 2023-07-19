package de.blutmondgilde.stevesskills.skill.wrapper;

import de.blutmondgilde.stevesskills.skill.SkillInstance;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SkillWrapper {
    protected final SkillInstance instance;

    public SkillInstance getSkillInstance() {
        return instance;
    }
}
