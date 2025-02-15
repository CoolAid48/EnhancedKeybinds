package me.coolaid.enhancedkeybinds.compat.ClothConfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.RequiresRestart;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@me.shedaniel.autoconfig.annotation.Config(name = "enhanced-keybinds")
public class clothConfig implements ConfigData {

    @RequiresRestart
    public boolean enableKeybinds = true; // Toggles the new keybinds appearing

    public static void register() {
        AutoConfig.register(clothConfig.class, GsonConfigSerializer::new);
    }

    public static clothConfig get() {
        return AutoConfig.getConfigHolder(clothConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(clothConfig.class).save();
    }
}
