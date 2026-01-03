package moe.zapic.clockhud;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moe.zapic.clockhud.config.ModConfig;
import moe.zapic.clockhud.render.DayCountRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ClockHUD");
	public static ModConfig config = null;
	@Override
	public void onInitializeClient() {
		KeyBind.bind();
		ClientTickEvents.END_WORLD_TICK.register(client -> DayCountRender.checkIsNewDay());
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		LOGGER.info("[ClockHUD] Mod initialized.");
	}
}
