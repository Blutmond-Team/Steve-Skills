package de.blutmondgilde.stevesskills.registrate;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import de.blutmondgilde.stevesskills.skill.Skill;
import de.blutmondgilde.stevesskills.skill.Skills;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SkillBuilder<P extends AbstractRegistrate<P>> extends AbstractBuilder<Skill, Skill, P, SkillBuilder<P>> {
    private final List<Pair<Class<?>, Function<Skill, Consumer<? extends Event>>>> eventListener = new ArrayList<>();

    @Nullable
    private final Supplier<Skill> factory;


    public SkillBuilder(P parent, String name, BuilderCallback callback, @Nullable Supplier<Skill> factory) {
        super(parent, parent, name, callback, Skills.REGISTRY_KEY);
        this.factory = factory;
    }

    @Override
    protected Skill createEntry() {
        Skill action = this.factory == null ? new Skill() : this.factory.get();
        this.eventListener.forEach(classFunctionPair -> action.addEventListener(classFunctionPair.getFirst(), classFunctionPair.getSecond().apply(action)));
        return action;
    }

    public <E extends Event> SkillBuilder<P> addEventListener(Function<Skill, Consumer<E>> eventHandler) {
        return addEventListener(null, eventHandler);
    }

    public <E extends Event, G> SkillBuilder<P> addEventListener(Class<G> genericClass, Function<Skill, Consumer<E>> eventHandler) {
        // Cast the stuff because java is smart
        Pair<Class<?>, Function<Skill, Consumer<? extends Event>>> pair = (Pair<Class<?>, Function<Skill, Consumer<? extends Event>>>) (Object) Pair.of(genericClass, eventHandler);
        eventListener.add(pair);
        return this;
    }
}
