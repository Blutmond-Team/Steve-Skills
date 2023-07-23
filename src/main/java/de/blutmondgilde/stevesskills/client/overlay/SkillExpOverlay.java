package de.blutmondgilde.stevesskills.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.stevesskills.client.animation.LinearProgression;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class SkillExpOverlay {
    private static volatile List<AnimatedExpMessage> messages = new CopyOnWriteArrayList<>();

    public static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        for (int i = 0; i < messages.size(); i++) {
            AnimatedExpMessage message = messages.get(i);
            message.setIndex(i);
            message.render(gui, guiGraphics, partialTick, screenWidth, screenHeight);
        }
        messages.removeIf(message -> message.remove);
    }

    public static void addMessage(Skill sourceSkill, double amount) {
//        Optional<AnimatedExpMessage> message = messages.stream()
//                .filter(animatedExpMessage -> animatedExpMessage.isFor(sourceSkill))
//                .findFirst();
//        if (message.isPresent()) {
//            message.get().increaseAmount(amount);
//        } else {
        messages.add(new AnimatedExpMessage(sourceSkill, amount));
//        }
    }

    public static class AnimatedExpMessage implements IGuiOverlay {
        private static final int ANIMATION_DURATION = 5 * 20;
        @Getter
        @Setter
        private int index = 0;
        private LinearProgression slideDownAnimation = null;
        private LinearProgression fadeOutAnimation = null;
        private final Skill skill;
        private double amount;
        private boolean remove = false;

        public AnimatedExpMessage(Skill skill, double amount) {
            this.skill = skill;
            this.amount = amount;
        }

        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            // Exit if too many elements are already rendering
            if (!shouldRender(gui.getMinecraft(), screenHeight)) return;
            // Handle flowing down animation
            if (this.slideDownAnimation == null) {
                this.slideDownAnimation = new LinearProgression(gui.getMinecraft().level.getGameTime(), ANIMATION_DURATION, 0, 0);
            }
            FormattedCharSequence text = Component.translatable("message.stevesskills.skill.exp.gained", BigDecimal.valueOf(this.amount).setScale(2, RoundingMode.HALF_UP).doubleValue(), SkillNames.of(this.skill)).getVisualOrderText();
            drawText(text, gui, guiGraphics, screenWidth, screenHeight, partialTick);
        }

        private void drawText(FormattedCharSequence text, ForgeGui gui, GuiGraphics guiGraphics, int screenWidth, int screenHeight, float partialTick) {
            this.slideDownAnimation.updateLimits(screenHeight / 2.0, screenHeight - gui.getMinecraft().font.lineHeight * (this.index + 1));
            PoseStack stack = guiGraphics.pose();
            Font font = gui.getFont();
            int width = font.width(text);
            float scale = 0.6F;
            stack.scale(scale, scale, scale);
            float xPos = screenWidth / scale - width - 4 / scale;
            float yPos = (float) this.slideDownAnimation.getCurrentValue(partialTick);
            yPos /= scale;
            if (this.slideDownAnimation.isDone()) {
                if (this.fadeOutAnimation == null) {
                    this.fadeOutAnimation = new LinearProgression(gui.getMinecraft().level.getGameTime(), ANIMATION_DURATION, 0F, 1F);
                }

                RenderSystem.setShaderColor(1F, 1F, 1F, 1F - this.fadeOutAnimation.getProgress(partialTick));

                if (this.fadeOutAnimation.isDone()) {
                    this.remove = true;
                }
            }

            guiGraphics.drawString(gui.getFont(), text, xPos, yPos, Color.WHITE.getRGB(), true);
            stack.scale(1 / scale, 1 / scale, 1 / scale);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        }

        public boolean shouldRender(Minecraft minecraft, int screenHeight) {
            return this.index * (minecraft.font.lineHeight + 2) < screenHeight / 2;
        }

        public void increaseAmount(double amount) {
            this.amount += amount;
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
