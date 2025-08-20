package net.mat0u5.pastlife.mixin.client;

import net.mat0u5.pastlife.lives.ClientLivesManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Redirect(
            method = "renderNameTag(Lnet/minecraft/entity/living/player/PlayerEntity;DDD)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/living/player/PlayerEntity;name:Ljava/lang/String;")
    )
    private String redirectPlayerName(PlayerEntity playerEntity) {
        String originalName = playerEntity.name;
        String colorCode = ClientLivesManager.getColorCode(playerEntity);
        if (colorCode == null) {
            return originalName;
        }
        return colorCode + originalName;
    }
}
