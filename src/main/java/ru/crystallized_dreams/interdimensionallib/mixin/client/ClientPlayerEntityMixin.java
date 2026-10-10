package ru.crystallized_dreams.interdimensionallib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ru.crystallized_dreams.interdimensionallib.client.render.ClientCameraState;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @ModifyReturnValue(method="isCamera", at=@At("RETURN"))
    boolean isCamera$interdim(boolean original) {
        return !ClientCameraState.INSTANCE.hasTarget() && original && !ClientCameraState.INSTANCE.needToShowPlayer();
    }
}
