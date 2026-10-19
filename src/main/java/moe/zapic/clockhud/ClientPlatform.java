package moe.zapic.clockhud;

import java.util.function.BooleanSupplier;

/**
 * Native Minecraft operations. Screen objects cross this boundary as Object;
 * only version proxies convert them to the native Screen type.
 * Feature decisions and event handling belong in Main.
 */
public interface ClientPlatform {
    KeyCategory registerKeyCategory(String translationKey, String namespace, String path);
    void onClientTick(Runnable callback);
    boolean hasWorld();
    long worldTime();
    boolean hudHidden();
    int screenWidth();
    Object currentScreen();
    Object configScreen(Object parent);
    void setScreen(Object screen);

    @FunctionalInterface
    interface KeyCategory {
        BooleanSupplier registerKey(String translationKey, int defaultKey);
    }
}
