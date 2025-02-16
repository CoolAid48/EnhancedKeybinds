package me.coolaid.enhancedkeybinds.event;

import me.coolaid.enhancedkeybinds.multikey.multiKeyModifier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public interface InterKeyBinding {
    InputUtil.Key getKey();

    InterKeyConflictContext getKeyConflictContext();

    multiKeyModifier getKeyModifierDefault();

    multiKeyModifier getKeyModifier();

    void setKeyConflictContext(InterKeyConflictContext keyConflictContext);

    void setKeyModifierAndCode(multiKeyModifier keyModifier, InputUtil.Key keyCode);

    void press();

    default boolean isConflictContextAndModifierActive() {
        return getKeyConflictContext().isActive() && getKeyModifier().isActive(getKeyConflictContext());
    }


    // Returns true when one of the key codes conflicts with another's modifier.
    default boolean hasKeyCodeModifierConflict(KeyBinding other) {
        final InterKeyBinding extended = (InterKeyBinding) other;
        if (getKeyConflictContext().conflicts(extended.getKeyConflictContext()) || extended.getKeyConflictContext().conflicts(getKeyConflictContext())) {
            return getKeyModifier().matches(extended.getKey()) || extended.getKeyModifier().matches(getKey());
        }
        return false;
    }


    // Checks that both the key conflict context and modifier are active, and that the keyCode matches the binding.
    default boolean isActiveAndMatches(InputUtil.Key keyCode) {
        return keyCode != InputUtil.UNKNOWN_KEY && keyCode.equals(getKey()) && getKeyConflictContext().isActive() && getKeyModifier().isActive(getKeyConflictContext());
    }

    default void setToDefault() {
        setKeyModifierAndCode(getKeyModifierDefault(), getKeyBinding().getDefaultKey());
    }

    default KeyBinding getKeyBinding() {
        return (KeyBinding) this;
    }
}