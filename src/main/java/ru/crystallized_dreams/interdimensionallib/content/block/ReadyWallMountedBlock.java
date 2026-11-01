package ru.crystallized_dreams.interdimensionallib.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;

public class ReadyWallMountedBlock extends WallMountedBlock {
    public static final MapCodec<ReadyWallMountedBlock> CODEC=createCodec(ReadyWallMountedBlock::new);

    protected ReadyWallMountedBlock(Settings settings) {
        super(settings);
        setDefaultState(super.getDefaultState().with(FACING, Direction.NORTH).with(FACE, BlockFace.WALL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING);
    }

    @Override
    protected MapCodec<? extends WallMountedBlock> getCodec() {
        return CODEC;
    }
}
