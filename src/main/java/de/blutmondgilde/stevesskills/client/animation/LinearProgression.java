package de.blutmondgilde.stevesskills.client.animation;

import net.minecraft.client.Minecraft;

public class LinearProgression {
    private final Minecraft minecraft = Minecraft.getInstance();
    private final long startTimeStamp;
    private final long totalDurationTicks;
    private double minValue;
    private double maxValue;
    private boolean isDone;

    public LinearProgression(long startTimeStamp, long totalDurationTicks, double minValue, double maxValue) {
        this.startTimeStamp = startTimeStamp;
        this.totalDurationTicks = totalDurationTicks;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public boolean isDone() {
        this.isDone = getProgress(0) == 1F;
        return this.isDone;
    }

    public float getProgress(float partialTicks) {
        if (this.isDone) return 1F;
        return 1F / this.totalDurationTicks * (minecraft.level.getGameTime() + partialTicks - this.startTimeStamp);
    }

    public double getCurrentValue(float partialTicks) {
        if (this.isDone) return this.maxValue;
        double diff = this.maxValue - this.minValue;
        diff *= getProgress(partialTicks);
        return Math.min(this.minValue + diff, this.maxValue);
    }

    public LinearProgression rerun(long startTime) {
        return rerun(startTime, this.totalDurationTicks);
    }

    public LinearProgression rerun(long startTime, long duration) {
        return rerun(startTime, duration, this.minValue, this.maxValue);
    }

    public LinearProgression rerun(long startTime, long duration, double min, double max) {
        return new LinearProgression(startTime, duration, min, max);
    }

    public void updateLimits(double min, double max) {
        this.minValue = min;
        this.maxValue = max;
    }
}
