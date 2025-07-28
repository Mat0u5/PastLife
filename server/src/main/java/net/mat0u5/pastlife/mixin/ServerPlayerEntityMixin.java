package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.InventoryStorage;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.mat0u5.pastlife.utils.WorldBorderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "onKilled", at = @At("HEAD"))
    private void onKilled(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Remove a life
        if (Main.livesManager != null) {
            Main.livesManager.addLives(player, -1);
        }
        if (Main.livesManager != null && Main.livesManager.getLives(player) > 0) {
            InventoryStorage.storeInventory(player.name, player.inventory);
        }
    }

    @Redirect(
            method = "onKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V")
    )
    private void keepInventory(PlayerInventory instance) {
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void restoreInventoryOnSpawn(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        String playerName = player.name;
        if (InventoryStorage.hasStoredInventory(playerName) && (Main.livesManager != null && Main.livesManager.getLives(player) > 0)) {
            InventoryStorage.restoreInventory(playerName, player.inventory);
        }
    }
}
