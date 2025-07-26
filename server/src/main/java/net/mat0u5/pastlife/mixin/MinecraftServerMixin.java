package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.lives.LivesUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow
    int ticks = 0;

    @Inject(method = "init", at = @At("TAIL"))
    private void onModInit(CallbackInfoReturnable<Boolean> cir) {
        Main.init();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onModInit(CallbackInfo ci) {
        if (Main.livesManager == null) {
            return;
        }
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (ticks % 10 == 0) {
            for(int i = 0; i < server.playerManager.players.size(); ++i) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)server.playerManager.players.get(i);
                int lives = Main.livesManager.getLives(serverPlayerEntity);
                server.playerManager.sendPacket(new LivesUpdatePacket(serverPlayerEntity.name, lives));
            }
        }
    }
}
