package moe.zapic.clockhud.render;

import com.mojang.blaze3d.systems.RenderSystem;
import moe.zapic.clockhud.Main;
import moe.zapic.clockhud.Textures;
import moe.zapic.clockhud.Utils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ClockRender {
    public static void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        var config = Main.config;
        if (!config.isShow) { return; }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,config.Opacity / 255.0f);
        matrices.push();

        var RealScale = Textures.SCALE * (config.Scale / 100f);
        matrices.scale(RealScale,RealScale,RealScale);

        var RealX = config.ScreenX + (Textures.ICON_S - Textures.DOT_S) / 2;
        var RealY = config.ScreenY + (Textures.ICON_S - Textures.BAR_H) / 2 - 1;
        drawTexture(matrices, Textures.BAR, RealX, RealY, Textures.BAR_W, Textures.BAR_H);

        var IconX = config.ScreenX + (int) (Utils.getScaleTime() * (Textures.BAR_W - Textures.DOT_S));
        drawTexture(matrices, Utils.isDay() ? Textures.SUN : Textures.MOON, IconX, config.ScreenY, Textures.ICON_S, Textures.ICON_S);

        matrices.pop();
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
    }

    private static void drawTexture(MatrixStack matrices, Identifier texture, int x, int y, int w, int h) {
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(matrices, x, y, 0f, 0f, w, h, w, h);
    }
}
