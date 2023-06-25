package moe.zapic.clockhud;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moe.zapic.clockhud.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ClockHUD");
	public static ModConfig config = null;
	@Override
	public void onInitializeClient() {
		KeyBind.bind();
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return Textures.BAR;
			}
			@Override
			public void reload(ResourceManager manager) {
				var TextureManager = MinecraftClient.getInstance().getTextureManager();
				TextureManager.getTexture(Textures.BAR).setFilter(true, true);
				TextureManager.getTexture(Textures.MOON).setFilter(true, true);
				TextureManager.getTexture(Textures.SUN).setFilter(true, true);
			}
		});
		LOGGER.info("[ClockHUD] Mod initialized.");
	}
}
