package net.mat0u5.pastlife.mixin.client;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.interfaces.IPlayerEntity;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.entity.living.player.InputPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InputPlayerEntity.class)
public class InputPlayerEntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifySkinField(Minecraft minecraft, World world, Session session, int dimensionId, CallbackInfo ci) {
        InputPlayerEntity entity = (InputPlayerEntity) (Object) (this);
        IPlayerEntity accessor = (IPlayerEntity) entity;
        accessor.setName(session.username);
        PlayerUtils.loadUUIDWithAllMethods(accessor);
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
