package moe.zapic.clockhud;

import me.shedaniel.autoconfig.AutoConfig;
import moe.zapic.clockhud.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBind {
    public static void bind() {
        var ToggleShowKeyBind = KeyBindingHelper.registerKeyBinding(
                (new KeyBinding(
                        "key.clock-hud.toggle-clock",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F10,
                        "key.clock-hud.category"
                ))
        );
        var ShowConfigKeyBind = KeyBindingHelper.registerKeyBinding(
                (new KeyBinding(
                        "key.clock-hud.open-config",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_HOME,
                        "key.clock-hud.category"
                ))
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ToggleShowKeyBind.wasPressed()) {
                Main.config.isShow = !Main.config.isShow;
            }
            while (ShowConfigKeyBind.wasPressed()) {
                MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(ModConfig.class, client.currentScreen).get());
            }
        });
    }
}
