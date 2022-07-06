package moe.zapic.clockhud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ToggleShowKeyBind.wasPressed()) {
                Config.KeyToggleShow = !Config.KeyToggleShow;
            }
        });
    }
}
