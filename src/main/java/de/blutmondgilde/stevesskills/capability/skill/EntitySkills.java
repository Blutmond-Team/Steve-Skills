package de.blutmondgilde.stevesskills.capability.skill;

import de.blutmondgilde.stevesskills.skill.action.SkillActionInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@AutoRegisterCapability
public interface EntitySkills extends INBTSerializable<CompoundTag> {

    Map<SkillActionInstance, Boolean> getSkillsStatusMap();

    default Set<SkillActionInstance> getSkills() {
        return getSkillsStatusMap().keySet();
    }

    default List<SkillActionInstance> getActiveSkills() {
        return getSkillsStatusMap().keySet()
                .stream()
                .filter(skillActionInstance -> getSkillsStatusMap().get(skillActionInstance))
                .toList();
    }

    void addSkillInstance(SkillActionInstance instance, boolean status);

    default void addSkillInstance(SkillActionInstance instance) {
        addSkillInstance(instance, true);
    }

    boolean removeSkillInstance(SkillActionInstance instance);

    default void setSkillInstance(SkillActionInstance instance, boolean status) {
        addSkillInstance(instance, status);
    }
}
