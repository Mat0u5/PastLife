package net.mat0u5.pastlife.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
    private int clampEnchantmentLevel(int level) {
        return Math.min(level, 1);
    }
}
