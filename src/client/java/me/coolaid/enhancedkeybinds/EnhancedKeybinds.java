package me.coolaid.enhancedkeybinds;

import me.coolaid.enhancedkeybinds.compat.Controlling;
import me.coolaid.enhancedkeybinds.config.Config;
import me.coolaid.enhancedkeybinds.compat.ClothConfig.clothConfig;
import me.coolaid.enhancedkeybinds.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnhancedKeybinds implements ClientModInitializer {

	public static final String MOD_ID = "enhanced-keybinds";

	// This logger is used to write text to the console and the log file.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	private static Config Config;
	private static boolean nonConflictKeys;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Beep Boop! The keys are modifying...");
		LOGGER.info("Check out my Hardcore World on Twitch!");

		clothConfig.register();
		KeyInputHandler.register();
		KeyInputHandler.registerKeyInputs();

		Config = Config.of("ModernKeyBinding-Config").provider(path ->
				"#ModernKeyBinding-Config" + "\n"
						+ "nonConflictKeys=true"
		).request();
		nonConflictKeys = Config.getOrDefault("nonConflictKeys", false);
		if (FabricLoader.getInstance().isModLoaded("controlling")) {
			try {
				Controlling.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Config getConfig() {
		return Config;
	}

	public static boolean nonConflictKeys() {
		return nonConflictKeys;
	}
}

