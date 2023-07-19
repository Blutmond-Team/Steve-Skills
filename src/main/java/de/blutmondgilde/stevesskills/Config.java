package de.blutmondgilde.stevesskills;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = StevesSkills.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ConfigValue<String> SKILL_ENDURANCE_LEVEL_EXPRESSION, SKILL_ENDURANCE_HEALTH_EXPRESSION;

    static {
        BUILDER.comment("Skill Configuration").push("skills");

        BUILDER.comment("Endurance Skill Configuration").push("endurance");
        SKILL_ENDURANCE_LEVEL_EXPRESSION = BUILDER
                .comment("Expression used to calculate the required exp to reach the next level up", "Placeholders: lvl", "The expression evaluation is based on EvalEx", "See more here: https://ezylang.github.io/EvalEx/concepts/parsing_evaluation.html")
                .define("levelExpression", "(lvl + 7) ^ 2", Config::validateExpression);
        SKILL_ENDURANCE_HEALTH_EXPRESSION = BUILDER
                .comment("Expression used to calculate the amount of health a user gains with the current level", "Placeholders: lvl", "The expression evaluation is based on EvalEx", "See more here: https://ezylang.github.io/EvalEx/concepts/parsing_evaluation.html")
                .define("healthExpression", "2 * lvl", Config::validateExpression);
        BUILDER.pop();
        BUILDER.pop();
    }

    private static boolean validateExpression(Object obj) {
        if (!(obj instanceof final String string)) return false;
        try {
            Expression expression = new Expression(string);
            expression.with("lvl", 1);
            expression.evaluate();
            return true;
        } catch (ParseException | EvaluationException e) {
            e.printStackTrace();
            return false;
        }
    }

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Expression ENDURANCE_LEVEL_EXPRESSION, ENDURANCE_MEALTH_EXPRESSION;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        ENDURANCE_LEVEL_EXPRESSION = new Expression(SKILL_ENDURANCE_LEVEL_EXPRESSION.get());
        ENDURANCE_MEALTH_EXPRESSION = new Expression(SKILL_ENDURANCE_HEALTH_EXPRESSION.get());
    }
}
