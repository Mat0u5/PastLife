package net.mat0u5.pastlife.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.interfaces.IKeybinds;
import net.mat0u5.pastlife.utils.KeybindUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.living.LivingEntity;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Redirect(
            method = "transformCamera",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/living/LivingEntity;pitch:F", ordinal = 1)
    )
    private float customF5(LivingEntity entity) {
        if (KeybindUtils.reversedF5()) {
            return entity.pitch + 180;
        }
        return entity.pitch;
    }

    @Redirect(
            method = "transformCamera",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 6)
    )
    private void customF5Rotate(float angle, float x, float y, float z) {
        if (KeybindUtils.reversedF5()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }
        GL11.glRotatef(angle, x, y, z);
    }
}
