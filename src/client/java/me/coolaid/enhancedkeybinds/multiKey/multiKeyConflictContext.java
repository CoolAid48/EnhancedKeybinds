package me.coolaid.enhancedkeybinds.multiKey;

import me.coolaid.enhancedkeybinds.event.InterKeyConflictContext;
import net.minecraft.client.MinecraftClient;

public enum multiKeyConflictContext implements InterKeyConflictContext {

     // Key Bindings are universal by default, and are used in every context and will conflict with any other context.
    UNIVERSAL {
        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public boolean conflicts(InterKeyConflictContext other) {
            return true;
        }
    },

    // Gui key bindings are only used when a {@link net.minecraft.client.gui.screen.Screen} is open.
    GUI {
        @Override
        public boolean isActive() {
            return MinecraftClient.getInstance().currentScreen != null;
        }

        @Override
        public boolean conflicts(InterKeyConflictContext other) {
            return this == other;
        }
    },

    // In-game key bindings are only used when a {@link net.minecraft.client.gui.screen.Screen} is not open.
    IN_GAME {
        @Override
        public boolean isActive() {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(InterKeyConflictContext other) {
            return this == other;
        }
    }
}