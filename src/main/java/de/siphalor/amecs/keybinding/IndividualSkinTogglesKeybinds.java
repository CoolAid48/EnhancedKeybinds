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
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerModelPart;

public class IndividualSkinTogglesKeybinds {

    // REGISTER INDIVIDUAL SKIN KEYBINDS
    public static void register() {
        // Checks if keybinds are disabled in ClothConfig
        if (!ClothConfig.get().enableIndividualSkinBinds) {
            System.out.println("Skin Keybinds are disabled in the config.");
            return;
        }
        // Toggles individual parts
        {
            for (PlayerModelPart p : PlayerModelPart.values()) {

                KeyBinding keyBinding = new KeyBinding(
                        "key.enhanced-keybinds.togglebody." + p,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "category.enhanced-keybinds"
                );
                KeyBindingHelper.registerKeyBinding(keyBinding);

                ClientTickEvents.END_CLIENT_TICK.register(client -> {
                    while (keyBinding.wasPressed()) {
                        GameOptions gameOptions = client.options;

                        gameOptions.setPlayerModelPart(p, !gameOptions.isPlayerModelPartEnabled(p));
                        gameOptions.write();
                    }
                });
            }
        }
    }
}