package de.blutmondgilde.stevesskills.skill.action;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;


public class SkillAction {
    public static final Codec<SkillAction> CODEC = ExtraCodecs.lazyInitializedCodec(() -> SkillActions.REGISTRY_SUPPLIER.get().getCodec());
    private final List<Pair<Class<?>, Consumer<? extends Event>>> eventListener;

    public SkillAction(List<Pair<Class<?>, Consumer<? extends Event>>> eventListener) {
        this.eventListener = Lists.newArrayList(eventListener);
    }

    public SkillAction(Class<?> eventClass, Consumer<? extends Event> eventHandler) {
        this.eventListener = Lists.newArrayList(Pair.of(eventClass, eventHandler));
    }

    public SkillAction() {
        this.eventListener = Lists.newArrayList();
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
}
