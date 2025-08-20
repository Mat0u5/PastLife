package net.mat0u5.pastlife.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.mat0u5.pastlife.interfaces.IKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    private Minecraft minecraft;

    private static final float ZOOM_FACTOR = 0.4f;

    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private float modifyFov(float fov) {
        GameOptions options = minecraft.options;
        if (minecraft.screen != null) {
            return fov;
        }
        if (options instanceof IKeybinds) {
            IKeybinds customOptions = (IKeybinds) options;
            if (!Keyboard.isKeyDown(customOptions.zoomKey().keyCode)) {
                return fov;
            }
        }
        return fov * ZOOM_FACTOR;
    }
}
