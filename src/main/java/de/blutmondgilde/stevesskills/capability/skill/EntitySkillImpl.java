package de.blutmondgilde.stevesskills.capability.skill;

import de.blutmondgilde.stevesskills.skill.action.SkillActionInstance;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class EntitySkillImpl implements EntitySkills {
    private static final String SKILL_LIST_TAG_ID = "skills";
    private static final String SKILL_INSTANCE_ID = "skillInstance";
    private static final String SKILL_INSTANCE_STATUS_ID = "enabled";

    /**
     * Map containing the skill action instances and their status (enabled / disabled)
     */
    private final Map<SkillActionInstance, Boolean> skills = new HashMap<>();
    @Getter
    @Setter
    private Entity owner = null;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag skillTag = new ListTag();
        skills.forEach((instance, aBoolean) -> {
            CompoundTag skillData = new CompoundTag();
            skillData.put(SKILL_INSTANCE_ID, instance.serialize());
            skillData.putBoolean(SKILL_INSTANCE_STATUS_ID, aBoolean);

            skillTag.add(skillData);
        });

        tag.put(SKILL_LIST_TAG_ID, skillTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        skills.clear();
        ListTag skillTags = nbt.getList(SKILL_LIST_TAG_ID, Tag.TAG_COMPOUND);
        skillTags.forEach(data -> {
            CompoundTag tag = (CompoundTag) data;
            boolean status = tag.getBoolean(SKILL_INSTANCE_STATUS_ID);
            SkillActionInstance instance = SkillActionInstance.of(tag.getCompound(SKILL_INSTANCE_ID));
            skills.put(instance, status);
        });
    }

    public Map<SkillActionInstance, Boolean> getSkillsStatusMap() {
        return skills;
    }

    public void addSkillInstance(SkillActionInstance instance, boolean status) {
        skills.put(instance, status);
    }

    public boolean removeSkillInstance(SkillActionInstance instance) {
        return skills.remove(instance);
    }
}
