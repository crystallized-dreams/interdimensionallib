package ru.crystallized_dreams.interdimensionallib.common.physics;

import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;

public record AbstractVoxelShape(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    public VoxelShape build() {
        return Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
