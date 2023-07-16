package de.blutmondgilde.stevesskills.registrate;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import de.blutmondgilde.stevesskills.StevesSkills;
import de.blutmondgilde.stevesskills.skill.action.SkillAction;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
}
