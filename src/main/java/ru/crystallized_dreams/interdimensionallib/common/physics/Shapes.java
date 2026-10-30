package ru.crystallized_dreams.interdimensionallib.common.physics;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.HashMap;
import java.util.Map;

public class Shapes {
    public static Map<Direction, VoxelShape> rotateHorizontal(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                                                              double maxSize) {
        var map = new HashMap<Direction, VoxelShape>();
        map.put(Direction.NORTH, Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ));
        map.put(Direction.SOUTH, Block.createCuboidShape(maxSize-maxX, minY, maxSize-maxZ, maxSize-minX, maxY, maxSize-minZ));
        map.put(Direction.WEST, Block.createCuboidShape(minZ, minY, minX, maxZ, maxY, maxX));
        map.put(Direction.EAST, Block.createCuboidShape(maxSize-maxZ, minY, minX, maxSize-minZ, maxY, maxX));
        return map;
    }
    public static Map<Direction, VoxelShape> rotateHorizontal(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return rotateHorizontal(minX, minY, minZ, maxX, maxY, maxZ, 16);
    }

    public static Map<Direction, VoxelShape> rotateAll(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                                                       double minSize, double maxSize) {
        var map = rotateHorizontal(minX, minY, minZ, maxX, maxY, maxZ, maxSize);
        map.put(Direction.UP, Block.createCuboidShape(minX, minSize, minY, maxX, maxZ-minZ, maxY));
        map.put(Direction.DOWN, Block.createCuboidShape(minX, maxSize-(maxZ-minZ), minY, maxX, maxSize, maxY));
        return map;
    }
    public static Map<Direction, VoxelShape> rotateAll(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return rotateAll(minX, minY, minZ, maxX, maxY, maxZ, 0, 16);
    }

    public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        var map = new HashMap<Direction.Axis, VoxelShape>();
        map.put(Direction.Axis.X, Block.createCuboidShape(minZ, minY, minX, maxZ, maxY, maxX));
        map.put(Direction.Axis.Z, Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ));
        return map;
    }
    public static Map<Direction.Axis, VoxelShape> rotateAllAxis(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                                                                double minSize, double maxSize) {
        var map = rotateHorizontalAxis(minX, minY, minZ, maxX, maxY, maxZ);
        map.put(Direction.Axis.Y, Block.createCuboidShape(minX, minSize, minY, maxX, maxZ-minZ, maxY));
        return map;
    }
    public static Map<Direction.Axis, VoxelShape> rotateAllAxis(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return rotateAllAxis(minX, minY, minZ, maxX, maxY, maxZ, 0, 16);
    }


    public static Map<Direction, VoxelShape> rotateHorizontal(AbstractVoxelShape shape) {
        return rotateHorizontal(shape.minX(), shape.minY(), shape.minZ(), shape.maxX(), shape.maxY(), shape.maxZ());
    }
    public static Map<Direction, VoxelShape> rotateAll(AbstractVoxelShape shape) {
        return rotateAll(shape.minX(), shape.minY(), shape.minZ(), shape.maxX(), shape.maxY(), shape.maxZ());
    }
    public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(AbstractVoxelShape shape) {
        return rotateHorizontalAxis(shape.minX(), shape.minY(), shape.minZ(), shape.maxX(), shape.maxY(), shape.maxZ());
    }
    public static Map<Direction.Axis, VoxelShape> rotateAllAxis(AbstractVoxelShape shape) {
        return rotateAllAxis(shape.minX(), shape.minY(), shape.minZ(), shape.maxX(), shape.maxY(), shape.maxZ());
    }


    public static Map<Direction, VoxelShape> rotateHorizontal(AbstractComplexVoxelShape shape) {
        var batch=rotateHorizontal(shape.shapes.getFirst());
        for (int i=0;i<shape.steps.size();i++) {
            var nextBatch=rotateHorizontal(shape.shapes.get(i+1));
            for (var dir : batch.keySet()) {
                batch.put(dir, VoxelShapes.combine(
                        batch.get(dir), nextBatch.get(dir), AbstractComplexVoxelShape.realMerge(shape.steps.get(i))
                ));
            }
        }
        return batch;
    }
    public static Map<Direction, VoxelShape> rotateAll(AbstractComplexVoxelShape shape) {
        var batch=rotateAll(shape.shapes.getFirst());
        for (int i=0;i<shape.steps.size();i++) {
            var nextBatch=rotateAll(shape.shapes.get(i+1));
            for (var dir : batch.keySet()) {
                batch.put(dir, VoxelShapes.combine(
                        batch.get(dir), nextBatch.get(dir), AbstractComplexVoxelShape.realMerge(shape.steps.get(i))
                ));
            }
        }
        return batch;
    }
    public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(AbstractComplexVoxelShape shape) {
        var batch=rotateHorizontalAxis(shape.shapes.getFirst());
        for (int i=0;i<shape.steps.size();i++) {
            var nextBatch=rotateHorizontalAxis(shape.shapes.get(i+1));
            for (var dir : batch.keySet()) {
                batch.put(dir, VoxelShapes.combine(
                        batch.get(dir), nextBatch.get(dir), AbstractComplexVoxelShape.realMerge(shape.steps.get(i))
                ));
            }
        }
        return batch;
    }
    public static Map<Direction.Axis, VoxelShape> rotateAllAxis(AbstractComplexVoxelShape shape) {
        var batch=rotateAllAxis(shape.shapes.getFirst());
        for (int i=0;i<shape.steps.size();i++) {
            var nextBatch=rotateAllAxis(shape.shapes.get(i+1));
            for (var dir : batch.keySet()) {
                batch.put(dir, VoxelShapes.combine(
                        batch.get(dir), nextBatch.get(dir), AbstractComplexVoxelShape.realMerge(shape.steps.get(i))
                ));
            }
        }
        return batch;
    }
}
