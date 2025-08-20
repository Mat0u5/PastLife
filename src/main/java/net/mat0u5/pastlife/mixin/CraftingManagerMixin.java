package net.mat0u5.pastlife.mixin;

import net.minecraft.block.Block;
import net.minecraft.crafting.CraftingManager;
import net.minecraft.crafting.recipe.Recipe;
import net.minecraft.crafting.recipe.ShapedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CraftingManager.class)
public class CraftingManagerMixin {
    @Shadow
    private List<Recipe> recipes;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addRecipe(CallbackInfo ci) {
        ItemStack[] itemStacks = new ItemStack[9];
        ItemStack paper = new ItemStack(Item.PAPER);
        ItemStack sand = new ItemStack(Block.SAND);
        ItemStack gunpowder = new ItemStack(Item.GUNPOWDER);

        itemStacks[0] = paper;
        itemStacks[1] = sand;
        itemStacks[2] = paper;
        itemStacks[3] = sand;
        itemStacks[4] = gunpowder;
        itemStacks[5] = sand;
        itemStacks[6] = paper;
        itemStacks[7] = sand;
        itemStacks[8] = paper;

        recipes.add(new ShapedRecipe(3, 3, itemStacks, new ItemStack(Block.TNT, 1)));
    }
}
