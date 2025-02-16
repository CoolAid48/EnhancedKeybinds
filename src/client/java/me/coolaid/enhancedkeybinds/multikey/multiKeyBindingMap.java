package me.coolaid.enhancedkeybinds.multikey;

import me.coolaid.enhancedkeybinds.event.InterKeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class multiKeyBindingMap {
    private static final EnumMap<multiKeyModifier, Map<InputUtil.Key, Collection<KeyBinding>>> map = new EnumMap<>(multiKeyModifier.class);

    static {
        for (multiKeyModifier modifier : multiKeyModifier.values()) map.put(modifier, new HashMap<>());
    }

    @Nullable
    public KeyBinding lookupActive(InputUtil.Key keyCode) {
        final multiKeyModifier activeModifier = multiKeyModifier.getActiveModifier();
        if (!activeModifier.matches(keyCode)) {
            KeyBinding binding = getBinding(keyCode, activeModifier);
            if (binding != null) {
                return binding;
            }
        }
        return getBinding(keyCode, multiKeyModifier.NONE);
    }

    public Set<KeyBinding> lookupActives(InputUtil.Key keyCode) {
        final multiKeyModifier activeModifier = multiKeyModifier.getActiveModifier();
        if (!activeModifier.matches(keyCode)) {
            Set<KeyBinding> bindings = getBindings(keyCode, activeModifier);
            if (!bindings.isEmpty()) return bindings;
        }
        return getBindings(keyCode, multiKeyModifier.NONE);
    }

    @Nullable
    private KeyBinding getBinding(InputUtil.Key keyCode, multiKeyModifier keyModifier) {
        Collection<KeyBinding> bindings = map.get(keyModifier).get(keyCode);
        if (bindings == null) return null;
        for (KeyBinding binding : bindings) if (((InterKeyBinding) binding).isActiveAndMatches(keyCode)) return binding;
        return null;
    }

    private Set<KeyBinding> getBindings(InputUtil.Key keyCode, multiKeyModifier keyModifier) {
        Collection<KeyBinding> bindings = map.get(keyModifier).get(keyCode);
        final Set<KeyBinding> toReturn = new HashSet<>();
        if (bindings == null) return toReturn;
        for (KeyBinding binding : bindings)
            if (((InterKeyBinding) binding).isActiveAndMatches(keyCode)) toReturn.add(binding);
        return toReturn;
    }

    public List<KeyBinding> lookupAll(InputUtil.Key keyCode) {
        List<KeyBinding> matchingBindings = new ArrayList<KeyBinding>();
        for (Map<InputUtil.Key, Collection<KeyBinding>> bindingsMap : map.values()) {
            Collection<KeyBinding> bindings = bindingsMap.get(keyCode);
            if (bindings != null) {
                matchingBindings.addAll(bindings);
            }
        }
        return matchingBindings;
    }

    public void addKey(InputUtil.Key keyCode, KeyBinding keyBinding) {
        multiKeyModifier keyModifier = ((InterKeyBinding) keyBinding).getKeyModifier();
        Map<InputUtil.Key, Collection<KeyBinding>> bindingsMap = map.get(keyModifier);
        Collection<KeyBinding> bindingsForKey = bindingsMap.computeIfAbsent(keyCode, k -> new ArrayList<>());
        bindingsForKey.add(keyBinding);
    }

    public void removeKey(KeyBinding keyBinding) {
        final InterKeyBinding extended = (InterKeyBinding) keyBinding;
        multiKeyModifier keyModifier = extended.getKeyModifier();
        InputUtil.Key keyCode = extended.getKey();
        Map<InputUtil.Key, Collection<KeyBinding>> bindingsMap = map.get(keyModifier);
        Collection<KeyBinding> bindingsForKey = bindingsMap.get(keyCode);
        if (bindingsForKey != null) {
            bindingsForKey.remove(keyBinding);
            if (bindingsForKey.isEmpty()) {
                bindingsMap.remove(keyCode);
            }
        }
    }

    public void clearMap() {
        for (Map<InputUtil.Key, Collection<KeyBinding>> bindings : map.values()) {
            bindings.clear();
        }
    }
}