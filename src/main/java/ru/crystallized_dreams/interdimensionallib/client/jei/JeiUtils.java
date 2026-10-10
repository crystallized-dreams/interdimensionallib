package ru.crystallized_dreams.interdimensionallib.client.jei;

import mezz.jei.api.recipe.RecipeType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class JeiUtils {
    private static final List<RecipeType<?>> RECIPE_TYPES = new ArrayList<>();

    public static <I extends RecipeInput, R extends net.minecraft.recipe.Recipe<I>> List<R>
                                    listRecipes(net.minecraft.recipe.RecipeType<R> recipeType) {
        if (MinecraftClient.getInstance().world == null) {
            return List.of();
        }
        return MinecraftClient.getInstance().world.getRecipeManager().listAllOfType(recipeType)
                .stream()
                .map(RecipeEntry::value)
                .toList();
    }

    public static <R> RecipeType<R> createRecipeType(Identifier recipeTypeIid, Class<? extends R> recipeClass) {
        RecipeType<R> recipeType = new RecipeType<>(recipeTypeIid, recipeClass);
        RECIPE_TYPES.add(recipeType);
        return recipeType;
    }
}
