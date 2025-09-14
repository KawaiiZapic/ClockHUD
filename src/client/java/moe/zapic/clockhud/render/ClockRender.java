package moe.zapic.clockhud.render;

import moe.zapic.clockhud.Main;
import moe.zapic.clockhud.Textures;
import moe.zapic.clockhud.Utils;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class ClockRender {
    public static void render(DrawContext context, RenderTickCounter rtc) {
        var config = Main.config;
        if (!config.isShow) { return; }

        var color = ColorHelper.fromFloats(config.Opacity / 255.0f, 1.0f,1.0f,1.0f);

        var matrices = context.getMatrices();
        matrices.pushMatrix();

        var RealScale = Textures.SCALE * (config.Scale / 100f);
        matrices.scale(RealScale,RealScale);

        var RealX = config.ScreenX + (Textures.ICON_S - Textures.DOT_S) / 2;
        var RealY = config.ScreenY + (Textures.ICON_S - Textures.BAR_H) / 2 - 1;
        drawTexture(context, Textures.BAR, RealX, RealY, Textures.BAR_W, Textures.BAR_H, color);

        var IconX = config.ScreenX + (int) (Utils.getScaleTime() * (Textures.BAR_W - Textures.DOT_S));
        drawTexture(context, Utils.isDay() ? Textures.SUN : Textures.MOON, IconX, config.ScreenY, Textures.ICON_S, Textures.ICON_S, color);

        matrices.popMatrix();
    }

    private static void drawTexture(DrawContext context, Identifier texture, int x, int y, int w, int h, int color) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0f, 0f, w, h, w, h, color);
    }
}
