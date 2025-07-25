package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.utils.WorldBorderManager;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!WorldBorderManager.setUp) {
            WorldBorderManager.setCenter(player.world.spawnpointX, player.world.spawnpointZ);
        }

        if (WorldBorderManager.isOutsideBorder(player.x, player.z)) {
            double halfSize = WorldBorderManager.getSize() / 2.0;
            double centerX = WorldBorderManager.getCenterX();
            double centerZ = WorldBorderManager.getCenterZ();

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
                player.setPosition(newX, posY, newZ);
                player.velocityX = 0;
                player.velocityZ = 0;
            }
        }
    }
}
