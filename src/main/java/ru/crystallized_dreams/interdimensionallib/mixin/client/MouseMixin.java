package ru.crystallized_dreams.interdimensionallib.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.crystallized_dreams.interdimensionallib.client.ClientCameraState;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method="updateMouse",at=@At(value="INVOKE",target="Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"), cancellable = true)
    void changeLookDirection$interdim(double timeDelta, CallbackInfo info) {
        ClientCameraState state=ClientCameraState.INSTANCE;
        if(state.hasRotationOverride()) {
            client.player.setYaw((float)state.getCurrentRotation().x);
            client.player.setPitch((float)state.getCurrentRotation().y);
            info.cancel();
        }
    }
}
