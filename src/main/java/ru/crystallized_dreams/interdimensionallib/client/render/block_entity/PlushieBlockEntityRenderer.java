package ru.crystallized_dreams.interdimensionallib.client.render.block_entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import ru.crystallized_dreams.interdimensionallib.content.block.entity.PlushieBlockEntity;

//TODO: Fetch skin, cache it and draw it.
public class PlushieBlockEntityRenderer implements BlockEntityRenderer<PlushieBlockEntity> {
    @Override
    public void render(PlushieBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

    }
}
