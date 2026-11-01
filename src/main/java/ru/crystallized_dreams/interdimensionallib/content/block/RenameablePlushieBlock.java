package ru.crystallized_dreams.interdimensionallib.content.block;


import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
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
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import ru.crystallized_dreams.interdimensionallib.common.INTERDIM_Sounds;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_DataComponents;
import ru.crystallized_dreams.interdimensionallib.content.block.entity.PlushieBlockEntity;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;

import java.util.List;

public class RenameablePlushieBlock extends BlockWithEntity {
    public static final DirectionProperty FACING=Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WIDE=BooleanProperty.of("wide");
    public static final VoxelShape SHAPE=Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public RenameablePlushieBlock(Settings settings) {
        super(settings.nonOpaque());
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(RenameablePlushieBlock::new);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        PlushieData data=stack.get(INTERDIM_DataComponents.PLUSHIE_DATA);
        if(data==null) tooltip.add(Text.translatable(super.getTranslationKey()).formatted(Formatting.GRAY));
        else tooltip.add(Text.literal(data.nickname()).formatted(Formatting.GRAY));
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
        builder.add(FACING,WIDE);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING,ctx.getHorizontalPlayerFacing());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(!world.isClient()) {
            world.playSound(null, pos, INTERDIM_Sounds.BLOCK_PLUSHIE_CLICK_SOUND, SoundCategory.PLAYERS,
                    0.7f, 1 + world.random.nextFloat() * 0.25f);
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

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlushieBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
