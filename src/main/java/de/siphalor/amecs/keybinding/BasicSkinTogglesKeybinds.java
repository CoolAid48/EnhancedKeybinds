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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Arm;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.entity.player.PlayerModelPart;

public class BasicSkinTogglesKeybinds {

    // REGISTER SKIN KEYBINDS
    public static void register() {
        // Checks if keybinds are disabled in ClothConfig
        if (!ClothConfig.get().enableSkinBinds) {
            System.out.println("Skin Keybinds are disabled in the config.");
            return;
        }

        // Swaps main hand
        {
            KeyBinding keyBinding = new KeyBinding(
                    "key.enhanced-keybinds.fliphand",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN,
                    "category.enhanced-keybinds"
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (keyBinding.wasPressed()) {
                    GameOptions gameOptions = client.options;
                    SimpleOption<Arm> arm = gameOptions.getMainArm();
                    Boolean isRight = arm.getValue() == Arm.RIGHT;
                    gameOptions.getMainArm().setValue(isRight ? Arm.LEFT : Arm.RIGHT);
                    gameOptions.write();

                    // Send actionbar message to the player
                    String messageKey = isRight ? "message.main-hand.disabled" : "message.main-hand.enabled";
                    Formatting color = isRight ? Formatting.RED : Formatting.GREEN;
                    client.player.sendMessage(
                            Text.translatable(messageKey).formatted(color),
                            true
                    );
                }
            });

        }   // Toggles all at once
        {
            KeyBinding keyBinding = new KeyBinding(
                    "key.enhanced-keybinds.toggleall",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN,
                    "category.enhanced-keybinds"
            );
            KeyBindingHelper.registerKeyBinding(keyBinding);

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (keyBinding.wasPressed()) {
                    GameOptions gameOptions = client.options;

                    Boolean anyEnabled = false;
                    for (PlayerModelPart p : PlayerModelPart.values()) {
                        if (gameOptions.isPlayerModelPartEnabled(p)) {
                            anyEnabled = true;
                            break;
                        }
                    }

                    for (PlayerModelPart p : PlayerModelPart.values()) {
                        gameOptions.setPlayerModelPart(p, !anyEnabled);
                    }
                    gameOptions.write();

                    // Send actionbar message to the player
                    String messageKey = anyEnabled ? "message.skin-layers.disabled" : "message.skin-layers.enabled";
                    Formatting color = anyEnabled ? Formatting.RED : Formatting.GREEN;
                    client.player.sendMessage(
                            Text.translatable(messageKey).formatted(color),
                            true
                    );
                }
            });
        }
    }
}