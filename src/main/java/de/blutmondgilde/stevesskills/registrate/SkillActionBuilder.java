package de.blutmondgilde.stevesskills.registrate;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import de.blutmondgilde.stevesskills.skill.action.SkillAction;
import de.blutmondgilde.stevesskills.skill.action.SkillActions;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SkillActionBuilder<P extends AbstractRegistrate<P>> extends AbstractBuilder<SkillAction, SkillAction, P, SkillActionBuilder<P>> {
    private final List<Pair<Class<?>, Consumer<? extends Event>>> eventListener = new ArrayList<>();

    @Nullable
    private final Function<List<Pair<Class<?>, Consumer<? extends Event>>>, SkillAction> factory;


    public SkillActionBuilder(P parent, String name, BuilderCallback callback, @Nullable Function<List<Pair<Class<?>, Consumer<? extends Event>>>, SkillAction> factory) {
        super(parent, parent, name, callback, SkillActions.REGISTRY_KEY);
        this.factory = factory;
    }

    @Override
    protected SkillAction createEntry() {
        if (this.factory != null) return factory.apply(eventListener);
        return new SkillAction(this.eventListener);
    }

    public <E extends Event> SkillActionBuilder<P> addEventListener(Consumer<E> eventHandler) {
        eventListener.add(Pair.of(null, eventHandler));
        return this;
    }

    public <E extends Event, G> SkillActionBuilder<P> addEventListener(Class<G> genericClass, Consumer<E> eventHandler) {
        eventListener.add(Pair.of(genericClass, eventHandler));
        return this;
    }
}
