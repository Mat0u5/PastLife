package net.mat0u5.pastlife.mixin.client;

import net.mat0u5.pastlife.client.interfaces.IPlayerEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
}
