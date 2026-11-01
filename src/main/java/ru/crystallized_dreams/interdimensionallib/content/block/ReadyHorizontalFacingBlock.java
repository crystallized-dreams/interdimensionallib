package ru.crystallized_dreams.interdimensionallib.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class ReadyHorizontalFacingBlock extends HorizontalFacingBlock {
    public static final MapCodec<ReadyHorizontalFacingBlock> CODEC = createCodec(ReadyHorizontalFacingBlock::new);

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    public ReadyHorizontalFacingBlock(Settings settings) {
        super(settings);
        setDefaultState(super.getDefaultState().with(FACING,Direction.NORTH));
    }

    @Override
    protected boolean isCullingShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        if (super.getPlacementState(ctx)==null) return null;
        return super.getPlacementState(ctx).with(FACING,ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
