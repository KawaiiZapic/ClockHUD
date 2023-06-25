package moe.zapic.clockhud.render;

import com.mojang.blaze3d.systems.RenderSystem;
import moe.zapic.clockhud.Main;
import moe.zapic.clockhud.Textures;
import moe.zapic.clockhud.Utils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ClockRender {
    public static void render(DrawContext context) {
        var config = Main.config;
        if (!config.isShow) { return; }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,config.Opacity / 255.0f);
        var matrices = context.getMatrices();
        matrices.push();

        var RealScale = Textures.SCALE * (config.Scale / 100f);
        matrices.scale(RealScale,RealScale,RealScale);

        var RealX = config.ScreenX + (Textures.ICON_S - Textures.DOT_S) / 2;
        var RealY = config.ScreenY + (Textures.ICON_S - Textures.BAR_H) / 2 - 1;
        drawTexture(context, Textures.BAR, RealX, RealY, Textures.BAR_W, Textures.BAR_H);

        var IconX = config.ScreenX + (int) (Utils.getScaleTime() * (Textures.BAR_W - Textures.DOT_S));
        drawTexture(context, Utils.isDay() ? Textures.SUN : Textures.MOON, IconX, config.ScreenY, Textures.ICON_S, Textures.ICON_S);

        matrices.pop();
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
    }

    private static void drawTexture(DrawContext context, Identifier texture, int x, int y, int w, int h) {
        context.drawTexture(texture, x, y, 0f, 0f, w, h, w, h);
    }
}
