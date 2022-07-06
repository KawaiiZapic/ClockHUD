package moe.zapic.clockhud;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClockHUD implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ClockHUD");

	@Override
	public void onInitializeClient() {
		LOGGER.info("ClockHUD test build initialized.");
	}
}
