package ru.crystallized_dreams.interdimensionallib.content.block;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.crystallized_dreams.interdimensionallib.common.INTERDIM_Sounds;

import java.util.List;

public class PlushieBlock extends Block {
    public static final DirectionProperty FACING=Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WIDE=BooleanProperty.of("wide");
    public static final VoxelShape COLLIDER=Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 14.0, 12.0);
    private final boolean clown;
    public int clicksLastMS;
    public long lastMSTime;

    public PlushieBlock(Settings settings) {
        super(settings.nonOpaque());
        this.clown=false;
    }
    public PlushieBlock(Settings settings, boolean clown) {
        super(settings.nonOpaque());
        this.clown=clown;
        clicksLastMS=0;
        lastMSTime=0L;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        tooltip.add(Text.translatable(super.getTranslationKey()).formatted(Formatting.GRAY));
    }

    @Override
    public String getTranslationKey() {
        return "block.interdimensionallib.plushie";
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLIDER;
    }
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLIDER;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING,WIDE);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING,ctx.getHorizontalPlayerFacing());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(!world.isClient()) {
            world.playSound(null, pos, clown ? INTERDIM_Sounds.BLOCK_PLUSHIE_CLOWN_CLICK_SOUND
                    : INTERDIM_Sounds.BLOCK_PLUSHIE_CLICK_SOUND, SoundCategory.PLAYERS,
                    0.7f, 1 + world.random.nextFloat() * 0.25f);
        }
        if (clown) {
            if (System.currentTimeMillis() - 200 > lastMSTime) {
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
    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1;
    }
}
