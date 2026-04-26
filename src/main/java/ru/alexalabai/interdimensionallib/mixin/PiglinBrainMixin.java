package ru.alexalabai.interdimensionallib.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.alexalabai.interdimensionallib.common.HelperTags;

@SuppressWarnings("unused")
@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method="wearsGoldArmor",at=@At("RETURN"),cancellable=true)
    private static void wearsGoldArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof PlayerEntity player && !info.getReturnValue()) {
            info.setReturnValue(player.getEquippedStack(EquipmentSlot.HEAD).isIn(HelperTags.PIGLINS_LOVE_ARMOR));
            if(info.getReturnValue()) info.cancel();
        }
    }
}
