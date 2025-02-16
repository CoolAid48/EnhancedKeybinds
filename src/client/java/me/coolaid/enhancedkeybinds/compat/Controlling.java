package me.coolaid.enhancedkeybinds.compat;

import com.blamejared.controlling.api.event.ControllingEvents;
import me.coolaid.enhancedkeybinds.multikey.multiKeyModifier;
import me.coolaid.enhancedkeybinds.event.InterKeyBinding;

public class Controlling {
    public static void init() {
        ControllingEvents.HAS_CONFLICTING_MODIFIERS_EVENT
                .register(event -> ((InterKeyBinding) event.thisMapping()).hasKeyCodeModifierConflict(event.otherMapping()));

        ControllingEvents.SET_KEY_EVENT.register(event -> {
            ((InterKeyBinding) event.mapping()).setKeyModifierAndCode(multiKeyModifier.getActiveModifier(), event.key());
            return false;
        });

        ControllingEvents.IS_KEY_CODE_MODIFIER_EVENT
                .register(event -> multiKeyModifier.isKeyCodeModifier(event.key()));

        ControllingEvents.SET_TO_DEFAULT_EVENT.register(event -> {
            ((InterKeyBinding) event.mapping()).setToDefault();
            return false;
        });
    }
}