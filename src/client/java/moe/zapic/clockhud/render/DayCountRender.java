package moe.zapic.clockhud.render;

import moe.zapic.clockhud.Main;
import moe.zapic.clockhud.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class DayCountRender {
    public static boolean isTextRendering = false;
    public static float renderTime = 0.0f;
    public static long currentDay = -1;
    public static float Duration = 100.0f;
    public static int TextOpacity = 4;

    public static void render(DrawContext context) {
        if(!Main.config.showDayCount) return;
        checkIsNewDay();
        if (!isTextRendering) return;
        var mc = MinecraftClient.getInstance();
        var matrices = context.getMatrices();
        matrices.push();
        var scale = (Main.config.TipScale / 100.0f) * (1f + 0.25f * (renderTime / Duration));
        matrices.translate(-mc.getWindow().getScaledWidth() * (scale - 1) / 2, - 30 * (scale - 1) / 2,0);
        matrices.scale(scale, scale, 1);
        setRenderStatus();
        context.drawCenteredTextWithShadow(mc.textRenderer, Text.translatable("text.clock-hud.new-day-tip", currentDay), mc.getWindow().getScaledWidth() / 2, 30, (TextOpacity << 24) + 0xffffff);
        matrices.pop();
        renderTime += mc.getLastFrameDuration();
        if (renderTime >= Duration) {
            isTextRendering = false;
            renderTime = 0.0f;
        }
    }

    public static void setRenderStatus() {
        if(renderTime <= 20) {
            // Mojang shit, will treat all opacity < 0x04 as 0xff
            TextOpacity = (int) ((0xff * (renderTime / 20)) + 4);
        } else if (renderTime >= 80) {
            TextOpacity = (int) ((0xff * ((Duration - renderTime) / 20)) + 4);
        }  else if (TextOpacity != 0xff) {
            TextOpacity = 0xff;
        }
    }

    public static long getDayCount() {
        var world = MinecraftClient.getInstance().world;
        assert world != null;
        return world.getTimeOfDay() / Utils.DAY_TICKS;
    }

    public static void checkIsNewDay() {
        var day = getDayCount();
        if (currentDay == -1) {
            currentDay = day;
        }
        if (day == currentDay) return;

        isTextRendering = day > currentDay;
        currentDay = day;
    }
}
