package moe.zapic.clockhud.core;

import moe.zapic.clockhud.config.ModConfig;

/**
 * Shared behavior. Minecraft types belong in the adapters.
 */
public final class ClockHud {
    private static final int ICON_SIZE = 48;
    private static final int BAR_WIDTH = 400;
    private static final int BAR_HEIGHT = 12;
    private static final int DOT_SIZE = 10;
    private static final float TIP_DURATION = 100;

    private final ModConfig config;
    private long currentDay;
    private boolean hasWorld;
    private boolean showingTip;
    private float tipTime;

    public ClockHud(ModConfig config) {
        this.config = config;
    }

    public void tick(long worldTime) {
        long day = Math.floorDiv(worldTime, WorldClock.DAY_TICKS);
        if (hasWorld && day != currentDay) {
            showingTip = true;
            tipTime = 0;
        }
        currentDay = day;
        hasWorld = true;
    }

    public void disconnect() {
        hasWorld = false;
        showingTip = false;
        tipTime = 0;
    }

    public void render(HudCanvas canvas, long worldTime, float frameTicks, int screenWidth) {
        if (config.isShow) {
            renderClock(canvas, worldTime);
        }
        if (showingTip) {
            if (config.showDayCount) {
                renderTip(canvas, screenWidth);
            }
            tipTime += Math.max(0, frameTicks);
            if (tipTime >= TIP_DURATION) {
                showingTip = false;
                tipTime = 0;
            }
        }
    }

    private void renderClock(HudCanvas canvas, long worldTime) {
        canvas.push();
        try {
            float scale = 0.5f * config.Scale / 100f;
            canvas.scale(scale, scale);
            int color = (Math.max(0, Math.min(255, config.Opacity)) << 24) | 0xffffff;
            int barX = config.ScreenX + (ICON_SIZE - DOT_SIZE) / 2;
            int barY = config.ScreenY + (ICON_SIZE - BAR_HEIGHT) / 2 - 1;
            canvas.texture("bar.png", barX, barY, BAR_WIDTH, BAR_HEIGHT, color);
            int iconX = config.ScreenX + (int) (WorldClock.progress(worldTime) * (BAR_WIDTH - DOT_SIZE));
            canvas.texture(WorldClock.isDay(worldTime) ? "sun.png" : "moon.png",
                    iconX, config.ScreenY, ICON_SIZE, ICON_SIZE, color);
        } finally {
            canvas.pop();
        }
    }

    private void renderTip(HudCanvas canvas, int screenWidth) {
        float scale = config.TipScale / 100f * (1 + 0.25f * tipTime / TIP_DURATION);
        float opacity = Math.min(1, Math.min(tipTime / 20, (TIP_DURATION - tipTime) / 20));
        int alpha = Math.max(0, Math.min(255, (int) (255 * opacity)));
        // Older text renderers treat alpha < 4 as fully opaque.
        if (alpha < 4) {
            return;
        }
        canvas.push();
        try {
            canvas.translate(-screenWidth * (scale - 1) / 2, -30 * (scale - 1) / 2);
            canvas.scale(scale, scale);
            canvas.centeredText("text.clock-hud.new-day-tip", currentDay,
                    screenWidth / 2, 30, (alpha << 24) | 0xffffff);
        } finally {
            canvas.pop();
        }
    }
}
