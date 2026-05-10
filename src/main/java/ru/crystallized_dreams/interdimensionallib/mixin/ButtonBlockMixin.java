package ru.crystallized_dreams.interdimensionallib.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.crystallized_dreams.interdimensionallib.config.INTERDIM_ModConfig;

import java.util.function.BiConsumer;

@Mixin(ButtonBlock.class)
public abstract class ButtonBlockMixin extends Block {
    @Shadow @Final private BlockSetType blockSetType;

    @Shadow public abstract void powerOn(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player);

    public ButtonBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onExploded", at = @At("HEAD"), cancellable = true)
    void onExploded$kc(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger, CallbackInfo info) {
        if(!INTERDIM_ModConfig.INSTANCE.overhaulBlockInteractions) return;
        info.cancel();
        if (explosion.canTriggerBlocks() && !(Boolean)state.get(Properties.POWERED) && blockSetType.canOpenByWindCharge()) {
            powerOn(state, world, pos, null);
        }

        super.onExploded(state, world, pos, explosion, stackMerger);
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    void onUse$kc(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        if(!INTERDIM_ModConfig.INSTANCE.overhaulBlockInteractions) return;
        if(!blockSetType.canOpenByHand() && blockSetType.canButtonBeActivatedByArrows()) info.setReturnValue(ActionResult.CONSUME);
    }
}
