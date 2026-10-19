package moe.zapic.clockhud.platform;

import moe.zapic.clockhud.Main;
import net.minecraft.client.util.math.MatrixStack;

public final class HudBridge {
    private HudBridge() {}

    public static void render(MatrixStack context, float frameTicks) {
        Main.render(new MinecraftCanvas(context), frameTicks);
    }
}
