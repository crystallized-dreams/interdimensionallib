package ru.alexalabai.interdimensionallib.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;
import ru.alexalabai.interdimensionallib.common.args.GrowRateArgumentType;
import ru.alexalabai.interdimensionallib.common.types.GrowRate;
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
    }
}
