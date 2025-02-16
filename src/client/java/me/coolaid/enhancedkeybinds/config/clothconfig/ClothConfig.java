package me.coolaid.enhancedkeybinds.config.clothconfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.RequiresRestart;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import me.coolaid.enhancedkeybinds.EnhancedKeybinds;


@Config(name = EnhancedKeybinds.MOD_ID)
public class ClothConfig implements ConfigData {

    @RequiresRestart
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean enableKeybinds = true;   // Toggles the new keybinds appearing
    @RequiresRestart
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean nonConflictKeys = false; // Toggle for non-conflicting keybinds


    public static void register() {
        AutoConfig.register(ClothConfig.class, GsonConfigSerializer::new);
    }

    public static ClothConfig get() {
        return AutoConfig.getConfigHolder(ClothConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(ClothConfig.class).save();
    }
}
