package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.boogeyman.BoogeymanCommand;
import net.mat0u5.pastlife.lives.LivesCommand;
import net.mat0u5.pastlife.secretsociety.SecretSocietyCommand;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    private MinecraftServer server;

    @Inject(method = "runCommand", at = @At("HEAD"), cancellable = true)
    private void injectCustomCommand(String command, CallbackInfo ci) {
        /*
        ServerPlayNetworkHandler networkHandler = (ServerPlayNetworkHandler) (Object) this;
        command = command.toLowerCase().trim();
        if (command.startsWith("/lives")) {
            LivesCommand.handleCommand(server, networkHandler.player, command, networkHandler);
            ci.cancel();
        }
        if (command.startsWith("/boogeyman")) {
            BoogeymanCommand.handleCommand(server, networkHandler.player, command, networkHandler);
            ci.cancel();
        }
        if (command.startsWith("/society") || command.startsWith("/initiate")) {
            SecretSocietyCommand.handleCommand(server, networkHandler.player, command, networkHandler);
            ci.cancel();
        }*/
    }

    @Redirect(
            method = "handleChatMessage",
            at = @At(value = "FIELD", target = "Lnet/minecraft/server/entity/living/player/ServerPlayerEntity;name:Ljava/lang/String;")
    )
    private String redirectPlayerName(ServerPlayerEntity playerEntity) {
        String originalName = playerEntity.name;
        if (Main.livesManager == null) {
            return originalName;
        }
        String colorCode = Main.livesManager.getColorCode(playerEntity);
        if (colorCode == null) {
            return originalName;
        }
        return colorCode + originalName+"§r";
    }
}
