package ru.crystallized_dreams.interdimensionallib.content.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.client.screen.INTERDIM_ScreenHandler;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_DataComponents;
import ru.crystallized_dreams.interdimensionallib.content.component.CommandExecutorData;

import java.util.List;

public class CommandExecutorItem extends ItemWithHint {
    private int curCooldown=0;
    public CommandExecutorItem(Settings settings) {
        super(settings);
    }

    public static String getName(int type) {
        return switch (type) {
            case 0->"item.interdimensionallib.command_executor.button";
            case 1->"item.interdimensionallib.command_executor.lever";
            case 2->"item.interdimensionallib.command_executor.joystick";
            default -> "the greatest controller type. idk what this is";
        };
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        var stack=player.getStackInHand(hand);
        if (player.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.fail(stack);
        if (world.isClient()) {
            if (player.isSneaking()) INTERDIM_ScreenHandler.openCommandExecutorEditorScreen(
                    stack.getOrDefault(INTERDIM_DataComponents.COMMAND_EXECUTOR_DATA, CommandExecutorData.DEFAULT));
            return TypedActionResult.success(player.getStackInHand(hand), false);
        }
        if (player.isSneaking()) return TypedActionResult.success(stack);
        var data=stack.getOrDefault(INTERDIM_DataComponents.COMMAND_EXECUTOR_DATA, CommandExecutorData.DEFAULT);
        ServerPlayerEntity user=(ServerPlayerEntity)player;
        ServerWorld serverWorld=(ServerWorld)player.getWorld();
        if (data.controllerType()==0) {
            executeCommand(serverWorld, user, data.commands().getFirst());
            return TypedActionResult.success(player.getStackInHand(hand), false);
        } else if (data.controllerType()==1) {
            data=new CommandExecutorData(data.commands(), data.controllerType(), data.pressed()==0?1:0);
            stack.set(INTERDIM_DataComponents.COMMAND_EXECUTOR_DATA, data);
            user.setStackInHand(hand, stack);
            user.getItemCooldownManager().set(this, 10);
            return TypedActionResult.success(stack, false);
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) return;
        if (entity instanceof ServerPlayerEntity player) {
            if (curCooldown>0) {
                curCooldown--;
                return;
            }
            var data=stack.getOrDefault(INTERDIM_DataComponents.COMMAND_EXECUTOR_DATA, CommandExecutorData.DEFAULT);
            if (data.controllerType()==1 && data.pressed()==1) {
                executeCommand((ServerWorld)world, player, data.commands().getFirst());
                curCooldown=3;
            }
        }
    }

    private void executeCommand(ServerWorld world, ServerPlayerEntity user, String command) {
        world.getServer().getCommandManager().executeWithPrefix(user.getCommandSource(), command);
    }

    @Override
    public void beforeHint(ItemStack stack, TooltipContext ctx, List<Text> tooltip) {
        tooltip.add(Text.translatable(getName(stack.getOrDefault(INTERDIM_DataComponents.COMMAND_EXECUTOR_DATA,
                CommandExecutorData.DEFAULT).controllerType())).formatted(Formatting.GRAY));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext ctx, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, ctx, tooltip, type);
        if(stack.getOrDefault(INTERDIM_DataComponents.COMMAND_EXECUTOR_DATA, CommandExecutorData.DEFAULT).controllerType()==2)
            tooltip.add(Text.translatable("text.interdimensionallib.command_controller_wip").formatted(Formatting.RED));
    }

    @Override
    public void showHint(ItemStack stack, TooltipContext ctx, List<Text> tooltip) {
        tooltip.add(Text.translatable("text.interdimensionallib.command_executor_hint").formatted(Formatting.GRAY));
    }

    @Override
    public boolean canShowHint(ItemStack stack, TooltipContext ctx) {
        return MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.isCreative();
    }
}
