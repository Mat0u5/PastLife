package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.interfaces.IPlayerEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IPlayerEntity {

    @Unique
    private String playerUUID;

    @Override
    public void setUUID(String uuid) {
        playerUUID = uuid;
    }

    @Override
    public String getUUID() {
        return playerUUID;
    }

    @Unique
    private String playerName;

    @Override
    public void setName(String name) {
        playerName = name;
    }

    @Override
    public String getName() {
        return playerName;
    }

    @Inject(method = "registerCloak", at = @At("HEAD"), cancellable = true)
    public void registerCloak(CallbackInfo ci) {
        PlayerEntity entity = (PlayerEntity) (Object) this;
        if (playerUUID != null) {
            entity.cape = "https://crafatar.com/capes/"+playerUUID+".png";
            entity.cloak = entity.cape;
            ci.cancel();
        }
    }
}
