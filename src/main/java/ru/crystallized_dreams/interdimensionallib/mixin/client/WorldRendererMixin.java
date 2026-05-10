package ru.crystallized_dreams.interdimensionallib.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.crystallized_dreams.interdimensionallib.client.ClientRendererConfig;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @ModifyConstant(
            method = "renderSky",
            constant = @Constant(floatValue = 30.0F)
    )
    private float sunSize$interdim(float original) {
        return ClientRendererConfig.Sky.sunSize;
    }
    @ModifyConstant(
            method = "renderSky",
            constant = @Constant(floatValue = 20.0F)
    )
    private float moonSize$interdim(float original) {
        return ClientRendererConfig.Sky.moonSize;
    }

    @Redirect(
            method = "renderSky",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getStarBrightness(F)F")
    )
    private float starBrightness$interdim(ClientWorld world, float tickDelta) {
        if(ClientRendererConfig.Sky.starsBrightness!=1) return ClientRendererConfig.Sky.starsBrightness;
        return world.getStarBrightness(tickDelta);
    }

    @Inject(
            method = "renderClouds",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderClouds$interdim(MatrixStack matrices, Matrix4f matrix4f, Matrix4f matrix4f2, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo info) {
        if (!ClientRendererConfig.Sky.showClouds) info.cancel();
    }
}
