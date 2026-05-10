package ru.crystallized_dreams.interdimensionallib.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import ru.crystallized_dreams.interdimensionallib.common.ItemUtils;
import ru.crystallized_dreams.interdimensionallib.recipe.INTERDIM_Recipes;
import ru.crystallized_dreams.interdimensionallib.recipe.all.GrindstonePolishRecipe;

import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(GrindstoneBlock.class)
public abstract class GrindstoneBlockMixin extends Block {
    @Shadow @Final private static Text TITLE;

    public GrindstoneBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    void processItem(World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        if(world.isClient) return;
        player.getItemCooldownManager().set(stack.getItem(),10);
        stack.decrementUnlessCreative(1,player);
        world.playSound(null,pos,SoundEvents.BLOCK_GRINDSTONE_USE,SoundCategory.BLOCKS);
    }

    @Unique @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.getItemCooldownManager().isCoolingDown(stack.getItem())) return ItemActionResult.FAIL;
        SingleStackRecipeInput input=new SingleStackRecipeInput(stack);
        Optional<RecipeEntry<GrindstonePolishRecipe>> recipe=world.getRecipeManager().getFirstMatch(INTERDIM_Recipes.GRINDSTONE_POLISH_RECIPE,input,world);
        if(recipe.isEmpty()) return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        processItem(world,pos,player,stack);
        ItemUtils.tryToInsertStack(player,recipe.get().value().craft(input,world.getRegistryManager()));
        return ItemActionResult.CONSUME;
    }
}
