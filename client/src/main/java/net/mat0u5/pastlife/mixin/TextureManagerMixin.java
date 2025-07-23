package net.mat0u5.pastlife.mixin;


import net.mat0u5.pastlife.utils.RenderUtils;
import net.minecraft.client.render.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Inject(method = "load", at = @At(value = "HEAD"))
    private void tickHead(String path, CallbackInfoReturnable<Integer> cir) {
        RenderUtils.loadedTexture = path;
    }
}
