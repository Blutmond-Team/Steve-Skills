package de.blutmondgilde.stevesskills.skill.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

public class SkillActionInstance {
    public static final Codec<SkillActionInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SkillAction.CODEC.fieldOf("type").forGetter(SkillActionInstance::getSkillAction),
            CompoundTag.CODEC.optionalFieldOf("config", new CompoundTag()).forGetter(SkillActionInstance::getAdditionalData)
    ).apply(instance, SkillActionInstance::new));
    private final Holder<SkillAction> holder;
    private CompoundTag additionalData;

    public SkillActionInstance(SkillAction skillAction) {
        this(skillAction, new CompoundTag());
    }

    public SkillActionInstance(Holder<SkillAction> skillActionHolder) {
        this(skillActionHolder, new CompoundTag());
    }

    public SkillActionInstance(SkillAction skillAction, CompoundTag tag) {
        this(SkillActions.REGISTRY_SUPPLIER.get().getHolder(skillAction).get(), tag);
    }

    public SkillActionInstance(Holder<SkillAction> skillActionHolder, CompoundTag tag) {
        this.holder = skillActionHolder;
        this.additionalData = tag;
    }

    public void setAdditionalData(CompoundTag tag) {
        this.additionalData = tag;
    }

    public CompoundTag getAdditionalData() {
        return this.additionalData;
    }

    public SkillAction getSkillAction() {
        return this.holder.get();
    }

    public Holder<SkillAction> getSkillActionHolder() {
        return holder;
    }

    public CompoundTag serialize() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).get().left().get();
    }

    public static SkillActionInstance of(CompoundTag serializedSkillActionInstance) {
        return CODEC.parse(NbtOps.INSTANCE, serializedSkillActionInstance).get().left().get();
    }
}
