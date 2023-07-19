package de.blutmondgilde.stevesskills.network.packet.clientbound;

import de.blutmondgilde.stevesskills.StevesSkills;
import de.blutmondgilde.stevesskills.capability.skill.EntitySkillsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

class ClientAccess {
    static void syncEntityCapability(int entityId, CompoundTag serializedCapability) {
        StevesSkills.getLogger().debug("Syncing Entity Capabilities of Entity with id {}", entityId);
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        EntitySkillsCapability.from(entity).deserializeNBT(serializedCapability);
        StevesSkills.getLogger().debug("Synced Entity Capabilities of Entity with id {}", entityId);
    }
}
