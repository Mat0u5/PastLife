package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileDamageSource.class)
public class ProjectileDamageSourceMixin {

    @Redirect(
            method = "getDeathMessage",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/living/player/PlayerEntity;name:Ljava/lang/String;")
    )
    private String redirectPlayerName(PlayerEntity playerEntity) {
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
