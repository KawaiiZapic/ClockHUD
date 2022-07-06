package moe.zapic.clockhud.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "clock-hud")
public class ModConfig implements ConfigData {
    public boolean isShow = true;
    public int ScreenX = 0;
    public int ScreenY = 0;

//    @ConfigEntry.Gui.Excluded
//    public static int _ScreenW = 0;
//    @ConfigEntry.Gui.Excluded
//    public static int _ScreenH = 0;

//    public static void _initScreenSize() {
//        var client = MinecraftClient.getInstance();
//        if (client.currentScreen != null) {
//            _ScreenW = client.currentScreen.width;
//            _ScreenH = client.currentScreen.height;
//        }
//
//    }
}
