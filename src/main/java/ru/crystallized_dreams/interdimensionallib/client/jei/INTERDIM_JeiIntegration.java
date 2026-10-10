package ru.crystallized_dreams.interdimensionallib.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.client.jei.category.AnvilCrushJeiRecipe;
import ru.crystallized_dreams.interdimensionallib.client.jei.category.GrindstonePolishJeiRecipe;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_Recipes;
import ru.crystallized_dreams.interdimensionallib.content.recipe.AnvilCrushRecipe;
import ru.crystallized_dreams.interdimensionallib.content.recipe.GrindstonePolishRecipe;

@JeiPlugin
public class INTERDIM_JeiIntegration implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    public static final RecipeType<GrindstonePolishRecipe> GRINDSTONE_POLISH=JeiUtils.createRecipeType(
            Identifier.of(InterdimensionalLib.MOD_ID,"grindstone_polish"), GrindstonePolishRecipe.class);
    public static final RecipeType<AnvilCrushRecipe> ANVIL_CRUSH=JeiUtils.createRecipeType(
            Identifier.of(InterdimensionalLib.MOD_ID,"anvil_crush"), AnvilCrushRecipe.class);

    @Override
    public Identifier getPluginUid() {
        return Identifier.of(InterdimensionalLib.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(GRINDSTONE_POLISH, JeiUtils.listRecipes(INTERDIM_Recipes.GRINDSTONE_POLISH_RECIPE));
        registration.addRecipes(ANVIL_CRUSH, JeiUtils.listRecipes(INTERDIM_Recipes.ANVIL_CRUSH_RECIPE));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper=registration.getJeiHelpers();
        var guiHelper=registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new GrindstonePolishJeiRecipe(guiHelper));
        registration.addRecipeCategories(new AnvilCrushJeiRecipe(guiHelper));
    }
}
