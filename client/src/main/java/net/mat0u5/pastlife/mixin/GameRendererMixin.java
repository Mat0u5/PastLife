package net.mat0u5.pastlife.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mat0u5.pastlife.interfaces.IKeybinds;
import net.mat0u5.pastlife.utils.KeybindUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
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

    @WrapOperation(
            method = "transformCamera", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z")
    )
    private boolean customF5(int key, Operation<Boolean> original) {
        return KeybindUtils.reversedF5();
    }
}
