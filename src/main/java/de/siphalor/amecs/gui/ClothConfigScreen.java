package de.siphalor.amecs.gui;

import de.siphalor.amecs.compat.ClothConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class ClothConfigScreen {
    public static Screen getScreen(Screen parent) {
        return AutoConfig.getConfigScreen(ClothConfig.class, parent).get();
    }
}


