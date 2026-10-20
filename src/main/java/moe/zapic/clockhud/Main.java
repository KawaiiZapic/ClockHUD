package moe.zapic.clockhud;

import java.util.function.BooleanSupplier;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moe.zapic.clockhud.config.ModConfig;
import moe.zapic.clockhud.core.ClockHud;
import moe.zapic.clockhud.core.HudCanvas;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.LoggerFactory;

public final class Main implements ClientModInitializer {
    private static Main instance;

    // GLFW key codes, shared by all supported versions.
    private static final int KEY_F10 = 299;
    private static final int KEY_HOME = 268;

    private ClientPlatform platform;
    private ModConfig config;
    private ClockHud hud;
    private Runnable saveConfig;
    private BooleanSupplier toggleKey;
    private BooleanSupplier settingsKey;

    @Override
    public void onInitializeClient() {
        var platforms = FabricLoader.getInstance()
                .getEntrypoints("clock-hud:platform", ClientPlatform.class);
        if (platforms.isEmpty()) {
            throw new IllegalStateException(
                    "This version of ClockHUD is not compatible with your Minecraft version. "
                            + "Please install a ClockHUD release that supports your Minecraft version.");
        }
        platform = platforms.get(0);
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        var holder = AutoConfig.getConfigHolder(ModConfig.class);
        config = holder.getConfig();
        saveConfig = holder::save;
        hud = new ClockHud(config);
        var category = platform.registerKeyCategory("key.clock-hud.category", "clock-hud", "main");
        toggleKey = category.registerKey("key.clock-hud.toggle-clock", KEY_F10);
        settingsKey = category.registerKey("key.clock-hud.open-config", KEY_HOME);
        platform.onClientTick(this::tick);
        instance = this;
        LoggerFactory.getLogger("ClockHUD").info("[ClockHUD] Mod initialized.");
    }

    private void tick() {
        while (toggleKey.getAsBoolean()) {
            config.isShow = !config.isShow;
            saveConfig.run();
        }
        while (settingsKey.getAsBoolean()) {
            openConfig();
        }
        if (platform.hasWorld()) {
            hud.tick(platform.worldTime());
        } else {
            hud.disconnect();
        }
    }

    public void openConfig() {
        platform.setScreen(configScreen(platform.currentScreen()));
    }

    public static Object configScreen(Object parent) {
        return instance == null ? parent : instance.platform.configScreen(parent);
    }

    public static void render(HudCanvas canvas, float frameTicks) {
        Main client = instance;
        if (client == null || !client.platform.hasWorld() || client.platform.hudHidden()) {
            return;
        }
        client.hud.render(canvas, client.platform.worldTime(), frameTicks, client.platform.screenWidth());
    }
}
