package ru.alexalabai.interdimensionallib.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LiquidAbsorbingBlock extends Block {
    final Block soakedState;
    final Fluid fluid;
    final Item catalyst;
    boolean tryingToSoak=false;
    public LiquidAbsorbingBlock(Block soakedState, Fluid fluid, Item catalyst, Settings settings) {
        super(settings);
        this.soakedState=soakedState;
        this.fluid=fluid;
        this.catalyst=catalyst;
    }

    void scheduleSoaking(World world, BlockPos pos) {
        tryingToSoak=true;
        world.scheduleBlockTick(pos,this,20*world.random.nextBetween(1,8));
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if(world.isClient) return;
        boolean soak=canSoak(world,pos);
        if(!tryingToSoak&&soak) scheduleSoaking(world, pos);
        else if(tryingToSoak&&!soak) tryingToSoak=false;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClient) return;
        if(canSoak(world,pos)) scheduleSoaking(world, pos);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient||!stack.isOf(catalyst)) return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        convert((ServerWorld) world, pos, world.random);
        stack.decrementUnlessCreative(1,player);
        return ItemActionResult.CONSUME_PARTIAL;
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!tryingToSoak) return;
        if(random.nextInt(5)>1) convert(world, pos, random);
        else scheduleSoaking(world,pos);
    }

    void convert(ServerWorld world, BlockPos pos, Random random) {
        world.playSound(null,pos,SoundEvents.BLOCK_SPONGE_ABSORB, SoundCategory.BLOCKS,0.5f,1.f+random.nextFloat()*0.2f);
        world.setBlockState(pos,soakedState.getDefaultState());
    }

    boolean canSoak(World world, BlockPos pos) {
        if(world.getBlockState(pos.offset(Direction.Axis.Y,1)).getFluidState().isOf(fluid)) return true;
        else if(world.getBlockState(pos.offset(Direction.Axis.Y,-1)).getFluidState().isOf(fluid)) return true;
        else if(world.getBlockState(pos.offset(Direction.Axis.X,1)).getFluidState().isOf(fluid)) return true;
        else if(world.getBlockState(pos.offset(Direction.Axis.X,-1)).getFluidState().isOf(fluid)) return true;
        else if(world.getBlockState(pos.offset(Direction.Axis.Z,1)).getFluidState().isOf(fluid)) return true;
        else return world.getBlockState(pos.offset(Direction.Axis.Z, -1)).getFluidState().isOf(fluid);
    }
}
