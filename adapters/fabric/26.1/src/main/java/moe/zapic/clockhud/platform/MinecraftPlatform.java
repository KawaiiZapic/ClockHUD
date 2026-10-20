package moe.zapic.clockhud.platform;

import me.shedaniel.autoconfig.AutoConfigClient;
import moe.zapic.clockhud.ClientPlatform;
import moe.zapic.clockhud.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;

public final class MinecraftPlatform implements ClientPlatform {
    @Override
    public KeyCategory registerKeyCategory(String translationKey, String namespace, String path) {
        var category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(namespace, path));
        return (key, defaultKey) -> {
            var mapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                    key, InputConstants.Type.KEYSYM, defaultKey, category));
            return mapping::consumeClick;
        };
    }

    @Override
    public void onClientTick(Runnable callback) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> callback.run());
    }

    @Override
    public boolean hasWorld() {
        return Minecraft.getInstance().level != null;
    }

    @Override
    public long worldTime() {
        return Minecraft.getInstance().level.getDefaultClockTime();
    }

    @Override
    public boolean hudHidden() {
        return false;
    }

    @Override
    public int screenWidth() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }

    @Override
    public Screen currentScreen() {
        return Minecraft.getInstance().screen;
    }

    @Override
    public Screen configScreen(Object parent) {
        return AutoConfigClient.getConfigScreen(ModConfig.class, (Screen) parent).get();
    }

    @Override
    public void setScreen(Object screen) {
        Minecraft.getInstance().setScreen((Screen) screen);
    }
}
