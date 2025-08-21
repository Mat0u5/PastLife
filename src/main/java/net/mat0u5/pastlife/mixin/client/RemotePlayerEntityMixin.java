package net.mat0u5.pastlife.mixin.client;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.client.interfaces.IPlayerEntity;
import net.mat0u5.pastlife.client.utils.ClientPlayerUtils;
import net.minecraft.client.entity.living.player.RemoteClientPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemoteClientPlayerEntity.class)
public class RemotePlayerEntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifySkinField(World world, String name, CallbackInfo ci) {
        RemoteClientPlayerEntity entity = (RemoteClientPlayerEntity) (Object) (this);
        IPlayerEntity accessor = (IPlayerEntity) entity;
        accessor.setName(name);
        ClientPlayerUtils.loadUUIDWithAllMethods(accessor);
        String uuid = accessor.getUUID();
        if (uuid != null) {
            entity.skin = "https://crafatar.com/skins/"+uuid+".png";
            Main.log("Override texture " + entity.skin);
            entity.cape = "https://crafatar.com/capes/"+uuid+".png";
            entity.cloak = entity.cape;
            Main.log("Override cape " + entity.cape);
        }
    }
}
