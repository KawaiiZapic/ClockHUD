package moe.zapic.clockhud;

import me.shedaniel.autoconfig.AutoConfigClient;
import moe.zapic.clockhud.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBind {
    public static void bind() {
        var category = KeyBinding.Category.create(Identifier.of("clock-hud", "main"));
        var ToggleShowKeyBind = KeyBindingHelper.registerKeyBinding(
                (new KeyBinding(
                        "key.clock-hud.toggle-clock",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F10,
                        category
                ))
        );
        var ShowConfigKeyBind = KeyBindingHelper.registerKeyBinding(
                (new KeyBinding(
                        "key.clock-hud.open-config",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_HOME,
                        category
                ))
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ToggleShowKeyBind.wasPressed()) {
                Main.config.isShow = !Main.config.isShow;
            }
            while (ShowConfigKeyBind.wasPressed()) {
                MinecraftClient.getInstance().setScreen(AutoConfigClient.getConfigScreen(ModConfig.class, client.currentScreen).get());
            }
        });
    }
}
