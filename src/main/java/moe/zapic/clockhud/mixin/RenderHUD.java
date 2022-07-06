package moe.zapic.clockhud.mixin;

import moe.zapic.clockhud.ClockHUD;
import moe.zapic.clockhud.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import moe.zapic.clockhud.Textures;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class RenderHUD {

	@Inject(at = @At("TAIL"), method = "render")
	public void renderClock(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		if (!ClockHUD.config.isShow) { return; }
		RenderSystem.enableBlend();
		matrices.push();
		matrices.scale(Textures.SCALE,Textures.SCALE,Textures.SCALE);
		RenderSystem.setShaderTexture(0, Textures.BAR);
		var RealX = ClockHUD.config.ScreenX + (Textures.ICON_S - Textures.DOT_S) / 2;
		var RealY = ClockHUD.config.ScreenY + (Textures.ICON_S - Textures.BAR_H) / 2 - 1;
		DrawableHelper.drawTexture(
				matrices,
				RealX,
				RealY,
				0f,
				0f,
				Textures.BAR_W,
				Textures.BAR_H,
				Textures.BAR_W,
				Textures.BAR_H
		);
		var icon = Utils.isDay() ? Textures.SUN : Textures.MOON;
		RenderSystem.setShaderTexture(0, icon);
		var IconX = ClockHUD.config.ScreenX + (int) (Utils.getScaleTime() * (Textures.BAR_W - Textures.DOT_S));
		DrawableHelper.drawTexture(
				matrices,
				IconX,
				ClockHUD.config.ScreenY,
				0f,
				0f,
				Textures.ICON_S,
				Textures.ICON_S,
				Textures.ICON_S,
				Textures.ICON_S
		);
		matrices.pop();
	}
}
