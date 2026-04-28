package ru.alexalabai.interdimensionallib.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.client.ScreenShakeHandler;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
@Mixin(Camera.class)
public abstract class CameraShakeMixin {
    @Shadow
    protected abstract void setRotation(float yaw, float pitch);
    @Shadow
    protected abstract void setPos(Vec3d pos);

    @Inject(method = "update", at = @At("RETURN"))
    void kc$update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float delta, CallbackInfo info) {
        boolean active=ScreenShakeHandler.isActive();
        if(!active) return;

        Camera camera=(Camera)(Object)this;
        double curPitch = camera.getPitch();
        double curYaw = camera.getYaw();

        double newPitch = curPitch + ScreenShakeHandler.getOffset();
        double newYaw = curYaw + ScreenShakeHandler.getOffset();

        setRotation((float)MathHelper.lerp(0.2, curYaw, newYaw),(float) MathHelper.lerp(0.2, curPitch, newPitch));
    }
}
