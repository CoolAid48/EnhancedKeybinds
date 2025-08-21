/*
 * Copyright 2020-2023 Siphalor, 2025 MisterCheezeCake, 2025 CoolAid48
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
package de.siphalor.amecs;

import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyModifiers;
import de.siphalor.amecs.compat.ClothConfig;
import de.siphalor.amecs.impl.MetaOptions;
import de.siphalor.amecs.impl.duck.IKeyBindingEntry;
import de.siphalor.amecs.keybinding.BasicSkinTogglesKeybinds;
import de.siphalor.amecs.keybinding.ControlsTogglesKeybinds;
import de.siphalor.amecs.keybinding.IndividualSkinTogglesKeybinds;
import de.siphalor.amecs.mixin.ControlsListWidgetKeyBindingEntryAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of Amecs Reborn (Alt-Meta-Escape-Control-Shift).
 *
 * @author Siphalor, MisterCheezeCake
 */
@Environment(EnvType.CLIENT)
public class Amecs implements ClientModInitializer {

    public static final KeyModifiers CURRENT_MODIFIERS = new KeyModifiers();
    public static boolean TRIGGER_KEYBINDING_ON_SCROLL = true;

    public static final String KEY_PREFIX = "enhanced-keybinds";
    public static final String MOD_NAME_SHORT = "enhanced-keybinds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME_SHORT);
    @Nullable public static KeyBinding ESCAPE_KEYBINDING = null;

    public static String makeKeyID(String keyName) {
        return "key." + "amecs" + "." + keyName;
    }

    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static void registerEscapeKeybind() {
        if (!ClothConfig.get().registerEscapeKeybind) {
            System.out.println("Skin Keybinds are disabled in the config.");
            return;
        } if (MetaOptions.get().escapeKeybind) {
            ESCAPE_KEYBINDING = KeyBindingHelper.registerKeyBinding(new AmecsKeyBinding(Identifier.of(KEY_PREFIX, "alternative_escape"), InputUtil.Type.KEYSYM, -1, "key.categories.misc", new KeyModifiers()));

        }
    }

    public static boolean entryKeyMatches(ControlsListWidget.KeyBindingEntry entry, String keyFilter) {
        if (keyFilter == null) {
            return true;
        }
        return switch (keyFilter) {
            case "" -> ((IKeyBindingEntry) entry).amecs$getKeyBinding().isUnbound();
            case "%" ->
                    ((ControlsListWidgetKeyBindingEntryAccessor) entry).getEditButton().getMessage().getStyle().getColor() == TextColor.fromFormatting(Formatting.RED);
            default ->
                    StringUtils.containsIgnoreCase(((IKeyBindingEntry) entry).amecs$getKeyBinding().getBoundKeyLocalizedText().getString(), keyFilter);
        };
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Beep Boop! The keys are modifying...");
        LOGGER.info("Check out my Hardcore World on Twitch!");

        // Initialize configuration

        MetaOptions.load(false);
        MetaOptions.registerCommand();

        // Register configs and KeyInputHandler
        ClothConfig.register();
        BasicSkinTogglesKeybinds.register();
        IndividualSkinTogglesKeybinds.register();
        ControlsTogglesKeybinds.register();
        ControlsTogglesKeybinds.registerKeyInputs();
        registerEscapeKeybind();
    }

}