package ru.alexalabai.interdimensionallib.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.packets.all.MovementStatePayload;
import ru.alexalabai.interdimensionallib.common.types.MovementState;

@Environment(EnvType.SERVER)
@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
public class PlayerPoseServerMixin {
    //On other hand handling other ClientPlayerEntity states is easy for it. Literally why?
    @Inject(method="updatePose",at=@At(value="HEAD"),cancellable=true)
    void kc$makePlayerSupportPlayerMoveState(CallbackInfo info) {
        PlayerEntity player=(PlayerEntity)(Object)this;
        if(MovementStatePayload.MOVEMENT_STATES.containsKey(player.getUuid())) {
            MovementState state=MovementStatePayload.MOVEMENT_STATES.get(player.getUuid());
            switch(state) {
                case MovementState.CRAWLING:
                    //if(!ModConfig.INSTANCE.canPlayersCrawl) return;
                    player.setPose(EntityPose.SWIMMING);
                    break;
                case MovementState.SITTING:
                    break;
                default:
                    player.setPose(EntityPose.STANDING);
                    MovementStatePayload.MOVEMENT_STATES.remove(player.getUuid());
                    break;
            }
            info.cancel();
        }
    }
}
