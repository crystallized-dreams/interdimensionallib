package ru.crystallized_dreams.interdimensionallib.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.recipe.all.GrindstonePolishRecipe;

public class INTERDIM_Recipes {
    public static final RecipeSerializer<GrindstonePolishRecipe> GRINDSTONE_POLISH_SERIALIZER=
            RecipeSerializer.register("interdimensionallib:grindstone_polish", GrindstonePolishRecipe.GrindstonePolishRecipeSerializer.INSTANCE);

    public static final RecipeType<GrindstonePolishRecipe> GRINDSTONE_POLISH_RECIPE=register( "grindstone_polish");

    public static void regAll() {
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered recipes and serializers");
    }

    static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(InterdimensionalLib.MOD_ID,id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }
}
