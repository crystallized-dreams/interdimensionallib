package ru.crystallized_dreams.interdimensionallib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.crystallized_dreams.interdimensionallib.client.render.ClientCameraState;
import ru.crystallized_dreams.interdimensionallib.client.render.shader.ShaderEvents;
import ru.crystallized_dreams.interdimensionallib.client.render.shader.ShaderHandler;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(method="getBasicProjectionMatrix", at=@At("RETURN"))
    private Matrix4f applyRoll$interdim(Matrix4f original) {
        float roll=ClientCameraState.INSTANCE.getCurrentRoll();
        if(roll != 0f) return original.rotate(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
        return original;
    }

    @ModifyReturnValue(method="getFov", at=@At("RETURN"))
    private double getFov$interdim(double original) {
        if(ClientCameraState.INSTANCE.hasFovOverride()) return ClientCameraState.INSTANCE.getFovValue();
        return original;
    }
    @Inject(method = "render", at = @At(
            value = "INVOKE", shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V"
    ))
    private void onPostRender$interdim(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        float tickDelta = tickCounter.getTickDelta(tick);

        ShaderEvents.PRE_POST_PROCESS.invoker().onPrePostProcess(tickDelta);
        ShaderHandler.getInstance().onPostWorldRender(tickDelta);
        ShaderEvents.POST_POST_PROCESS.invoker().onPostPostProcess(tickDelta);
    }

    @Inject(method = "onResized", at = @At("TAIL"))
    private void onResized$interdim(int width, int height, CallbackInfo ci) {
        ShaderHandler.getInstance().onResized(width, height);
    }
}
