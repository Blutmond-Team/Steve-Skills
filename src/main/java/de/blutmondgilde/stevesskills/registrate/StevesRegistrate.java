package de.blutmondgilde.stevesskills.registrate;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import de.blutmondgilde.stevesskills.StevesSkills;
import de.blutmondgilde.stevesskills.skill.Skill;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StevesRegistrate extends AbstractRegistrate<StevesRegistrate> {
    public StevesRegistrate() {
        super(StevesSkills.MODID);
    }

    public SkillBuilder<StevesRegistrate> skill(String name) {
        return skill(name, null);
    }

    public SkillBuilder<StevesRegistrate> skill(String name, @Nullable Supplier<Skill> factory) {
        return entry(name, builderCallback -> new SkillBuilder<>(this, name, builderCallback, factory));
    }

    @Override
    public StevesRegistrate registerEventListeners(IEventBus bus) {
        return super.registerEventListeners(bus);
    }

    public <R> ResourceKey<Registry<R>> makeDatapackRegistry(String name, Codec<R> codec) {
        final ResourceKey<Registry<R>> registryId = ResourceKey.createRegistryKey(new ResourceLocation(getModid(), name));
        OneTimeEventReceiver.addModListener(this, DataPackRegistryEvent.NewRegistry.class, event -> event.dataPackRegistry(registryId, codec));
        return registryId;
    }

    public <R> Supplier<IForgeRegistry<R>> getForgeRegistrySupplier(ResourceKey<Registry<R>> registryKey) {
        return Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(registryKey));
    }
}
