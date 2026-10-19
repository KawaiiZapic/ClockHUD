package moe.zapic.clockhud.platform;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import moe.zapic.clockhud.Main;
import net.minecraft.client.gui.screen.Screen;

public final class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> (Screen) Main.configScreen(parent);
    }
}
