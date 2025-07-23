package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.utils.KeybindUtils;
import net.mat0u5.pastlife.utils.RenderUtils;
import net.minecraft.client.gui.GuiElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiElement.class)
public class GuiElementMixin {
    @Inject(method = "drawTexture", at = @At(value = "HEAD"), cancellable = true)
    private void redirectDrawTexture(int x, int y, int u, int v, int width, int height, CallbackInfo ci) {
        //Cancel crosshair rendering in third person
        if (RenderUtils.loadedTexture.equals("/gui/icons.png") && KeybindUtils.thirdPerson()) {
            if (u == 0 && v == 0 && width == 16 && height == 16) {
                // Called from GameGui.render()
                ci.cancel();
            }
        }
    }
}
