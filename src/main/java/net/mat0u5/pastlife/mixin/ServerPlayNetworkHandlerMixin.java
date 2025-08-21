package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

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
