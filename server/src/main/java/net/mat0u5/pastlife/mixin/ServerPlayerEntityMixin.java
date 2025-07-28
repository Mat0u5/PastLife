package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.WorldBorderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (!WorldBorderManager.initialized) {
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

    @Inject(method = "onKilled", at = @At("HEAD"))
    private void onKilled(Entity entity, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        //Death message
        String playerName = player.name;
        if (Main.livesManager != null) {
            String colorCode = Main.livesManager.getColorCode(player);
            if (colorCode != null) {
                playerName = colorCode + playerName + "§r";
            }
        }
        String deathMessage = playerName + " died";
        if (lastDamageSource != null) {
            if (lastDamageSource instanceof PlayerEntity) {
                PlayerEntity killer = (PlayerEntity) lastDamageSource;
                String killerName = killer.name;
                if (Main.livesManager != null) {
                    String killerColorCode = Main.livesManager.getColorCode(killer);
                    if (killerColorCode != null) {
                        killerName = killerColorCode + killerName + "§r";
                    }
                }
                deathMessage = playerName + " was slain by " + killerName;
            }
        }
        player.server.playerManager.sendPacket(new ChatMessagePacket(deathMessage));

        //Remove lives
        if (Main.livesManager != null) {
            Main.livesManager.addLives(player, -1);
        }
    }

    @Unique
    private Entity lastDamageSource;

    @Inject(method = "damage", at = @At("HEAD"))
    private void onKilled(Entity entity, int i, CallbackInfoReturnable<Boolean> cir) {
        lastDamageSource = entity;
    }
}
