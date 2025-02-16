package me.coolaid.enhancedkeybinds.mixin;

import me.coolaid.enhancedkeybinds.event.InterKeyBinding;
import me.coolaid.enhancedkeybinds.multikey.multiKeyBindingOptions;
import me.coolaid.enhancedkeybinds.multikey.multiKeyConflictContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;


@Mixin(GameOptions.class)
public abstract class MixinGameOptions {
    @Shadow
    @Final
    public KeyBinding[] allKeys;

    @Shadow
    @Final
    public KeyBinding forwardKey;

    @Shadow
    @Final
    public KeyBinding backKey;

    @Shadow
    @Final
    public KeyBinding leftKey;

    @Shadow
    @Final
    public KeyBinding rightKey;

    @Shadow
    @Final
    public KeyBinding jumpKey;

    @Shadow
    @Final
    public KeyBinding sneakKey;

    @Shadow
    @Final
    public KeyBinding sprintKey;

    @Shadow
    @Final
    public KeyBinding attackKey;

    @Shadow
    @Final
    public KeyBinding chatKey;

    @Shadow
    @Final
    public KeyBinding playerListKey;

    @Shadow
    @Final
    public KeyBinding commandKey;

    @Shadow
    @Final
    public KeyBinding togglePerspectiveKey;

    @Shadow
    @Final
    public KeyBinding smoothCameraKey;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$init(MinecraftClient client, File optionsFile, CallbackInfo ci) {
        multiKeyBindingOptions.read(this.allKeys);
        setKeybindProperties();
    }

    private void setKeybindProperties() {
        final KeyBinding[] keyBindings = {forwardKey, backKey, leftKey, rightKey, jumpKey, sneakKey,
                sprintKey, attackKey, chatKey, playerListKey, commandKey, togglePerspectiveKey, smoothCameraKey};
        for (final KeyBinding binding : keyBindings)
            ((InterKeyBinding) binding).setKeyConflictContext(multiKeyConflictContext.IN_GAME);
    }

    @Inject(method = "write", at = @At("HEAD"))
    private void inject$write(CallbackInfo ci) {
        try {
            multiKeyBindingOptions.write(this.allKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}