package de.blutmondgilde.stevesskills.skill.wrapper;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import de.blutmondgilde.stevesskills.Config;
import de.blutmondgilde.stevesskills.client.overlay.SkillExpOverlay;
import de.blutmondgilde.stevesskills.event.SkillEvent.ExperienceGainedEvent;
import de.blutmondgilde.stevesskills.event.SkillEvent.LevelUpEvent;
import de.blutmondgilde.stevesskills.skill.SkillInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;

public interface LevelableSkillWrapper {
    SkillInstance getSkillInstance();

    int getSkillLevel();

    void setSkillLevel(int level);

    default void levelUp() {
        LevelUpEvent event = new LevelUpEvent(getSkillInstance(), getSkillLevel(), getSkillLevel() + 1);
        MinecraftForge.EVENT_BUS.post(event);
        setSkillLevel(event.getNewLevel());
        setSkillExp(0);
    }

    double getSkillExp();

    void setSkillExp(double exp);

    default void addSkillExp(double exp) {
        ExperienceGainedEvent event = new ExperienceGainedEvent(getSkillInstance(), exp);
        MinecraftForge.EVENT_BUS.post(event);
        setSkillExp(getSkillExp() + event.getAmount());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SkillExpOverlay.addMessage(getSkillInstance().getSkill(), event.getAmount()));

        if (getRequiredExp() <= getSkillExp()) {
            levelUp();
        }
    }

    default double getRequiredExp() {
        try {
            return Config.ENDURANCE_LEVEL_EXPRESSION
                    .with("lvl", getSkillLevel())
                    .evaluate()
                    .getNumberValue()
                    .doubleValue();
        } catch (EvaluationException | ParseException e) {
            e.printStackTrace();
        }

        return Double.MAX_VALUE;
    }
}
