package ru.alexalabai.interdimensionallib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ru.alexalabai.interdimensionallib.client.ClientCameraState;

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
}
