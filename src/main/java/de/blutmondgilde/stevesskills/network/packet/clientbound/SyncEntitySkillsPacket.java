package de.blutmondgilde.stevesskills.network.packet.clientbound;

import de.blutmondgilde.stevesskills.capability.skill.EntitySkills;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncEntitySkillsPacket {
    private final int entityId;
    private final CompoundTag serializedCapability;

    public SyncEntitySkillsPacket(int entityId, EntitySkills skills) {
        this.entityId = entityId;
        this.serializedCapability = skills.serializeNBT();
    }

    public SyncEntitySkillsPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.serializedCapability = buf.readAnySizeNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeNbt(this.serializedCapability);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientAccess.syncEntityCapability(this.entityId, this.serializedCapability)));
        ctx.get().setPacketHandled(true);
    }
}
