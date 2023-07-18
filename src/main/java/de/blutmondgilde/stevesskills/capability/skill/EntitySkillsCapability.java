package de.blutmondgilde.stevesskills.capability.skill;

import de.blutmondgilde.stevesskills.StevesSkills;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntitySkillsCapability {
    public static final Capability<EntitySkills> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation IDENTIFIER = new ResourceLocation(StevesSkills.MODID, "skills");

        private final EntitySkills backend = new EntitySkillImpl();
        private final LazyOptional<EntitySkills> optionalData = LazyOptional.of(() -> backend);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return INSTANCE.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }
    }

    @EventBusSubscriber
    static class Attacher {
        @SubscribeEvent
        static void attach(final AttachCapabilitiesEvent<Entity> e) {
            e.addCapability(Provider.IDENTIFIER, new Provider());
        }
    }

    public static EntitySkills from(Entity entity) {
        EntitySkills skills = entity.getCapability(INSTANCE).orElseThrow(() -> new IllegalStateException("Could not load entity skills capability"));
        ((EntitySkillImpl) skills).setOwner(entity);
        return skills;
    }
}
