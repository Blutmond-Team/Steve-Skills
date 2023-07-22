package de.blutmondgilde.stevesskills.capability.skill;

import com.mojang.datafixers.util.Pair;
import de.blutmondgilde.stevesskills.skill.Skill;
import de.blutmondgilde.stevesskills.skill.SkillInstance;
import de.blutmondgilde.stevesskills.skill.wrapper.SkillWrapper;
import de.blutmondgilde.stevesskills.util.map.DoubleValuedMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@AutoRegisterCapability
public interface EntitySkills extends INBTSerializable<CompoundTag> {

    DoubleValuedMap<Skill, SkillInstance, Boolean> getSkillsStatusMap();

    default Set<Skill> getSkills() {
        return getSkillsStatusMap().keySet();
    }

    default List<Skill> getActiveSkills() {
        return getSkillsStatusMap().keySet()
                .stream()
                .filter(skillInstance -> getSkillsStatusMap().get(skillInstance).getSecond())
                .toList();
    }

    void addSkill(SkillInstance instance, boolean status);

    default void addSkill(SkillInstance instance) {
        addSkill(instance, true);
    }

    Pair<SkillInstance, Boolean> removeSkill(Skill instance);

    default Pair<SkillInstance, Boolean> removeSkill(SkillInstance instance) {
        return removeSkill(instance.getSkill());
    }

    default void setSkill(SkillInstance instance, boolean status) {
        addSkill(instance, status);
    }

    default boolean hasSkill(Skill skill) {
        return getSkills().contains(skill);
    }

    default boolean isActive(Skill skill) {
        return getActiveSkills().contains(skill);
    }

    default boolean hasSkill(Supplier<Skill> skillActionSupplier) {
        return hasSkill(skillActionSupplier.get());
    }

    void unlock(SkillInstance skillInstance);

    default SkillInstance getSkill(Skill skill) {
        return getSkillsStatusMap().get(skill).getFirst();
    }

    default <T extends SkillWrapper> T getSkillWrapper(Skill skill, Function<SkillInstance, T> factory) {
        return factory.apply(getSkill(skill));
    }

    void sync();
}
