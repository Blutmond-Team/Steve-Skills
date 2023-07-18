package de.blutmondgilde.stevesskills.skill;

import net.minecraft.nbt.CompoundTag;

public abstract class Skill {
    //public static final Codec<Skill> CODEC = ExtraCodecs.lazyInitializedCodec(() -> )

    public abstract CompoundTag serialize();

    public abstract void deserialize(CompoundTag tag);
}
