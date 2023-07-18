package de.blutmondgilde.stevesskills.registrate;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import de.blutmondgilde.stevesskills.StevesSkills;
import de.blutmondgilde.stevesskills.skill.action.SkillAction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class StevesRegistrate extends AbstractRegistrate<StevesRegistrate> {
    public StevesRegistrate() {
        super(StevesSkills.MODID);
    }

    public SkillActionBuilder<StevesRegistrate> skillAction(String name) {
        return skillAction(name, null);
    }

    public SkillActionBuilder<StevesRegistrate> skillAction(String name, @Nullable Function<List<Pair<Class<?>, Consumer<? extends Event>>>, SkillAction> factory) {
        return entry(name, builderCallback -> new SkillActionBuilder<>(this, name, builderCallback, factory));
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
