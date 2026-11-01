package ru.crystallized_dreams.interdimensionallib.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.crystallized_dreams.interdimensionallib.common.INTERDIM_Sounds;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_DataComponents;
import ru.crystallized_dreams.interdimensionallib.content.block.entity.PlushieBlockEntity;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;

public class PlushieBlock extends ReadyHorizontalFacingBlock {
    public static final BooleanProperty WIDE = BooleanProperty.of("wide");
    public static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    private final boolean clown;
    public int clicksLastMS;
    public long lastMSTime;

    public PlushieBlock(Settings settings) {
        super(settings);
        this.clown=false;
    }

    public PlushieBlock(Settings settings, boolean clown) {
        super(settings);
        this.clown=clown;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WIDE);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            world.playSound(null, pos, clown ? INTERDIM_Sounds.BLOCK_PLUSHIE_CLOWN_CLICK_SOUND
                            : INTERDIM_Sounds.BLOCK_PLUSHIE_CLICK_SOUND, SoundCategory.PLAYERS,
                    0.7f, 1 + world.random.nextFloat() * 0.25f);
        }
        if (clown) {
            if (System.currentTimeMillis() - 300 > lastMSTime) {
                lastMSTime = System.currentTimeMillis();
                clicksLastMS = 1;
            } else {
                lastMSTime = System.currentTimeMillis();
                clicksLastMS++;
            }

            if (clicksLastMS >= 50) {
                world.createExplosion(null, null, null,
                        pos.getX(), pos.getY(), pos.getZ(),
                        3.F, false, World.ExplosionSourceType.TNT);
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state,
                         @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof PlushieBlockEntity plushie) {
                PlushieData data = itemStack.get(INTERDIM_DataComponents.PLUSHIE_DATA);
                if (data != null) {
                    plushie.setData(data);
                }
            }
        }
    }

    @Override
    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1;
    }
}
