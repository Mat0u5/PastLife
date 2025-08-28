package net.mat0u5.pastlife.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
    private static int clampEnchantmentLevel(int original) {
        return Math.min(original, 1);
    }

    @ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true, index = 1)
    private static ItemEnchantmentsComponent clampEnchantmentLevel(ItemEnchantmentsComponent value) {
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        for (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<RegistryEntry<Enchantment>> enchant : value.getEnchantmentEntries()) {
            builder.add(enchant.getKey(), Math.min(1, enchant.getIntValue()));
        }
        return builder.build();
    }
}
