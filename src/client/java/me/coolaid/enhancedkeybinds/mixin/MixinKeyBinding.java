package me.coolaid.enhancedkeybinds.mixin;

import me.coolaid.enhancedkeybinds.EnhancedKeybinds;
import me.coolaid.enhancedkeybinds.event.InterKeyBinding;
import me.coolaid.enhancedkeybinds.event.InterKeyConflictContext;
import me.coolaid.enhancedkeybinds.multikey.multiKeyBindingMap;
import me.coolaid.enhancedkeybinds.multikey.multiKeyConflictContext;
import me.coolaid.enhancedkeybinds.multikey.multiKeyModifier;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding implements InterKeyBinding {
    @Shadow
    private InputUtil.Key boundKey;

    @Shadow
    private int timesPressed;
    @Shadow
    private boolean pressed;
    @Shadow
    @Final
    private InputUtil.Key defaultKey;
    @Shadow
    @Final
    private static Map<String, KeyBinding> KEYS_BY_ID;
    private static final multiKeyBindingMap MAP = new multiKeyBindingMap();

    private multiKeyModifier keyModifierDefault;
    private multiKeyModifier keyModifier;
    private InterKeyConflictContext keyConflictContext;

    @Override
    public InputUtil.Key getKey() {
        return boundKey;
    }

    @Override
    public InterKeyConflictContext getKeyConflictContext() {
        return keyConflictContext == null ? multiKeyConflictContext.UNIVERSAL : keyConflictContext;
    }

    @Override
    public multiKeyModifier getKeyModifierDefault() {
        return keyModifierDefault == null ? multiKeyModifier.NONE : keyModifierDefault;
    }

    @Override
    public multiKeyModifier getKeyModifier() {
        return keyModifier == null ? multiKeyModifier.NONE : keyModifier;
    }

    @Override
    public void setKeyConflictContext(InterKeyConflictContext keyConflictContext) {
        this.keyConflictContext = keyConflictContext;
    }

    @Override
    public void setKeyModifierAndCode(multiKeyModifier keyModifier, InputUtil.Key keyCode) {
        this.boundKey = keyCode;
        if (keyModifier.matches(keyCode))
            keyModifier = multiKeyModifier.NONE;
        MAP.removeKey((KeyBinding) (Object) this);
        this.keyModifier = keyModifier;
        MAP.addKey(keyCode, (KeyBinding) (Object) this);
    }

    @Override
    public void press() {
        ++timesPressed;
    }

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At("RETURN"))
    public void inject$init(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo ci) {
        MAP.addKey(this.boundKey, (KeyBinding) (Object) this);
    }

    @Inject(method = "onKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void inject$onKeyPressed(InputUtil.Key key, CallbackInfo ci) {
        ci.cancel();
        if (EnhancedKeybinds.nonConflictKeys()) {
            MAP.lookupActives(key).forEach(k -> ((InterKeyBinding) k).press());
            return;
        }
        KeyBinding keyBinding = MAP.lookupActive(key);
        if (keyBinding == null) return;
        ((InterKeyBinding) keyBinding).press();
    }

    @Inject(method = "setKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void inject$setKeyPressed(InputUtil.Key key, boolean pressed, CallbackInfo ci) {
        ci.cancel();
        if (EnhancedKeybinds.nonConflictKeys()) {
            MAP.lookupActives(key).forEach(k -> k.setPressed(pressed));
            return;
        }
        KeyBinding keyBinding = MAP.lookupActive(key);
        if (keyBinding == null) return;
        keyBinding.setPressed(pressed);
    }

    @Inject(method = "updateKeysByCode", at = @At("HEAD"), cancellable = true)
    private static void updateKeysByCode(CallbackInfo ci) {
        ci.cancel();
        MAP.clearMap();
        for (KeyBinding keybinding : KEYS_BY_ID.values()) MAP.addKey(((InterKeyBinding) keybinding).getKey(), keybinding);
    }

    @Inject(method = "isPressed", at = @At("RETURN"), cancellable = true)
    private void inject$isPressed(CallbackInfoReturnable<Boolean> cir) {
        if (((KeyBinding) (Object) this) instanceof StickyKeyBinding) {
            final StickyKeyBinding sticky = (StickyKeyBinding) (Object) this;
            cir.setReturnValue(pressed && (isConflictContextAndModifierActive() || ((AccessorStickyKeyBinding) sticky).getToggleGetter().getAsBoolean()));
            return;
        }
        cir.setReturnValue(pressed && isConflictContextAndModifierActive());
    }

    @Inject(method = "isDefault", at = @At("RETURN"), cancellable = true)
    private void inject$isDefault(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(boundKey.equals(defaultKey) && getKeyModifier() == getKeyModifierDefault());
    }

    @Inject(method = "equals", at = @At("HEAD"), cancellable = true)
    private void inject$equals(KeyBinding other, CallbackInfoReturnable<Boolean> cir) {
        final InterKeyBinding extended = (InterKeyBinding) other;
        if (!getKeyConflictContext().conflicts(extended.getKeyConflictContext()) && !extended.getKeyConflictContext().conflicts(getKeyConflictContext()))
            return;
        multiKeyModifier keyModifier = getKeyModifier();
        multiKeyModifier otherKeyModifier = extended.getKeyModifier();
        if (keyModifier.matches(extended.getKey()) || otherKeyModifier.matches(getKey())) {
            cir.setReturnValue(true);
        } else if (getKey().equals(extended.getKey())) {
            // IN_GAME key contexts have a conflict when at least one modifier is NONE. // Other key contexts do not have this limitation.
            // For example: If you hold shift to crouch, you can still press E to open your inventory... so a Shift+E hotkey is in conflict with E.
            cir.setReturnValue(keyModifier == otherKeyModifier ||
                    (getKeyConflictContext().conflicts(multiKeyConflictContext.IN_GAME) &&
                            (keyModifier == multiKeyModifier.NONE || otherKeyModifier == multiKeyModifier.NONE)));
        }
    }

    @Inject(method = "getBoundKeyLocalizedText", at = @At("RETURN"), cancellable = true)
    private void inject$getBoundKeyLocalizedText(CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(getKeyModifier().getCombinedName(boundKey, () -> this.boundKey.getLocalizedText()));
    }
}