package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityDamageSource.class)
public class EntityDamageSourceMixin {

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
    @Redirect(
            method = "getDeathMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getDisplayName()Ljava/lang/String;")
    )
    private String redirectPlayerName(Entity entity) {
        String originalName = entity.getDisplayName();
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (Main.livesManager == null) {
                return originalName;
            }
            String colorCode = Main.livesManager.getColorCode(playerEntity);
            if (colorCode == null) {
                return originalName;
            }
            return colorCode + originalName+"§r";
        }
        return originalName;
    }
}
