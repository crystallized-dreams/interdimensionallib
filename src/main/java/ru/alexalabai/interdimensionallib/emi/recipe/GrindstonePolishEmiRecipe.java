package ru.alexalabai.interdimensionallib.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;
import ru.alexalabai.interdimensionallib.emi.EmiUtils;
import ru.alexalabai.interdimensionallib.recipe.all.GrindstonePolishRecipe;

import java.util.List;

public class GrindstonePolishEmiRecipe implements EmiRecipe {
    public static final EmiStack WORKSTATION=EmiStack.of(Items.GRINDSTONE);
    public static final EmiRecipeCategory CATEGORY=
            new EmiRecipeCategory(Identifier.of(InterdimensionalLib.MOD_ID,"grindstone_polish_ctg"),WORKSTATION);

    final GrindstonePolishRecipe recipe;

    public GrindstonePolishEmiRecipe(GrindstonePolishRecipe recipe) {
        this.recipe=recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }
    @Override
    public @Nullable Identifier getId() {
        return recipe.id();
    }
    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(recipe.input()));
    }
    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(recipe.output()));
    }

    @Override
    public int getDisplayWidth() {
        return 80;
    }
    @Override
    public int getDisplayHeight() {
        return 30;
    }

    @Override
    public void addWidgets(WidgetHolder ctx) {
        ctx.addSlot(getInputs().get(0),0, 7);
        ctx.addTexture(EmiTexture.EMPTY_ARROW, 34, 7);
        EmiUtils.addTexture(ctx,Identifier.of(InterdimensionalLib.MOD_ID,"textures/gui/grind.png"),19,7,16,16);
        ctx.addSlot(getOutputs().get(0),60,7).recipeContext(this);
    }
}
