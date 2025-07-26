package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.lives.LivesCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    private MinecraftServer server;
    @Shadow
    private ServerPlayerEntity player;

    @Inject(method = "runCommand", at = @At("HEAD"), cancellable = true)
    private void injectCustomCommand(String command, CallbackInfo ci) {
        ServerPlayNetworkHandler networkHandler = (ServerPlayNetworkHandler) (Object) this;
        command = command.toLowerCase().trim();
        if (command.startsWith("/lives")) {
            LivesCommand.handleCommand(server, player, command, networkHandler);
            ci.cancel();
        }
    }
}
