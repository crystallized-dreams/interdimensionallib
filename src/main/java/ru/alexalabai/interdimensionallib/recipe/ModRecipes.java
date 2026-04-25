package ru.alexalabai.interdimensionallib.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;
import ru.alexalabai.interdimensionallib.recipe.all.GrindstonePolishRecipe;

public class ModRecipes {
    public static final RecipeSerializer<GrindstonePolishRecipe> GRINDSTONE_POLISH_SERIALIZER=
            RecipeSerializer.register("interdimensionallib:grindstone_polish", GrindstonePolishRecipe.GrindstonePolishRecipeSerializer.INSTANCE);

    public static final RecipeType<GrindstonePolishRecipe> GRINDSTONE_POLISH_RECIPE=register( "grindstone_polish");

    public static void regAll() {
        InterdimensionalLib.LOGGER.info("[KABAN]: Registered recipes and serializers");
    }

    static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(InterdimensionalLib.MOD_ID,id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }
}
