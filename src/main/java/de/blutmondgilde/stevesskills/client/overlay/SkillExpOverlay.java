package de.blutmondgilde.stevesskills.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.stevesskills.skill.Skill;
import de.blutmondgilde.stevesskills.skill.SkillNames;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class SkillExpOverlay {
    private static volatile List<AnimatedExpMessage> messages = new CopyOnWriteArrayList<>();

    public static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        List<AnimatedExpMessage> toBeRemoved = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            AnimatedExpMessage message = messages.get(i);
            message.setIndex(i);
            message.render(gui, guiGraphics, partialTick, screenWidth, screenHeight);
            if (message.remove) toBeRemoved.add(message);
        }
        messages.removeAll(toBeRemoved);
    }

    public static void addMessage(Skill sourceSkill, double amount) {
        Minecraft minecraft = Minecraft.getInstance();
        Optional<AnimatedExpMessage> message = messages.stream()
                .filter(animatedExpMessage -> animatedExpMessage.isFor(sourceSkill))
                .findFirst();
        if (message.isPresent()) {
            message.get().increaseAmount(minecraft.level.getGameTime(), amount);
        } else {
            messages.add(new AnimatedExpMessage(sourceSkill, amount, minecraft.level.getGameTime()));
        }
    }

    public static class AnimatedExpMessage implements IGuiOverlay {
        private static final int ANIMATION_DURATION = 5 * 20;
        @Getter
        @Setter
        private int index = 0;
        private long startedAt;
        private final Skill skill;
        private double amount;
        private boolean remove = false;

        public AnimatedExpMessage(Skill skill, double amount, long gameTime) {
            this.skill = skill;
            this.amount = amount;
            this.startedAt = gameTime;
        }

        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            // Exit if too many elements are already rendering
            if (!shouldRender(gui.getMinecraft(), screenHeight)) return;
            FormattedCharSequence text = Component.translatable("message.stevesskills.skill.exp.gained", BigDecimal.valueOf(this.amount).setScale(2, RoundingMode.HALF_UP).doubleValue(), SkillNames.of(this.skill)).getVisualOrderText();
            drawText(text, gui, guiGraphics, screenWidth, screenHeight, partialTick);
        }

        private void drawText(FormattedCharSequence text, ForgeGui gui, GuiGraphics guiGraphics, int screenWidth, int screenHeight, float partialTick) {
            PoseStack stack = guiGraphics.pose();
            Font font = gui.getFont();
            int width = font.width(text);
            float scale = 0.6F;
            stack.scale(scale, scale, scale);
            float xPos = screenWidth / scale - width - 4 / scale;
            float yPos = screenHeight / scale / 2.0F;
            float progess = Math.max(0, Math.min(1, (1F / ANIMATION_DURATION * ((gui.getMinecraft().level.getGameTime() + partialTick) - this.startedAt))));
            float maxYPos = screenHeight / scale - font.lineHeight / scale;

            float yDiff = maxYPos - yPos;
            float yPosWithProgess = yPos + yDiff * progess;

            font.drawInBatch(text, xPos, yPosWithProgess, Color.WHITE.getRGB(), true, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            stack.scale(1 / scale, 1 / scale, 1 / scale);
        }

        public boolean shouldRender(Minecraft minecraft, int screenHeight) {
            return this.index * (minecraft.font.lineHeight + 2) < screenHeight / 2;
        }

        public void increaseAmount(long gameTime, double amount) {
            this.amount += amount;
            this.startedAt = gameTime;
        }

        public boolean isFor(Skill skill) {
            return this.skill.equals(skill);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AnimatedExpMessage message)) return false;
            return Objects.equals(skill, message.skill);
        }

        @Override
        public int hashCode() {
            return Objects.hash(skill);
        }
    }
}
