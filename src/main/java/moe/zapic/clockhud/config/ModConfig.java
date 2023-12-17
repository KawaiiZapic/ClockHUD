package moe.zapic.clockhud.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "clock-hud")
public class ModConfig implements ConfigData, ModMenuApi {
    public boolean isShow = true;
    public boolean showDayCount = true;
    public int ScreenX = 4;
    public int ScreenY = 4;
    @ConfigEntry.BoundedDiscrete(min = 25, max = 200)
    public int Scale = 100;

    @ConfigEntry.BoundedDiscrete(min = 100, max = 200)
    public int TipScale = 150;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
    public int Opacity = 255;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }
}
