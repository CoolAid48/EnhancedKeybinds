package me.coolaid.enhancedkeybinds.mixin;

import me.coolaid.enhancedkeybinds.event.InterKeyBinding;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public abstract class MixinKeyBindingEntry {
    @Mutable
    @Shadow
    @Final
    private ButtonWidget resetButton;

    @Shadow(aliases = {"this$0", "field_2742"})
    private ControlsListWidget outerThis;

    @Shadow
    @Final
    private Text bindingName;

    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/option/ControlsListWidget;Lnet/minecraft/client/option/KeyBinding;Lnet/minecraft/text/Text;)V", at = @At("RETURN"))
    private void inject$init(ControlsListWidget controlsListWidget, KeyBinding binding, Text text, CallbackInfo ci) {
        resetButton = ButtonWidget.builder(Text.translatable("controls.reset"), button -> {
                    // Reset to default, allowing modifier-only keys if supported by InterKeyBinding
                    ((InterKeyBinding) binding).setToDefault();
                    binding.setBoundKey(binding.getDefaultKey()); // Supports modifier keys if default is a modifier
                    ((AccessorScreen) ((AccessorControlsListWidget) outerThis).getParent()).getClient().options.write();
                    outerThis.update();
                })
                .dimensions(0, 0, 50, 20)
                .narrationSupplier(textSupplier -> Text.translatable("narrator.controls.reset", bindingName))
                .build();
    }
}