package de.blutmondgilde.stevesskills.capability.skill;

import com.mojang.datafixers.util.Pair;
import de.blutmondgilde.stevesskills.event.SkillEvent.UnlockedEvent;
import de.blutmondgilde.stevesskills.network.StevesSkillsNetwork;
import de.blutmondgilde.stevesskills.network.packet.clientbound.SyncEntitySkillsPacket;
import de.blutmondgilde.stevesskills.skill.Skill;
import de.blutmondgilde.stevesskills.skill.SkillInstance;
import de.blutmondgilde.stevesskills.util.map.DoubeValuedHashMap;
import de.blutmondgilde.stevesskills.util.map.DoubleValuedMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

public class EntitySkillImpl implements EntitySkills {
    private static final String SKILL_LIST_TAG_ID = "skills";
    private static final String SKILL_INSTANCE_ID = "skillInstance";
    private static final String SKILL_INSTANCE_STATUS_ID = "enabled";

    /**
     * Map containing the skill action instances and their status (enabled / disabled)
     */
    private final DoubleValuedMap<Skill, SkillInstance, Boolean> skills = new DoubeValuedHashMap<>();
    @Getter
    @Setter
    private Entity owner = null;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag skillTag = new ListTag();
        skills.forEach((key, instance, status) -> {
            CompoundTag skillData = new CompoundTag();
            skillData.put(SKILL_INSTANCE_ID, instance.serialize());
            skillData.putBoolean(SKILL_INSTANCE_STATUS_ID, status);

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
            SkillInstance instance = SkillInstance.of(tag.getCompound(SKILL_INSTANCE_ID));
            skills.put(instance.getSkillAction(), instance, status);
        });
    }

    @Override
    public DoubleValuedMap<Skill, SkillInstance, Boolean> getSkillsStatusMap() {
        return skills;
    }

    @Override
    public void addSkill(SkillInstance instance, boolean status) {
        skills.put(instance.getSkillAction(), instance, status);
    }

    @Override
    public Pair<SkillInstance, Boolean> removeSkill(Skill skill) {
        return skills.remove(skill);
    }

    @Override
    public void unlock(SkillInstance skillInstance) {
        if (MinecraftForge.EVENT_BUS.post(new UnlockedEvent(skillInstance, getOwner()))) {
            addSkill(skillInstance);
            getOwner().sendSystemMessage(Component.literal(String.format("You unlocked the skill %s!", "tbh")));
        }
    }

    @Override
    public void sync() {
        if (!getOwner().level().isClientSide()) {
            StevesSkillsNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getOwner), new SyncEntitySkillsPacket(getOwner().getId(), this));
        }
    }
}
