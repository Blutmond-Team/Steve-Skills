package de.blutmondgilde.stevesskills.event;

import de.blutmondgilde.stevesskills.skill.Skill;
import de.blutmondgilde.stevesskills.skill.SkillInstance;
import de.blutmondgilde.stevesskills.skill.wrapper.DefaultSkillWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@RequiredArgsConstructor
public class SkillEvent extends Event {
    @Getter
    private final SkillInstance skillInstance;

    /**
     * Fired whenever a new {@link Skill} gets unlocked.
     * <p>
     * Cancel the {@link UnlockedEvent} to prevent the {@link Entity} to learn this {@link Skill}.
     */
    @Cancelable
    public static class UnlockedEvent extends SkillEvent {
        @Getter
        private final Entity target;

        public UnlockedEvent(SkillInstance skillInstance, Entity target) {
            super(skillInstance);
            this.target = target;
        }
    }

    /**
     * Fired whenever a {@link SkillInstance} gains some experience though {@link DefaultSkillWrapper#addSkillExp(double)}.
     */
    public static class ExperienceGainedEvent extends SkillEvent {
        @Getter
        @Setter
        private double amount;

        public ExperienceGainedEvent(SkillInstance skillInstance, double amount) {
            super(skillInstance);
            this.amount = amount;
        }
    }

    /**
     * Fired whenever a {@link SkillInstance} is about to level up
     */
    public static class LevelUpEvent extends SkillEvent {
        @Getter
        private final int oldLevel;
        @Getter
        @Setter
        private int newLevel;

        public LevelUpEvent(SkillInstance skillInstance, int oldLevel, int newLevel) {
            super(skillInstance);
            this.oldLevel = oldLevel;
            this.newLevel = newLevel;
        }
    }
}
