package moe.zapic.clockhud.platform;

import moe.zapic.clockhud.Main;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class HudBridge {
    private HudBridge() {}

    public static void render(GuiGraphicsExtractor context, float frameTicks) {
        Main.render(new MinecraftCanvas(context), frameTicks);
    }
}
