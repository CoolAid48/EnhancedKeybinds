package me.coolaid.enhancedkeybinds.compat.ClothConfig;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class clothConfigScreen {
    public static Screen getScreen(Screen parent) {
        return AutoConfig.getConfigScreen(clothConfig.class, parent).get();
    }
}


