package de.blutmondgilde.stevesskills.skill;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


public class Skill {
    public static final Codec<Skill> CODEC = ExtraCodecs.lazyInitializedCodec(() -> Skills.REGISTRY_SUPPLIER.get().getCodec());
    private final List<Pair<Class<?>, Consumer<? extends Event>>> eventListener = new ArrayList<>();

    public void addEventListener(Consumer<? extends Event> listener) {
        addEventListener(null, listener);
    }

    public void addEventListener(Class<?> genericClass, Consumer<? extends Event> listener) {
        eventListener.add(Pair.of(genericClass, listener));
    }

    public void registerEventListener() {
        eventListener.forEach(event -> {
            // Register Generic Events
            if (event.getFirst() != null) {
                try {
                    Method addGenericListenerMethod = MinecraftForge.EVENT_BUS.getClass().getDeclaredMethod("addGenericListener", Class.class, Consumer.class);
                    addGenericListenerMethod.invoke(MinecraftForge.EVENT_BUS, event.getFirst(), event.getSecond());
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Register Events
                MinecraftForge.EVENT_BUS.addListener(event.getSecond());
            }
        });
        // Prevent duplicate event listener
        this.eventListener.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill action)) return false;
        return Objects.equals(eventListener, action.eventListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventListener);
    }
}
