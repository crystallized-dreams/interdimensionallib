package ru.crystallized_dreams.interdimensionallib.mixin;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
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
import ru.crystallized_dreams.interdimensionallib.config.INTERDIM_ModConfig;

import java.util.function.BiConsumer;

@Mixin(AbstractPressurePlateBlock.class)
public abstract class AbstractPressurePlateBlockMixin extends Block {
    @Shadow @Final protected BlockSetType blockSetType;

    @Shadow protected abstract void updatePlateState(@Nullable Entity entity, World world, BlockPos pos, BlockState state, int output);

    @Shadow protected abstract int getRedstoneOutput(World world, BlockPos pos);

    @Shadow protected abstract BlockState setRedstoneOutput(BlockState state, int rsOut);

    @Shadow protected abstract void updateNeighbors(World world, BlockPos pos);

    @Shadow protected abstract int getTickRate();

    public AbstractPressurePlateBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "updatePlateState", at = @At("HEAD"), cancellable = true)
    void updatePlateState$kc(Entity entity, World world, BlockPos pos, BlockState state, int output, CallbackInfo info) {
        if(entity==null||!INTERDIM_ModConfig.INSTANCE.overhaulBlockInteractions) return;
        if(entity instanceof PlayerEntity && !blockSetType.canOpenByHand() && blockSetType.canButtonBeActivatedByArrows()) info.cancel();
    }

    @Override
    protected void onExploded(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        super.onExploded(state, world, pos, explosion, stackMerger);
        if (!explosion.canTriggerBlocks() || !blockSetType.canOpenByWindCharge() || world.isClient || !INTERDIM_ModConfig.INSTANCE.overhaulBlockInteractions) return;
        int o=getRedstoneOutput(world, pos);
        if (o>0 && !state.get(Properties.POWERED)) {
            BlockState blockState = setRedstoneOutput(state, o);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            updateNeighbors(world, pos);
            world.scheduleBlockRerenderIfNeeded(pos, state, blockState);
            world.scheduleBlockTick(new BlockPos(pos), this, getTickRate());
        }
    }
}
