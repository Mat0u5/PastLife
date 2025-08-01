package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.LivesUpdatePacket;
import net.mat0u5.pastlife.packets.SoundEventPacket;
import net.mat0u5.pastlife.packets.WorldBorderUpdatePacket;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.mat0u5.pastlife.utils.WorldBorderManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
        MinecraftServer server = (MinecraftServer) (Object) this;
        Main.init(server);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onModInit(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        TaskScheduler.onTick();

        if (!WorldBorderManager.initialized) {
            for (ServerWorld world : server.worlds) {
                if (world.dimension.hasWorldSpawn()) {
                    WorldBorderManager.init(400, world.getSpawnPoint().x, world.getSpawnPoint().z);
                }
            }
        }

        if (Main.livesManager == null) {
            return;
        }
        if (ticks % 10 == 0) {
            for(int i = 0; i < server.playerManager.players.size(); ++i) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)server.playerManager.players.get(i);
                int lives = Main.livesManager.getLives(serverPlayerEntity);
                server.playerManager.sendPacket(new LivesUpdatePacket(serverPlayerEntity.name, lives));
            }
            if (WorldBorderManager.initialized) {
                server.playerManager.sendPacket(new WorldBorderUpdatePacket(WorldBorderManager.centerX, WorldBorderManager.centerZ, WorldBorderManager.borderSize));
            }
        }
    }
}
