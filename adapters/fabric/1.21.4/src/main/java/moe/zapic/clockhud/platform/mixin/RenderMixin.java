package moe.zapic.clockhud.platform.mixin;

import moe.zapic.clockhud.platform.HudBridge;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class RenderMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void clockhud$render(DrawContext context, RenderTickCounter counter, CallbackInfo ci) {
        HudBridge.render(context, counter.getLastFrameDuration());
    }
}
