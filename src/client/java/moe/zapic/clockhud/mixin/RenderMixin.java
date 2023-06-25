package moe.zapic.clockhud.mixin;

import moe.zapic.clockhud.render.ClockRender;
import moe.zapic.clockhud.render.DayCountRender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class RenderMixin {

	@Inject(at = @At("TAIL"), method = "render")
	public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
		ClockRender.render(context);
		DayCountRender.render(context);
	}
}
