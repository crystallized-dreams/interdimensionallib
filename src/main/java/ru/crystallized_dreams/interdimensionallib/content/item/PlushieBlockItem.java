package ru.crystallized_dreams.interdimensionallib.content.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.crystallized_dreams.interdimensionallib.client.screen.INTERDIM_ScreenHandlers;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_DataComponents;

public class PlushieBlockItem extends BlockItem {
    public PlushieBlockItem(Block block, Settings settings) {
        super(block, settings.component(INTERDIM_DataComponents.PLUSHIE_DATA, "Steve|0"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && world.isClient) {
            INTERDIM_ScreenHandlers.openPlushieRenameScreen();
            return new TypedActionResult<>(ActionResult.SUCCESS, user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
