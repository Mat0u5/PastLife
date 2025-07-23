package net.mat0u5.pastlife.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mat0u5.pastlife.utils.KeybindUtils;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @WrapOperation(
            method = "transformCamera", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z")
    )
    private boolean customF5(int key, Operation<Boolean> original) {
        return KeybindUtils.reversedF5();
    }
}
