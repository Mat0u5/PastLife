package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.Main;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Unique
    private long ls$ticks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (ls$ticks % 5 == 0 && Main.livesManager != null) {
            Main.livesManager.scoreboardUpdate(player);
        }
        ls$ticks++;
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onKilled(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Remove a life
        if (Main.livesManager != null) {
            Main.livesManager.addLives(player, -1);
        }
    }
}
