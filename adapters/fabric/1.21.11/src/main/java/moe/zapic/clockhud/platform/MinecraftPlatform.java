package moe.zapic.clockhud.platform;

import me.shedaniel.autoconfig.AutoConfigClient;
import moe.zapic.clockhud.ClientPlatform;
import moe.zapic.clockhud.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public final class MinecraftPlatform implements ClientPlatform {
    @Override
    public KeyCategory registerKeyCategory(String translationKey, String namespace, String path) {
        var category = KeyBinding.Category.create(Identifier.of(namespace, path));
        return (key, defaultKey) -> {
            var binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    key, InputUtil.Type.KEYSYM, defaultKey, category));
            return binding::wasPressed;
        };
    }

    @Override
    public void onClientTick(Runnable callback) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> callback.run());
    }

    @Override
    public boolean hasWorld() {
        return MinecraftClient.getInstance().world != null;
    }

    @Override
    public long worldTime() {
        return MinecraftClient.getInstance().world.getTimeOfDay();
    }

    @Override
    public boolean hudHidden() {
        return MinecraftClient.getInstance().options.hudHidden;
    }

    @Override
    public int screenWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    @Override
    public Screen currentScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    @Override
    public Screen configScreen(Object parent) {
        return AutoConfigClient.getConfigScreen(ModConfig.class, (Screen) parent).get();
    }

    @Override
    public void setScreen(Object screen) {
        MinecraftClient.getInstance().setScreen((Screen) screen);
    }
}
