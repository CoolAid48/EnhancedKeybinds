/*
 * Copyright 2020-2023 Siphalor, 2025 CoolAid48
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.siphalor.amecs.keybinding;

import de.siphalor.amecs.compat.ClothConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ControlsTogglesKeybinds {

    private static KeyBinding autoJumpToggleKey;
    private static KeyBinding crouchToggleKey;
    private static KeyBinding sprintToggleKey;
    private static KeyBinding subtitlesToggleKey;

    // KEYPRESS LOGIC
    public static void registerKeyInputs() {

        // Listen for Auto-Jump keypress
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (autoJumpToggleKey != null) {
                while (autoJumpToggleKey.wasPressed()) {
                    if (client.options != null) {
                        // Get current state
                        boolean currentState = client.options.getAutoJump().getValue();

                        // Toggle state by directly setting it to the opposite of the current state
                        client.options.getAutoJump().setValue(!currentState);
                        client.options.write();

                        // Send actionbar message to the player
                        String messageKey = currentState ? "message.auto-jump.disabled" : "message.auto-jump.enabled";
                        Formatting color = currentState ? Formatting.RED : Formatting.GREEN;
                        client.player.sendMessage(
                                Text.translatable(messageKey).formatted(color),
                                true
                        );
                    }
                }
            }
        });
        // Listen for Crouching keypress
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (crouchToggleKey != null) {
                while (crouchToggleKey.wasPressed()) {
                    if (client.options != null && client.player != null) {
                        // Get and Set the current sprinting state, then write to options.txt
                        boolean currentState = client.options.getSneakToggled().getValue();
                        client.options.getSneakToggled().setValue(!currentState);
                        client.options.write();

                        // Disable sneaking when toggled | Simulate releasing the sneak key in "Hold" mode
                        if (currentState) {
                            client.options.sneakKey.setPressed(false);

                        }

                        // Send actionbar message to the player
                        String messageKey = currentState ? "message.crouching.disabled" : "message.crouching.enabled";
                        Formatting color = currentState ? Formatting.RED : Formatting.GREEN;
                        client.player.sendMessage(
                                Text.translatable(messageKey).formatted(color),
                                true
                        );
                    }
                }
            }
        });
        // Listen for Sprinting keypress
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (sprintToggleKey != null) {
                while (sprintToggleKey.wasPressed()) {
                    if (client.options != null && client.player != null) {
                        // Get and Set the current sprinting state, then write to options.txt
                        boolean currentState = client.options.getSprintToggled().getValue();
                        client.options.getSprintToggled().setValue(!currentState);
                        client.options.write();

					    /* If turning off sneaking, ensure the player stops sneaking
					    if (currentState) {
						// Simulate releasing the sneak key in "Hold" mode
						client.options.sprintKey.setPressed(false);
					    } NOT REALLY NECESSARY */

                        // Send actionbar message to the player
                        String messageKey = currentState ? "message.sprinting.disabled" : "message.sprinting.enabled";
                        Formatting color = currentState ? Formatting.RED : Formatting.GREEN;
                        client.player.sendMessage(
                                Text.translatable(messageKey).formatted(color),
                                true
                        );
                    }
                }
            }
        });
        // Listen for Subtitles keypress
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (subtitlesToggleKey != null) {
                while (subtitlesToggleKey.wasPressed()) {
                    if (client.options != null) {
                        // Get current subtitles state
                        boolean currentState = client.options.getShowSubtitles().getValue();

                        // Toggle state by directly setting it to the opposite of the current state
                        client.options.getShowSubtitles().setValue(!currentState);
                        client.options.write();

                        // Send actionbar message to the player
                        String messageKey = currentState ? "message.subtitles.disabled" : "message.subtitles.enabled";
                        Formatting color = currentState ? Formatting.RED : Formatting.GREEN;
                        client.player.sendMessage(
                                Text.translatable(messageKey).formatted(color),
                                true
                        );
                    }
                }
            }
        });
    }

    // REGISTER KEYBINDS
    public static void register() {

        // Checks if keybinds are disabled in ClothConfig
        if (!ClothConfig.get().enableControlsBinds) {
            System.out.println("Options Keybinds are disabled in the config.");
            return;
        }

        autoJumpToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.enhanced-keybinds.auto-jump",  // Translation
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.enhanced-keybinds.controls"    // Category
        ));
        crouchToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.enhanced-keybinds.crouching",  // Translation
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.enhanced-keybinds.controls"// Category
        ));
        sprintToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.enhanced-keybinds.sprinting",  // Translation
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.enhanced-keybinds.controls"// Category
        ));
        subtitlesToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.enhanced-keybinds.subtitles",  // Translation
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.enhanced-keybinds.controls"         // Category
        ));
    }
}