package moe.zapic.clockhud.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "clock-hud")
public class ModConfig implements ConfigData {
    public boolean isShow = true;
    public int ScreenX = 4;
    public int ScreenY = 4;
    @ConfigEntry.BoundedDiscrete(min = 25, max = 200)
    public int Scale = 100;
}
