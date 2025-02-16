package me.coolaid.enhancedkeybinds.mixin;

import me.coolaid.enhancedkeybinds.event.InterKeyBinding;
import me.coolaid.enhancedkeybinds.multikey.multiKeyModifier;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeybindsScreen.class)
public abstract class MixinKeybindsScreen extends GameOptionsScreen {

    @Shadow
    @Nullable
    public KeyBinding selectedKeyBinding;

    @Shadow
    public long lastKeyCodeUpdateTime;

    @Shadow
    private ControlsListWidget controlsList;

    public MixinKeybindsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void inject$keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (selectedKeyBinding != null) {
            final InterKeyBinding extended = (InterKeyBinding) selectedKeyBinding;
            if (keyCode == 256) {
                extended.setKeyModifierAndCode(multiKeyModifier.getActiveModifier(), InputUtil.UNKNOWN_KEY);
                selectedKeyBinding.setBoundKey(InputUtil.UNKNOWN_KEY);
            } else {
                InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
                extended.setKeyModifierAndCode(multiKeyModifier.getActiveModifier(), key);
                selectedKeyBinding.setBoundKey(key);
            } // Save changes
            if (!multiKeyModifier.isKeyCodeModifier(((InterKeyBinding) selectedKeyBinding).getKey())) selectedKeyBinding = null;
            this.lastKeyCodeUpdateTime = Util.getMeasuringTimeMs();
            this.gameOptions.write();
            this.controlsList.update();
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(super.keyPressed(keyCode, scanCode, modifiers));
    }

    @Redirect(method = "initFooter", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;",
            ordinal = 0
    ))
    private ButtonWidget redirect$initFooter(ButtonWidget.Builder instance) {
        return ButtonWidget.builder(Text.translatable("controls.resetAll"), button -> {
            for (KeyBinding keyBinding : this.gameOptions.allKeys) {
                ((InterKeyBinding) keyBinding).setToDefault();
            }

            this.controlsList.update();
        }).build();

    }
}