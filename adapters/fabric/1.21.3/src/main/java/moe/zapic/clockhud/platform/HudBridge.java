package moe.zapic.clockhud.platform;

import moe.zapic.clockhud.Main;
import net.minecraft.client.gui.DrawContext;

public final class HudBridge {
    private HudBridge() {}

    public static void render(DrawContext context, float frameTicks) {
        Main.render(new MinecraftCanvas(context), frameTicks);
    }
}
