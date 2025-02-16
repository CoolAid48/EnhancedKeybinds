package me.coolaid.enhancedkeybinds.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.coolaid.enhancedkeybinds.config.clothconfig.ClothConfigScreen;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClothConfigScreen::getScreen;
    }
}
