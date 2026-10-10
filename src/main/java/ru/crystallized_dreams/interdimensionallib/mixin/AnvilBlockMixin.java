package ru.crystallized_dreams.interdimensionallib.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_Recipes;
import ru.crystallized_dreams.interdimensionallib.content.recipe.AnvilCrushRecipe;

import java.util.List;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    @Inject(method="onLanding", at=@At("HEAD"))
    private void onLanding$interdim(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos,
                             FallingBlockEntity fallingBlockEntity, CallbackInfo info) {
        if (world.isClient()) return;

        Box searchArea=new Box(pos).expand(0.1, 0.25, 0.1);
        List<ItemEntity> items=world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class),
                searchArea, entity->true);

        for (ItemEntity itemEntity : items) {
            ItemStack inputStack=itemEntity.getStack();
            SingleStackRecipeInput input=new SingleStackRecipeInput(inputStack);

            List<RecipeEntry<AnvilCrushRecipe>> matches=world.getRecipeManager().getAllMatches(
                    INTERDIM_Recipes.ANVIL_CRUSH_RECIPE, input, world);

            if (matches.isEmpty()) continue;
            RecipeEntry<AnvilCrushRecipe> recipeEntry=matches.get(0);
            AnvilCrushRecipe recipe=recipeEntry.value();

            ItemStack converted=recipe.craft(input, world.getRegistryManager());
            if (converted.isEmpty()) continue;

            itemEntity.discard();
            int remaining=converted.getCount()*inputStack.getCount();
            int maxStack=converted.getMaxCount();
            while (remaining > 0) {
                int c=Math.min(remaining, maxStack);
                ItemStack piece=converted.copy();
                piece.setCount(c);
                ItemEntity ne=new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), piece);
                ne.setToDefaultPickupDelay();
                world.spawnEntity(ne);
                remaining -= c;
            }
        }
    }
}
