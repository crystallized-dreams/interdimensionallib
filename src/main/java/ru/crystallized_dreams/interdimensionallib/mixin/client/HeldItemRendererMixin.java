package ru.crystallized_dreams.interdimensionallib.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.crystallized_dreams.interdimensionallib.client.render.ClientCameraState;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Inject(method="renderFirstPersonItem", at=@At("HEAD"), cancellable=true)
    void renderFirstPersonItem$interdim(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if(ClientCameraState.INSTANCE.hasPositionOverride()||ClientCameraState.INSTANCE.hasTarget()) info.cancel();
        MinecraftClient mc = MinecraftClient.getInstance();
    }
}
