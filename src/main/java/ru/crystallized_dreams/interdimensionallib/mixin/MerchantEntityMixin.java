package ru.crystallized_dreams.interdimensionallib.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import ru.crystallized_dreams.interdimensionallib.common.HelperTags;

@SuppressWarnings("unused")
@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends PassiveEntity {
    protected MerchantEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType,world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.add(3, new TemptGoal(this, 0.7, stack -> stack.isIn(HelperTags.VILLAGER_FOLLOW_ITEMS), false));
    }
}
