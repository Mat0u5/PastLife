package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.WorldBorderManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (!WorldBorderManager.initialized || !player.world.dimension.hasWorldSpawn()) {
            return;
        }
        double centerX = WorldBorderManager.centerX;
        double centerZ = WorldBorderManager.centerZ;

        if (WorldBorderManager.isOutsideBorder(player.x, player.z)) {
            double halfSize = WorldBorderManager.borderSize / 2.0;

            double posX = player.x;
            double posY = player.y;
            double posZ = player.z;
            
            double newX = posX;
            double newZ = posZ;

            if (Math.abs(posX - centerX) > halfSize) {
                newX = centerX + (halfSize * Math.signum(posX - centerX));
            }
            if (Math.abs(posZ - centerZ) > halfSize) {
                newZ = centerZ + (halfSize * Math.signum(posZ - centerZ));
            }

            if (newX != posX || newZ != posZ) {
                player.networkHandler.teleport(newX, posY, newZ, player.yaw, player.pitch);
                player.velocityX = 0;
                player.velocityZ = 0;
            }
        }
    }

    @Inject(method = "onKilled", at = @At("TAIL"))
    private void onKilled(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Remove a life
        if (Main.livesManager != null) {
            Main.livesManager.addLives(player, -1);
        }
    }
}
