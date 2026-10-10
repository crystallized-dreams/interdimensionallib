package ru.crystallized_dreams.interdimensionallib.client.jei.category;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.content.recipe.GrindstonePolishRecipe;

public class GrindstonePolishJeiRecipe implements IRecipeCategory<GrindstonePolishRecipe> {
    public static final RecipeType<GrindstonePolishRecipe> TYPE =
            RecipeType.create(InterdimensionalLib.MOD_ID, "grindstone_polish", GrindstonePolishRecipe.class);

    private final IDrawable icon;
    private final IDrawableStatic arrow;
    private final Identifier grindIcon=Identifier.of(InterdimensionalLib.MOD_ID,"grind");

    public GrindstonePolishJeiRecipe(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.GRINDSTONE));
        arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public RecipeType<GrindstonePolishRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("jei.category.interdimensionallib.grindstone_polish");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 80;
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GrindstonePolishRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,0, 7).addIngredients(recipe.input());
        builder.addSlot(RecipeIngredientRole.OUTPUT,60,7).addItemStack(recipe.output());
    }

    @Override
    public void draw(GrindstonePolishRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics,34, 7);
        guiGraphics.drawGuiTexture(grindIcon,19,7,16,16);
    }
}
