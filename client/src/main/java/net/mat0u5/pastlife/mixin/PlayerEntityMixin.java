package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.interfaces.IPlayerEntity;
import net.mat0u5.pastlife.utils.WorldBorderManager;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IPlayerEntity {

    @Unique
    private String playerUUID;

    @Override
    public void setUUID(String uuid) {
        playerUUID = uuid;
    }

    @Override
    public String getUUID() {
        return playerUUID;
    }

    @Unique
    private String playerName;

    @Override
    public void setName(String name) {
        playerName = name;
    }

    @Override
    public String getName() {
        return playerName;
    }

    @Inject(method = "registerCloak", at = @At("HEAD"), cancellable = true)
    public void registerCloak(CallbackInfo ci) {
        PlayerEntity entity = (PlayerEntity) (Object) this;
        if (playerUUID != null) {
            entity.cape = "https://crafatar.com/capes/"+playerUUID+".png";
            entity.cloak = entity.cape;
            ci.cancel();
        }
    }


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
                player.setVelocity(0, player.velocityY, 0);
            }
        }
    }
}
