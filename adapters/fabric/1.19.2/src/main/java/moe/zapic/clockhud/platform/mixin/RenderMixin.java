package moe.zapic.clockhud.platform.mixin;

import moe.zapic.clockhud.platform.HudBridge;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class RenderMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void clockhud$render(MatrixStack context, float tickDelta, CallbackInfo ci) {
        var client = MinecraftClient.getInstance();
        HudBridge.render(context, client.getLastFrameDuration());
    }
}
