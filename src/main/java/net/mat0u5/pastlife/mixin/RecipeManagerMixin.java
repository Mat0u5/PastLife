package net.mat0u5.pastlife.mixin;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("TAIL"))
    private void addCustomRecipes(Map<Identifier, JsonObject> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        RecipeManagerAccessor accessor = (RecipeManagerAccessor) this;
        Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes = accessor.getRecipes();

        Map<RecipeType<?>, Map<Identifier, Recipe<?>>> newRecipes = new HashMap<>(recipes);

        Map<Identifier, Recipe<?>> craftingRecipes = newRecipes.get(RecipeType.CRAFTING);
        if (craftingRecipes == null) return;

        Map<Identifier, Recipe<?>> newCraftingRecipes = new HashMap<>(craftingRecipes);

        Identifier recipeId = new Identifier("pastlife", "tnt_variation");

        DefaultedList<Ingredient> inputs = DefaultedList.ofSize(9, Ingredient.EMPTY);
        inputs.set(0, Ingredient.ofItems(Items.PAPER));
        inputs.set(1, Ingredient.ofItems(Items.SAND));
        inputs.set(2, Ingredient.ofItems(Items.PAPER));
        inputs.set(3, Ingredient.ofItems(Items.SAND));
        inputs.set(4, Ingredient.ofItems(Items.GUNPOWDER));
        inputs.set(5, Ingredient.ofItems(Items.SAND));
        inputs.set(6, Ingredient.ofItems(Items.PAPER));
        inputs.set(7, Ingredient.ofItems(Items.SAND));
        inputs.set(8, Ingredient.ofItems(Items.PAPER));

        ShapedRecipe recipe = new ShapedRecipe(
                recipeId,
                "",  // group
                3,   // width
                3,   // height
                inputs,
                new ItemStack(Items.TNT)
        );

        newCraftingRecipes.put(recipeId, recipe);

        newRecipes.put(RecipeType.CRAFTING, newCraftingRecipes);

        accessor.setRecipes(newRecipes);
    }
}