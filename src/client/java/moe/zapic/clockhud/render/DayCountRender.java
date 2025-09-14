package moe.zapic.clockhud.render;

import moe.zapic.clockhud.Main;
import moe.zapic.clockhud.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class DayCountRender {
    public static boolean isTextRendering = false;
    public static float renderTime = 0.0f;
    public static long currentDay = -1;
    public static float Duration = 100.0f;
    public static int TextOpacity = 4;

    public static void render(DrawContext context, RenderTickCounter rtc) {
        if(!Main.config.showDayCount) return;
        if (!isTextRendering) return;
        var mc = MinecraftClient.getInstance();
        var matrices = context.getMatrices();
        matrices.pushMatrix();
        var scale = (Main.config.TipScale / 100.0f) * (1f + 0.25f * (renderTime / Duration));
        matrices.translate(-mc.getWindow().getScaledWidth() * (scale - 1) / 2, - 30 * (scale - 1) / 2);
        matrices.scale(scale, scale);
        setRenderStatus();
        context.drawCenteredTextWithShadow(mc.textRenderer, Text.translatable("text.clock-hud.new-day-tip", currentDay), mc.getWindow().getScaledWidth() / 2, 30, (TextOpacity << 24) + 0xffffff);
        matrices.popMatrix();
        renderTime += rtc.getDynamicDeltaTicks();
        if (renderTime >= Duration) {
            isTextRendering = false;
            renderTime = 0.0f;
        }
    }

    public static void setRenderStatus() {
        if(renderTime <= 20) {
            TextOpacity = (int) (0xff * (renderTime / 20));
        } else if (renderTime >= 80) {
            TextOpacity = (int) (0xff * ((Duration - renderTime) / 20));
        } else if (TextOpacity != 0xff) {
            TextOpacity = 0xff;
        }
    }

    public static void checkIsNewDay() {
        var world = MinecraftClient.getInstance().world;
        assert world != null;
        var day = world.getTimeOfDay() / Utils.DAY_TICKS;
        if (currentDay == -1) {
            currentDay = day;
        }
        if (day == currentDay) return;
        isTextRendering = true;
        currentDay = day;
    }
}
