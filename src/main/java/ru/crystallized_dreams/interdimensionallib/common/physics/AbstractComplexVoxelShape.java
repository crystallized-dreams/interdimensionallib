package ru.crystallized_dreams.interdimensionallib.common.physics;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;

public class AbstractComplexVoxelShape {
    public List<MergeOp> steps;
    public List<AbstractVoxelShape> shapes;
    public class Builder {
        public Builder() {
            shapes = new ArrayList<>();
            steps = new ArrayList<>();
        }

        public Builder or(AbstractVoxelShape shape) {
            shapes.add(shape);
            steps.add(MergeOp.OR);
            return this;
        }

        public Builder and(AbstractVoxelShape shape) {
            shapes.add(shape);
            steps.add(MergeOp.AND);
            return this;
        }

        public Builder nor(AbstractVoxelShape shape) {
            shapes.add(shape);
            steps.add(MergeOp.NOR);
            return this;
        }

        public Builder nand(AbstractVoxelShape shape) {
            shapes.add(shape);
            steps.add(MergeOp.NAND);
            return this;
        }

        public VoxelShape build() {
            var shape=shapes.getFirst().build();
            for (int i=0;i<steps.size();i++)
                shape=VoxelShapes.combine(shape, shapes.get(i+1).build(), realMerge(steps.get(i)));
            return shape;
        }
    }
    public enum MergeOp {
        OR,
        AND,
        NOR,
        NAND
    }
    public static BooleanBiFunction realMerge(MergeOp opCode) {
        return switch (opCode) {
            case OR -> BooleanBiFunction.OR;
            case AND -> BooleanBiFunction.AND;
            case NOR -> BooleanBiFunction.NOT_OR;
            case NAND -> BooleanBiFunction.NOT_AND;
        };
    }
}
