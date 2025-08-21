package net.mat0u5.pastlife.mixin.client;

import net.mat0u5.pastlife.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private static void onModInit(CallbackInfoReturnable<Boolean> cir) {
        Main.init(MinecraftServer.getInstance());
    }
}
