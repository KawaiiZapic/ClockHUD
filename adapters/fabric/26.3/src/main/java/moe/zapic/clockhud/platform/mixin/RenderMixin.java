package moe.zapic.clockhud.platform.mixin;

import moe.zapic.clockhud.platform.HudBridge;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class RenderMixin {
    @Inject(method = "extractBossOverlay", at = @At("TAIL"))
    private void clockhud$render(GuiGraphicsExtractor context, DeltaTracker counter, CallbackInfo ci) {
        HudBridge.render(context, counter.getGameTimeDeltaTicks());
    }
}
