package moe.zapic.clockhud.mixin;

import moe.zapic.clockhud.render.ClockRender;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class RenderMixin {

	@Inject(at = @At("TAIL"), method = "render")
	public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		ClockRender.render(matrices, tickDelta, ci);
	}
}
