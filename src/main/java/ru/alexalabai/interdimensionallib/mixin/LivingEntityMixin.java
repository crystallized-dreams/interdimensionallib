package ru.alexalabai.interdimensionallib.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import ru.alexalabai.interdimensionallib.common.HelperTags;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    /**
     * @author Alex Alabai
     * @reason WHY SCAFFOLDINGS AREN'T IN A TAG ALREADY???
     */
    @Overwrite
    private Vec3d applyClimbingSpeed(Vec3d motion) {
        LivingEntity entity=(LivingEntity)(Object)this;
        if (entity.isClimbing()) {
            entity.onLanding();
            float f = 0.15F;
            double d = MathHelper.clamp(motion.x, -0.15F, 0.15F);
            double e = MathHelper.clamp(motion.z, -0.15F, 0.15F);
            double g = Math.max(motion.y, -0.15F);
            if (g < 0.0 && !entity.getBlockStateAtPos().isIn(HelperTags.SCAFFOLDING_BLOCKS) && entity.isHoldingOntoLadder() && entity instanceof PlayerEntity) {
                g = 0.0;
            }

            motion = new Vec3d(d, g, e);
        }

        return motion;
    }
}
