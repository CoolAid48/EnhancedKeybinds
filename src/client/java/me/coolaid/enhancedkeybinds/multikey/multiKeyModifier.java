package me.coolaid.enhancedkeybinds.multikey;

import me.coolaid.enhancedkeybinds.event.InterKeyConflictContext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public enum multiKeyModifier {
    CONTROL {
        @Override
        public boolean matches(InputUtil.Key key) {
            int keyCode = key.getCode();
            if (MinecraftClient.IS_SYSTEM_MAC) {
                return keyCode == GLFW.GLFW_KEY_LEFT_ALT || keyCode == GLFW.GLFW_KEY_RIGHT_ALT;
            } else {
                return keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL;
            }
        }

        @Override
        public boolean isActive(@Nullable InterKeyConflictContext conflictContext) {
            return Screen.hasControlDown();
        }

        @Override
        public Text getCombinedName(InputUtil.Key key, Supplier<Text> defaultLogic) {
            String fm = MinecraftClient.IS_SYSTEM_MAC ? "CMD + " : "CTRL + ";
            return Text.literal(fm + defaultLogic.get().getString());
        }
    },
    SHIFT {
        @Override
        public boolean matches(InputUtil.Key key) {
            return key.getCode() == GLFW.GLFW_KEY_LEFT_SHIFT || key.getCode() == GLFW.GLFW_KEY_RIGHT_SHIFT;
        }

        @Override
        public boolean isActive(@Nullable InterKeyConflictContext conflictContext) {
            return Screen.hasShiftDown();
        }

        @Override
        public Text getCombinedName(InputUtil.Key key, Supplier<Text> defaultLogic) {
            return Text.literal("SHIFT + " + defaultLogic.get().getString());
        }
    },
    ALT {
        @Override
        public boolean matches(InputUtil.Key key) {
            return key.getCode() == GLFW.GLFW_KEY_LEFT_ALT || key.getCode() == GLFW.GLFW_KEY_RIGHT_ALT;
        }

        @Override
        public boolean isActive(@Nullable InterKeyConflictContext conflictContext) {
            return Screen.hasAltDown();
        }

        @Override
        public Text getCombinedName(InputUtil.Key keyCode, Supplier<Text> defaultLogic) {
            return Text.literal("ALT + " + defaultLogic.get().getString());
        }
    },
    NONE {
        @Override
        public boolean matches(InputUtil.Key key) {
            return false;
        }

        @Override
        public boolean isActive(@Nullable InterKeyConflictContext conflictContext) {
            if (conflictContext != null && !conflictContext.conflicts(multiKeyConflictContext.IN_GAME)) {
                for (multiKeyModifier keyModifier : MODIFIER_VALUES) {
                    if (keyModifier.isActive(conflictContext)) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public Text getCombinedName(InputUtil.Key key, Supplier<Text> defaultLogic) {
            return defaultLogic.get();
        }
    };

    public static final multiKeyModifier[] MODIFIER_VALUES = {SHIFT, CONTROL, ALT};

    public static multiKeyModifier getActiveModifier() {
        for (multiKeyModifier keyModifier : MODIFIER_VALUES) {
            if (keyModifier.isActive(null)) {
                return keyModifier;
            }
        }
        return NONE;
    }

    public static boolean isKeyCodeModifier(InputUtil.Key key) {
        for (multiKeyModifier keyModifier : MODIFIER_VALUES) {
            if (keyModifier.matches(key)) {
                return true;
            }
        }
        return false;
    }

    public static multiKeyModifier valueFromString(String stringValue) {
        try {
            return valueOf(stringValue.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException ignored) {
            return NONE;
        }
    }

    public abstract boolean matches(InputUtil.Key key);

    public abstract boolean isActive(@Nullable InterKeyConflictContext conflictContext);

    public abstract Text getCombinedName(InputUtil.Key key, Supplier<Text> defaultLogic);
}