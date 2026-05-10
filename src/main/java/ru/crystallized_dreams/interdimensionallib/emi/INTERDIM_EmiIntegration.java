package ru.crystallized_dreams.interdimensionallib.emi;

import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.recipe.RecipeManager;
import ru.crystallized_dreams.interdimensionallib.emi.recipe.GrindstonePolishEmiRecipe;
import ru.crystallized_dreams.interdimensionallib.recipe.INTERDIM_Recipes;

public class INTERDIM_EmiIntegration implements EmiPlugin {
    @Override
    public void initialize(EmiInitRegistry registry) {
        EmiPlugin.super.initialize(registry);
    }

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager=registry.getRecipeManager();
        registry.addCategory(GrindstonePolishEmiRecipe.CATEGORY);
        manager.listAllOfType(INTERDIM_Recipes.GRINDSTONE_POLISH_RECIPE).forEach((r)->
                registry.addRecipe(new GrindstonePolishEmiRecipe(r.value())));
    }
}
