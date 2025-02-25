package me.coolaid.enhancedkeybinds;

import me.coolaid.enhancedkeybinds.compat.Controlling;
import me.coolaid.enhancedkeybinds.config.clothconfig.ClothConfig;
import me.coolaid.enhancedkeybinds.config.Config;
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


		// Register configs and KeyInputHandler
		ClothConfig.register();
		KeyInputHandler.register();
		KeyInputHandler.registerKeyInputs();

		Config = Config.of("enhancedkeybinds-config").provider(path ->
				"# Enhanced Keybinds Config" + "\n"
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

		// Initialize the config
		LOGGER.info("Initializing the custom config...");
		Config customConfig = Config.of("enhancedkeybinds-config").request();
		nonConflictKeys = ClothConfig.get().nonConflictKeys;

		// Sync the config between ClothConfig and custom config
		syncCustomConfigWithCloth();

		// Check if "controlling" mod is loaded
		if (FabricLoader.getInstance().isModLoaded("controlling")) {
			try {
				Controlling.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Synchronizes the custom config with ClothConfig.
	 */
	private void syncCustomConfigWithCloth() {
		String clothConfigValue = String.valueOf(ClothConfig.get().nonConflictKeys);
		// Sync the value with the custom config file
		Config.set("nonConflictKeys", clothConfigValue);
		LOGGER.info("Synchronized nonConflictKeys with custom config: " + clothConfigValue);
	}

	/**
	 * Hook called when Cloth Config is saved.
	 * This synchronizes the properties file with the updated Cloth Config values.
	 */
	public static void onClothConfigSaved() {
		boolean clothConfigValue = ClothConfig.get().nonConflictKeys;

		// Update custom config if the value has changed
		if (nonConflictKeys != clothConfigValue) {
			LOGGER.info("ClothConfig value changed. Updating custom config...");
			Config.set("nonConflictKeys", String.valueOf(clothConfigValue));
			nonConflictKeys = clothConfigValue;
			LOGGER.info("Updated properties file from Cloth Config changes.");
		} else {
			LOGGER.info("ClothConfig value did not change. No update needed.");
		}
	}

	public static Config getConfig() {
		return Config;
	}

	public static boolean nonConflictKeys() {
		return nonConflictKeys;
	}
}