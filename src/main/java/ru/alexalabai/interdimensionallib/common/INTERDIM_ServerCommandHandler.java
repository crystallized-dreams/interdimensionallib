package ru.alexalabai.interdimensionallib.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;
import ru.alexalabai.interdimensionallib.common.args.GrowRateArgumentType;
import ru.alexalabai.interdimensionallib.common.types.GrowRate;
import ru.alexalabai.interdimensionallib.packets.all.GuiDisplayChangePayload;
import ru.alexalabai.interdimensionallib.packets.all.ScreenShakePayload;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class INTERDIM_ServerCommandHandler {
    private static void sendScreenShakePacket(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> targets, GrowRate rate) {
        double intensity = DoubleArgumentType.getDouble(ctx, "intensity");
        float dur = FloatArgumentType.getFloat(ctx, "duration");
        if(targets!=null&&!targets.isEmpty()&&rate!=null&&intensity>0&&dur>0) {
            int realRate=0;
            switch (rate) {
                case GrowRate.FADE -> realRate=-1;
                case GrowRate.GROW -> realRate=1;
            }
            ScreenShakePayload payload = new ScreenShakePayload(intensity, (int)(20*dur), realRate, true);
            InterdimensionalLib.LOGGER.info("[INTERDIM]: Sent screen shake packets (duration: {}s, growRate:{})",
                    (int) (20 * dur), rate);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
        }
    }

    private static int setVisualElementState(CommandContext<ServerCommandSource> ctx, int id, boolean state) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            GuiDisplayChangePayload payload=new GuiDisplayChangePayload(id,state);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }

    public static void regArgTypes() {
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(InterdimensionalLib.MOD_ID, "grow_rate"),
                GrowRateArgumentType.class,
                ConstantArgumentSerializer.of(GrowRateArgumentType::growRate));
    }

    public static void regAll(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("screen").requires(src->src.hasPermissionLevel(1))
                .then(
                    literal("close").executes(ctx->{
                        if(ctx.getSource().getPlayer()!=null) ctx.getSource().getPlayer().closeHandledScreen();
                        return 1;
                    }).then(argument("targets", EntityArgumentType.players()).executes(ctx->{
                        for(var player : EntityArgumentType.getPlayers(ctx,"targets")) if(player!=null) player.closeHandledScreen();
                        return 1;
                    }))
                )
        );
        dispatcher.register(literal("camera").requires(src->src.hasPermissionLevel(1))
                    .then(literal("shake")
                        .then(argument("targets", EntityArgumentType.players())
                            .then(literal("start")
                                .then(argument("intensity", DoubleArgumentType.doubleArg())
                                    .then(argument("duration", FloatArgumentType.floatArg())
                                        .executes(ctx->{
                                            sendScreenShakePacket(ctx, EntityArgumentType.getPlayers(ctx, "targets"), GrowRate.NONE);
                                            return 1;
                                        })
                                        .then(argument("rate", GrowRateArgumentType.growRate())
                                                .executes(ctx->{
                                                    sendScreenShakePacket(ctx, EntityArgumentType.getPlayers(ctx, "targets"), GrowRateArgumentType.getGrowRate(ctx, "rate"));
                                                    return 1;
                                                }))
                            )))
                            .then(literal("stop").executes(ctx->{
                                    var targets=EntityArgumentType.getPlayers(ctx, "targets");
                                    if(targets!=null&&!targets.isEmpty()) {
                                        ScreenShakePayload payload = new ScreenShakePayload(0, 0, 0, true);
                                        InterdimensionalLib.LOGGER.info("[INTERDIM]: Sent screen shake stop packets");
                                        for(ServerPlayerEntity target : targets)
                                            ServerPlayNetworking.send(target, payload);
                                    }
                                    return 1;
                            }))
                        ))
        );
        dispatcher.register(literal("gui").requires(src->src.hasPermissionLevel(1))
                .then(argument("targets", EntityArgumentType.players())
                        .then(literal("hide")
                                .then(literal("all").executes(ctx->setVisualElementState(ctx,0,false)))
                                .then(literal("debug").executes(ctx->setVisualElementState(ctx,1,false)))
                                .then(literal("crosshair").executes(ctx->setVisualElementState(ctx,2,false)))
                                .then(literal("hotbar").executes(ctx->setVisualElementState(ctx,3,false)))
                                .then(literal("itemText").executes(ctx->setVisualElementState(ctx,4,false)))
                                .then(literal("overlayMessage").executes(ctx->setVisualElementState(ctx,5,false)))
                                .then(literal("titleSubtitle").executes(ctx->setVisualElementState(ctx,6,false)))
                                .then(literal("statusEffect").executes(ctx->setVisualElementState(ctx,7,false)))
                                .then(literal("experience").executes(ctx->setVisualElementState(ctx,8,false)))
                                .then(literal("health").executes(ctx->setVisualElementState(ctx,9,false)))
                                .then(literal("armor").executes(ctx->setVisualElementState(ctx,10,false)))
                                .then(literal("air").executes(ctx->setVisualElementState(ctx,11,false)))
                                .then(literal("mountHealth").executes(ctx->setVisualElementState(ctx,12,false)))
                                .then(literal("food").executes(ctx->setVisualElementState(ctx,13,false)))
                                .then(literal("scoreboardSidebar").executes(ctx->setVisualElementState(ctx,14,false)))
                                .then(literal("bossbars").executes(ctx->setVisualElementState(ctx,15,false)))
                                .then(literal("chat").executes(ctx->setVisualElementState(ctx,16,false)))
                                .then(literal("playerList").executes(ctx->setVisualElementState(ctx,17,false)))
                                .then(literal("misc").executes(ctx->setVisualElementState(ctx,18,false)))
                        )
                        .then(literal("show")
                                .then(literal("all").executes(ctx->setVisualElementState(ctx,0,true)))
                                .then(literal("debug").executes(ctx->setVisualElementState(ctx,1,true)))
                                .then(literal("crosshair").executes(ctx->setVisualElementState(ctx,2,true)))
                                .then(literal("hotbar").executes(ctx->setVisualElementState(ctx,3,true)))
                                .then(literal("itemText").executes(ctx->setVisualElementState(ctx,4,true)))
                                .then(literal("overlayMessage").executes(ctx->setVisualElementState(ctx,5,true)))
                                .then(literal("titleSubtitle").executes(ctx->setVisualElementState(ctx,6,true)))
                                .then(literal("statusEffect").executes(ctx->setVisualElementState(ctx,7,true)))
                                .then(literal("experience").executes(ctx->setVisualElementState(ctx,8,true)))
                                .then(literal("health").executes(ctx->setVisualElementState(ctx,9,true)))
                                .then(literal("armor").executes(ctx->setVisualElementState(ctx,10,true)))
                                .then(literal("air").executes(ctx->setVisualElementState(ctx,11,true)))
                                .then(literal("mountHealth").executes(ctx->setVisualElementState(ctx,12,true)))
                                .then(literal("food").executes(ctx->setVisualElementState(ctx,13,true)))
                                .then(literal("scoreboardSidebar").executes(ctx->setVisualElementState(ctx,14,true)))
                                .then(literal("bossbars").executes(ctx->setVisualElementState(ctx,15,true)))
                                .then(literal("chat").executes(ctx->setVisualElementState(ctx,16,true)))
                                .then(literal("playerList").executes(ctx->setVisualElementState(ctx,17,true)))
                                .then(literal("misc").executes(ctx->setVisualElementState(ctx,18,true)))
                        )
        ));
    }
}
