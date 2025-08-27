package net.mat0u5.pastlife.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
    private static int clampEnchantmentLevel(int original) {
        return Math.min(original, 1);
    }

    @ModifyReturnValue(method = "getEnchantments(Lnet/minecraft/item/ItemStack;)Ljava/util/Map;", at = @At(value = "RETURN"))
    private static Map<Enchantment, Integer> clampEnchantmentLevel2(Map<Enchantment, Integer> original) {
        original.replaceAll((key, value) -> Math.min(1, value));
        return original;
    }

    @ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
    private static Map<Enchantment, Integer> clampEnchantmentLevel(Map<Enchantment, Integer> enchantments) {
        enchantments.replaceAll((key, value) -> Math.min(1, value));
        return enchantments;
    }
    //TODO maybe needs more
}
