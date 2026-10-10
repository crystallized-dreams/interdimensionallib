package ru.crystallized_dreams.interdimensionallib.client.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.client.emi.EmiUtils;
import ru.crystallized_dreams.interdimensionallib.content.recipe.AnvilCrushRecipe;
import ru.crystallized_dreams.interdimensionallib.content.recipe.GrindstonePolishRecipe;

import java.util.List;

public class AnvilCrushEmiRecipe implements EmiRecipe {
    public static final EmiStack WORKSTATION=EmiStack.of(Items.GRINDSTONE);
    public static final EmiRecipeCategory CATEGORY=
            new EmiRecipeCategory(Identifier.of(InterdimensionalLib.MOD_ID,"anvil_crush_ctg"),WORKSTATION);

    final AnvilCrushRecipe recipe;

    public AnvilCrushEmiRecipe(AnvilCrushRecipe recipe) {
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
        EmiUtils.addTexture(ctx,Identifier.of(InterdimensionalLib.MOD_ID,"textures/gui/sprites/anvil_crush.png"),19,7,16,16);
        ctx.addSlot(getOutputs().get(0),60,7).recipeContext(this);
    }
}
