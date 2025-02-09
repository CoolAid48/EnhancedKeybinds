package coolaid.enhancedkeybinds;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class configScreen {
    public static Screen getScreen(Screen parent) {
        return AutoConfig.getConfigScreen(enhancedKeybindsConfig.class, parent).get();
    }
}


