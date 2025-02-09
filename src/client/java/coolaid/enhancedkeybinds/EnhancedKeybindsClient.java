package coolaid.enhancedkeybinds;

import coolaid.enhancedkeybinds.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnhancedKeybindsClient implements ClientModInitializer {


	public static final String MOD_ID = "enhanced-keybinds";

	// This logger is used to write text to the console and the log file.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("The keys are binding...");
		LOGGER.info("Check out my Hardcore World on Twitch!");

		enhancedKeybindsConfig.register();
		KeyInputHandler.register();
		KeyInputHandler.registerKeyInputs();
	}
}