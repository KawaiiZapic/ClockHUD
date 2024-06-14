package moe.zapic.clockhud.mixin;

import moe.zapic.clockhud.render.ClockRender;
import moe.zapic.clockhud.render.DayCountRender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class RenderMixin {

	@Inject(at = @At("TAIL"), method = "render")
	public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		ClockRender.render(context, tickCounter);
		DayCountRender.render(context, tickCounter);
	}
}
