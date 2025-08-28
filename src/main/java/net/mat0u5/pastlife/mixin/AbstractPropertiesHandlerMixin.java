package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPropertiesHandler.class)
public class AbstractPropertiesHandlerMixin {
    @Inject(method = "getString", at = @At("HEAD"), cancellable = true)
    private void getString(String key, String fallback, CallbackInfoReturnable<String> cir) {
        if (key.equalsIgnoreCase("resource-pack")) {
            cir.setReturnValue(Main.RESOURCEPACK_URL);
        }
        if (key.equalsIgnoreCase("resource-pack-sha1")) {
            cir.setReturnValue(Main.RESOURCEPACK_HASH);
        }
        if (key.equalsIgnoreCase("resource-pack-prompt")) {
            cir.setReturnValue("Past Life Resourcepack");
        }
    }
}
