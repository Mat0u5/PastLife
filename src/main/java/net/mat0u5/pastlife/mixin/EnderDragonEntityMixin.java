package net.mat0u5.pastlife.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin {

    @Redirect(method = "method_6824", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean dropDiamonds(World world, Entity entity) {
        Entity diamond = new ItemEntity(world, entity.x, entity.y, entity.z, new ItemStack(Items.DIAMOND, 1));
        world.spawnEntity(diamond);
        return false;
    }
}
