package ru.alexalabai.interdimensionallib.mixin.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.alexalabai.interdimensionallib.client.ClientRendererConfig;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method="getSkyColor", at=@At("RETURN"), cancellable=true)
    void getSkyColor$interdim(CallbackInfoReturnable<Vec3d> info) {
        if(!ClientRendererConfig.Sky.override) return;
        info.setReturnValue(ClientRendererConfig.Sky.color);
    }
}
