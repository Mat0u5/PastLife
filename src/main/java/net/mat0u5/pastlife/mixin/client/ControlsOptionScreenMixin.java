package net.mat0u5.pastlife.mixin.client;

import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ControlsOptionsScreen.class)
public class ControlsOptionScreenMixin {
    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V"), index = 2)
    private int renderTitle(int par1) {
        return par1+20;
    }
}
