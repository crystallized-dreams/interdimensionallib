package ru.crystallized_dreams.interdimensionallib.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
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
import ru.crystallized_dreams.interdimensionallib.client.ClientCameraState;
import ru.crystallized_dreams.interdimensionallib.client.ScreenShakeHandler;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow private Vec3d pos;

    @Shadow protected abstract void setPos(Vec3d pos);

    @Inject(method="update", at=@At("RETURN"))
    private void update$interdim(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        ClientCameraState state=ClientCameraState.INSTANCE;
        state.tick(tickDelta/20f);
        if(state.hasTarget()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world!=null) {
                Entity target = client.world.getEntityById(state.getTargetEntityId());
                if(target!=null) {
                    double dx=target.getX()-pos.x;
                    double dy=(target.getEyeY())-pos.y;
                    double dz=target.getZ()-pos.z;
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    float yaw = (float) (MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
                    float pitch = (float) -(MathHelper.atan2(dy, dist) * (180.0 / Math.PI));
                    setRotation(yaw, pitch);
                }
            }
        }
        if(state.hasPositionOverride()) setPos(state.getCurrentPosition());
        if(state.hasRotationOverride()) {
            Vec3d rot = state.getCurrentRotation();
            setRotation((float) rot.y, (float) rot.x);
        }
        ScreenShakeHandler.update();
    }
}
