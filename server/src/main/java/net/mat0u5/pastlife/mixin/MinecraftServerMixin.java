package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void onModInit(CallbackInfoReturnable<Boolean> cir) {
        Main.init();
    }
}
