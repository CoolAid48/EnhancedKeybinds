package coolaid.enhancedkeybinds;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.RequiresRestart;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "enhanced-keybinds")
public class enhancedKeybindsConfig implements ConfigData {

    @RequiresRestart
    public boolean enableKeybinds = true; // Toggles the new keybinds appearing

    public static void register() {
        AutoConfig.register(enhancedKeybindsConfig.class, GsonConfigSerializer::new);
    }

    public static enhancedKeybindsConfig get() {
        return AutoConfig.getConfigHolder(enhancedKeybindsConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(enhancedKeybindsConfig.class).save();
    }
}
