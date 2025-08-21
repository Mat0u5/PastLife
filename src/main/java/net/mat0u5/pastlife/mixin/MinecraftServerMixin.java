package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.LivesUpdatePacket;
import net.mat0u5.pastlife.secretsociety.SecretSociety;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.mat0u5.pastlife.utils.TaskScheduler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow
    int ticks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onModInit(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        //PlayerUtils.doAction(server); //TODO REMOVE

        TaskScheduler.onTick();
        SecretSociety.tick(server);

        if (Main.livesManager == null) {
            return;
        }
        if (ticks % 10 == 0) {
            for(int i = 0; i < server.getPlayerManager().players.size(); ++i) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)server.getPlayerManager().players.get(i);
                int lives = Main.livesManager.getLives(serverPlayerEntity);
                PlayerUtils.sendPacketToAllPlayers(new LivesUpdatePacket(serverPlayerEntity.name, lives));
            }
        }
    }
}
