package ru.crystallized_dreams.interdimensionallib.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class ReadyFacingBlock extends FacingBlock {
    public static final MapCodec<ReadyFacingBlock> CODEC = createCodec(ReadyFacingBlock::new);

    @Override
    protected MapCodec<? extends FacingBlock> getCodec() {
        return CODEC;
    }

    public ReadyFacingBlock(Settings settings) {
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
        return super.getPlacementState(ctx).with(FACING,ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
