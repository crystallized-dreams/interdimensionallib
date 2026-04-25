package ru.alexalabai.interdimensionallib.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.InterdimensionalLibClient;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
public class PlayerPoseClientMixin {
    //Client pose handling is stupid and needs moral support to work for some reason
    @Inject(method="updatePose",at=@At(value="HEAD"),cancellable=true)
    void kc$makePlayerSupportPlayerMoveState(CallbackInfo info) {
        if(InterdimensionalLibClient.activelyCrawling&& InterdimensionalLibClient.isCrawlingAllowedOnServer) {
            ((PlayerEntity)(Object)this).setPose(EntityPose.SWIMMING);
            info.cancel();
        }
    }
}
