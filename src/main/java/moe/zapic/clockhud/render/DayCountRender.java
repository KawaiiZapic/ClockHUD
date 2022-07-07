package moe.zapic.clockhud.render;

import moe.zapic.clockhud.Main;
import moe.zapic.clockhud.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class DayCountRender {
    public static boolean isTextRendering = false;
    public static float renderTime = 0.0f;
    public static long currentDay = -1;
    public static float Duration = 200.0f;
    public static int TextOpacity = 0;

    public static void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if(!Main.config.showDayCount) return;
        checkIsNewDay();
        if (!isTextRendering) return;
        var mc = MinecraftClient.getInstance();
        matrices.push();
        var scale = (Main.config.TipScale / 100.0f) * (1f + 0.25f * (renderTime / Duration));
        matrices.translate(-mc.getWindow().getScaledWidth() * (scale - 1) / 2, - 30 * (scale - 1) / 2,0);
        matrices.scale(scale, scale, 1);
        setRenderStatus();
        DrawableHelper.drawCenteredText(matrices, mc.textRenderer, new TranslatableText("text.clock-hud.new-day-tip", currentDay), mc.getWindow().getScaledWidth() / 2, 30, (TextOpacity << 24) + 0xffffff);
        matrices.pop();
        renderTime += tickDelta;
        if (renderTime >= Duration) {
            isTextRendering = false;
            renderTime = 0.0f;
            TextOpacity = 0;
        }
    }

    public static void setRenderStatus() {
        if(renderTime <= 20) {
            TextOpacity = (int) (0xff * (renderTime / 20));
        } else if (renderTime >= 180) {
            TextOpacity = (int) (0xff * (1 - renderTime / 20));
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
