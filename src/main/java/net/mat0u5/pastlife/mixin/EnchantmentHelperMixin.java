package net.mat0u5.pastlife.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyVariable(method = "setEnchantments", at = @At("HEAD"), argsOnly = true)
    private static Map clampEnchantmentLevel(Map enchantments) {
        // WHY DONT THE MAPPINGS HAVE THE FRICKEN MAP KEY AND VALUE TYPE RAAAAAHHHH
        for(Object key : enchantments.keySet()) {
            Object value = enchantments.get(key);
            if (value instanceof Integer) {
                enchantments.put(key, Math.min(1, (Integer) value));
            }
        }
        return enchantments;
    }
}
