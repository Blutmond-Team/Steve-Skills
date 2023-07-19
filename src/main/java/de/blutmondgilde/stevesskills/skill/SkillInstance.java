package de.blutmondgilde.stevesskills.skill;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

public class SkillInstance {
    public static final Codec<SkillInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Skill.CODEC.fieldOf("type").forGetter(SkillInstance::getSkillAction),
            CompoundTag.CODEC.optionalFieldOf("config", new CompoundTag()).forGetter(SkillInstance::getAdditionalData)
    ).apply(instance, SkillInstance::new));
    private final Holder<Skill> holder;
    private CompoundTag additionalData;

    public SkillInstance(Skill skill) {
        this(skill, new CompoundTag());
    }

    public SkillInstance(Holder<Skill> skillActionHolder) {
        this(skillActionHolder, new CompoundTag());
    }

    public SkillInstance(Skill skill, CompoundTag tag) {
        this(Skills.REGISTRY_SUPPLIER.get().getHolder(skill).get(), tag);
    }

    public SkillInstance(Holder<Skill> skillActionHolder, CompoundTag tag) {
        this.holder = skillActionHolder;
        this.additionalData = tag;
    }

    public void setAdditionalData(CompoundTag tag) {
        this.additionalData = tag;
    }

    public CompoundTag getAdditionalData() {
        return this.additionalData;
    }

    public Skill getSkillAction() {
        return this.holder.get();
    }

    public Holder<Skill> getSkillActionHolder() {
        return holder;
    }

    public CompoundTag serialize() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).get().left().get();
    }

    public static SkillInstance of(CompoundTag serializedSkillActionInstance) {
        return CODEC.parse(NbtOps.INSTANCE, serializedSkillActionInstance).get().left().get();
    }

    public boolean is(Skill action) {
        return this.holder.get().equals(action);
    }

    public void setAdditionalInt(String key, int value) {
        this.additionalData.putInt(key, value);
    }

    public int getAdditionalInt(String key) {
        return getAdditionalInt(key, 0);
    }

    public int getAdditionalInt(String key, int defaultValue) {
        if (this.additionalData.contains(key)) {
            return this.additionalData.getInt(key);
        }

        setAdditionalInt(key, defaultValue);
        return defaultValue;
    }

    public void setAdditionalDouble(String key, double value) {
        this.additionalData.putDouble(key, value);
    }

    public double getAdditionalDouble(String key) {
        return getAdditionalDouble(key, 0);
    }

    public double getAdditionalDouble(String key, double defaultValue) {
        if (this.additionalData.contains(key)) {
            return this.additionalData.getInt(key);
        }

        setAdditionalDouble(key, defaultValue);
        return defaultValue;
    }
}
