package net.mat0u5.pastlife.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyVariable(method = "setEnchantments", at = @At("HEAD"), argsOnly = true)
    private static Map<Enchantment, Integer> clampEnchantmentLevel(Map<Enchantment, Integer> enchantments) {
        for (Enchantment key : enchantments.keySet()) {
            enchantments.put(key, Math.min(1, enchantments.get(key)));
        }
        return enchantments;
    }
}
