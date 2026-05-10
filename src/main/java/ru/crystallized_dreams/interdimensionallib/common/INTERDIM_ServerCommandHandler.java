package ru.crystallized_dreams.interdimensionallib.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.common.args.EasingArgumentType;
import ru.crystallized_dreams.interdimensionallib.common.args.GrowRateArgumentType;
import ru.crystallized_dreams.interdimensionallib.common.args.VolumeShapeArgumentType;
import ru.crystallized_dreams.interdimensionallib.common.types.Easing;
import ru.crystallized_dreams.interdimensionallib.common.types.GrowRate;
import ru.crystallized_dreams.interdimensionallib.packets.all.*;

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
    private static int sendFogPacket(CommandContext<ServerCommandSource> ctx, boolean defaultAccountViewDistance, boolean defaultSphere) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            float start=FloatArgumentType.getFloat(ctx,"start");
            float end=FloatArgumentType.getFloat(ctx,"end");
            boolean _accountViewDistance=false;
            if(!defaultAccountViewDistance) _accountViewDistance=BoolArgumentType.getBool(ctx,"accountViewDistance");
            boolean _sphere=false;
            if(!defaultSphere) _sphere=BoolArgumentType.getBool(ctx,"sphere");
            FogDisplayChangePayload payload=new FogDisplayChangePayload(start,end,_sphere,_accountViewDistance,false);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendFogResetPacket(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            FogDisplayChangePayload payload=new FogDisplayChangePayload(1,1,true,true,true);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendSkyColorPacket(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            float red=FloatArgumentType.getFloat(ctx,"red");
            float green=FloatArgumentType.getFloat(ctx,"green");
            float blue=FloatArgumentType.getFloat(ctx,"blue");
            SkyPackets.SkyColorChangePayload payload=new SkyPackets.SkyColorChangePayload(red, green, blue,false);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendCameraFadePacket(CommandContext<ServerCommandSource> ctx, boolean defaultTime, boolean defaultEasing) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            float red=FloatArgumentType.getFloat(ctx,"red");
            float green=FloatArgumentType.getFloat(ctx,"green");
            float blue=FloatArgumentType.getFloat(ctx,"blue");
            float _time=20*2;
            if(!defaultTime) _time=FloatArgumentType.getFloat(ctx,"time");
            Easing easing=Easing.CUBIC_IN_OUT;
            if(!defaultEasing) easing=EasingArgumentType.getEasing(ctx,"easing");
            CameraPackets.CameraFadePayload payload=new CameraPackets.CameraFadePayload(red, green, blue,_time, Easing.toId(easing));
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendCameraResetPacket(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            CameraPackets.CameraResetPayload payload=new CameraPackets.CameraResetPayload();
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendCameraFovPacket(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            float fov=FloatArgumentType.getFloat(ctx,"fov");
            CameraPackets.CameraFovPayload payload=new CameraPackets.CameraFovPayload(fov);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendCameraPositionPacket(CommandContext<ServerCommandSource> ctx, boolean defaultTime, boolean defaultEasing) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            Vec3d pos=Vec3ArgumentType.getVec3(ctx,"pos");
            float time=0;
            if(!defaultTime) time=FloatArgumentType.getFloat(ctx,"time");
            Easing easing=Easing.LINEAR;
            if(!defaultEasing) easing=EasingArgumentType.getEasing(ctx,"easing");
            CameraPackets.CameraPositionPayload payload=new CameraPackets.CameraPositionPayload(pos.x,pos.y,pos.z,time,Easing.toId(easing));
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendCameraRotationPacket(CommandContext<ServerCommandSource> ctx, boolean defaultFixed, boolean defaultTime, boolean defaultEasing) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            float pitch=FloatArgumentType.getFloat(ctx,"pitch");
            float yaw=FloatArgumentType.getFloat(ctx,"yaw");
            float roll=FloatArgumentType.getFloat(ctx,"roll");
            boolean fixed=true;
            if(!defaultFixed) fixed=BoolArgumentType.getBool(ctx,"fixed");
            float time=0;
            if(!defaultTime) time=FloatArgumentType.getFloat(ctx,"time");
            Easing easing=Easing.LINEAR;
            if(!defaultEasing) easing=EasingArgumentType.getEasing(ctx,"easing");
            CameraPackets.CameraRotationPayload payload=new CameraPackets.CameraRotationPayload(pitch,yaw,roll,fixed,time,Easing.toId(easing));
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendCameraAttachPayload(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            Entity targetEntity=EntityArgumentType.getEntity(ctx,"target");
            CameraPackets.CameraAttachPayload payload=new CameraPackets.CameraAttachPayload(targetEntity.getId());
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendCameraDetachPayload(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            CameraPackets.CameraDetachPayload payload=new CameraPackets.CameraDetachPayload();
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendSkyColorResetPacket(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            SkyPackets.SkyColorChangePayload payload=new SkyPackets.SkyColorChangePayload(0.5f,0.5f,0.5f,true);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendSkyDisplayPacket(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            float sunSize=FloatArgumentType.getFloat(ctx,"sunSize");
            float moonSize=FloatArgumentType.getFloat(ctx,"moonSize");
            float starsBrightness=FloatArgumentType.getFloat(ctx,"starsBrightness");
            boolean showClouds=BoolArgumentType.getBool(ctx,"showClouds");
            SkyPackets.SkyDisplayChangePayload payload=new SkyPackets.SkyDisplayChangePayload(sunSize,moonSize,starsBrightness,showClouds);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendSkyDisplayResetPacket(CommandContext<ServerCommandSource> ctx) {
        try {
            var targets=EntityArgumentType.getPlayers(ctx,"targets");
            if(targets==null||targets.isEmpty()) {
                ctx.getSource().sendError(Text.of("No targets found"));
                return -1;
            }
            SkyPackets.SkyDisplayChangePayload payload=new SkyPackets.SkyDisplayChangePayload(30,20,1,true);
            for(ServerPlayerEntity target : targets)
                ServerPlayNetworking.send(target, payload);
            return 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of(e.getMessage()));
        }
        return -1;
    }
    private static int sendGuiElementDisplayPacket(CommandContext<ServerCommandSource> ctx, int id, boolean state) {
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
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(InterdimensionalLib.MOD_ID, "volume_shape"),
                VolumeShapeArgumentType.class,
                ConstantArgumentSerializer.of(VolumeShapeArgumentType::shape));
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(InterdimensionalLib.MOD_ID, "easing"),
                EasingArgumentType.class,
                ConstantArgumentSerializer.of(EasingArgumentType::easing));
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
                        })))
                )
                .then(literal("fade").then(argument("targets", EntityArgumentType.players())
                        .then(argument("red", FloatArgumentType.floatArg(0,1))
                                .then(argument("green", FloatArgumentType.floatArg(0,1))
                                        .then(argument("blue", FloatArgumentType.floatArg(0,1))
                                                .executes(ctx->sendCameraFadePacket(ctx,true, true))
                                                .then(argument("time", FloatArgumentType.floatArg(1))
                                                        .executes(ctx->sendCameraFadePacket(ctx,false, true))
                                                        .then(argument("easing",EasingArgumentType.easing()).executes(ctx->sendCameraFadePacket(ctx,false, false))))))))
                )
                .then(literal("position").then(argument("targets", EntityArgumentType.players()).then(argument("pos", Vec3ArgumentType.vec3())
                        .executes(ctx->sendCameraPositionPacket(ctx,true,true))
                                .then(argument("time",FloatArgumentType.floatArg(0)).executes(ctx->sendCameraPositionPacket(ctx,false,true))
                                        .then(argument("easing",EasingArgumentType.easing()).executes(ctx->sendCameraPositionPacket(ctx,false,false))))
                        ))
                )
                .then(literal("rotation").then(argument("targets", EntityArgumentType.players()).then(argument("pitch",FloatArgumentType.floatArg()).then(argument("yaw",FloatArgumentType.floatArg()).then(argument("roll",FloatArgumentType.floatArg())
                        .executes(ctx->sendCameraRotationPacket(ctx,true,true,true))
                        .then(argument("fixed",BoolArgumentType.bool())
                                .executes(ctx->sendCameraRotationPacket(ctx,false,true,true))
                                .then(argument("time",FloatArgumentType.floatArg())
                                        .executes(ctx->sendCameraRotationPacket(ctx,false,false,true))
                                        .then(argument("easing",EasingArgumentType.easing())
                                                .executes(ctx->sendCameraRotationPacket(ctx,false,false,false)))))))))
                )
                .then(literal("target")
                        .then(literal("attach").then(argument("targets", EntityArgumentType.players()).then(argument("target",EntityArgumentType.entity()).executes(INTERDIM_ServerCommandHandler::sendCameraAttachPayload))))
                        .then(literal("detach").then(argument("targets", EntityArgumentType.players()).executes(INTERDIM_ServerCommandHandler::sendCameraDetachPayload)))
                )
                .then(literal("fov").then(argument("targets", EntityArgumentType.players()).then(argument("fov",FloatArgumentType.floatArg()).executes(INTERDIM_ServerCommandHandler::sendCameraFovPacket))))
                .then(literal("reset").then(argument("targets", EntityArgumentType.players()).executes(INTERDIM_ServerCommandHandler::sendCameraResetPacket)))
        );
        dispatcher.register(literal("gui").requires(src->src.hasPermissionLevel(1))
                .then(argument("targets", EntityArgumentType.players())
                        .then(literal("hide")
                                .then(literal("*").executes(ctx-> sendGuiElementDisplayPacket(ctx,0,false)))
                                .then(literal("debug").executes(ctx-> sendGuiElementDisplayPacket(ctx,1,false)))
                                .then(literal("crosshair").executes(ctx-> sendGuiElementDisplayPacket(ctx,2,false)))
                                .then(literal("hotbar").executes(ctx-> sendGuiElementDisplayPacket(ctx,3,false)))
                                .then(literal("itemText").executes(ctx-> sendGuiElementDisplayPacket(ctx,4,false)))
                                .then(literal("overlayMessage").executes(ctx-> sendGuiElementDisplayPacket(ctx,5,false)))
                                .then(literal("titleSubtitle").executes(ctx-> sendGuiElementDisplayPacket(ctx,6,false)))
                                .then(literal("statusEffect").executes(ctx-> sendGuiElementDisplayPacket(ctx,7,false)))
                                .then(literal("experience").executes(ctx-> sendGuiElementDisplayPacket(ctx,8,false)))
                                .then(literal("health").executes(ctx-> sendGuiElementDisplayPacket(ctx,9,false)))
                                .then(literal("armor").executes(ctx-> sendGuiElementDisplayPacket(ctx,10,false)))
                                .then(literal("air").executes(ctx-> sendGuiElementDisplayPacket(ctx,11,false)))
                                .then(literal("mountHealth").executes(ctx-> sendGuiElementDisplayPacket(ctx,12,false)))
                                .then(literal("food").executes(ctx-> sendGuiElementDisplayPacket(ctx,13,false)))
                                .then(literal("scoreboardSidebar").executes(ctx-> sendGuiElementDisplayPacket(ctx,14,false)))
                                .then(literal("bossbars").executes(ctx-> sendGuiElementDisplayPacket(ctx,15,false)))
                                .then(literal("chat").executes(ctx-> sendGuiElementDisplayPacket(ctx,16,false)))
                                .then(literal("playerList").executes(ctx-> sendGuiElementDisplayPacket(ctx,17,false)))
                                .then(literal("misc").executes(ctx-> sendGuiElementDisplayPacket(ctx,18,false)))
                        )
                        .then(literal("show")
                                .then(literal("*").executes(ctx-> sendGuiElementDisplayPacket(ctx,0,true)))
                                .then(literal("debug").executes(ctx-> sendGuiElementDisplayPacket(ctx,1,true)))
                                .then(literal("crosshair").executes(ctx-> sendGuiElementDisplayPacket(ctx,2,true)))
                                .then(literal("hotbar").executes(ctx-> sendGuiElementDisplayPacket(ctx,3,true)))
                                .then(literal("itemText").executes(ctx-> sendGuiElementDisplayPacket(ctx,4,true)))
                                .then(literal("overlayMessage").executes(ctx-> sendGuiElementDisplayPacket(ctx,5,true)))
                                .then(literal("titleSubtitle").executes(ctx-> sendGuiElementDisplayPacket(ctx,6,true)))
                                .then(literal("statusEffect").executes(ctx-> sendGuiElementDisplayPacket(ctx,7,true)))
                                .then(literal("experience").executes(ctx-> sendGuiElementDisplayPacket(ctx,8,true)))
                                .then(literal("health").executes(ctx-> sendGuiElementDisplayPacket(ctx,9,true)))
                                .then(literal("armor").executes(ctx-> sendGuiElementDisplayPacket(ctx,10,true)))
                                .then(literal("air").executes(ctx-> sendGuiElementDisplayPacket(ctx,11,true)))
                                .then(literal("mountHealth").executes(ctx-> sendGuiElementDisplayPacket(ctx,12,true)))
                                .then(literal("food").executes(ctx-> sendGuiElementDisplayPacket(ctx,13,true)))
                                .then(literal("scoreboardSidebar").executes(ctx-> sendGuiElementDisplayPacket(ctx,14,true)))
                                .then(literal("bossbars").executes(ctx-> sendGuiElementDisplayPacket(ctx,15,true)))
                                .then(literal("chat").executes(ctx-> sendGuiElementDisplayPacket(ctx,16,true)))
                                .then(literal("playerList").executes(ctx-> sendGuiElementDisplayPacket(ctx,17,true)))
                                .then(literal("misc").executes(ctx-> sendGuiElementDisplayPacket(ctx,18,true)))
                        )
        ));
        dispatcher.register(literal("sky")
                .then(literal("fog").requires(src->src.hasPermissionLevel(1))
                            .then(literal("set").then(argument("targets", EntityArgumentType.players()).then(argument("start", FloatArgumentType.floatArg()).then(argument("end", FloatArgumentType.floatArg())
                                            .executes(ctx->sendFogPacket(ctx, true,true))
                                    .then(argument("accountViewDistance", BoolArgumentType.bool()).executes(ctx->sendFogPacket(ctx,false,true))
                                            .then(argument("sphere", BoolArgumentType.bool()).executes(ctx->sendFogPacket(ctx,false,false))))))))
                            .then(literal("reset").then(argument("targets", EntityArgumentType.players()).executes(INTERDIM_ServerCommandHandler::sendFogResetPacket)))
                    )
                .then(literal("color")
                        .then(literal("set")
                                .then(argument("targets", EntityArgumentType.players())
                                        .then(argument("red", FloatArgumentType.floatArg(0,1))
                                                .then(argument("green", FloatArgumentType.floatArg(0,1))
                                                        .then(argument("blue", FloatArgumentType.floatArg(0,1))
                                                                .executes(INTERDIM_ServerCommandHandler::sendSkyColorPacket))))))
                        .then(literal("reset").then(argument("targets", EntityArgumentType.players()).executes(INTERDIM_ServerCommandHandler::sendSkyColorResetPacket)))
                )
                .then(literal("display")
                        .then(literal("set")
                                .then(argument("targets", EntityArgumentType.players())
                                        .then(argument("sunSize", FloatArgumentType.floatArg(0))
                                                .then(argument("moonSize", FloatArgumentType.floatArg(0))
                                                        .then(argument("starsBrightness", FloatArgumentType.floatArg(0,1))
                                                                .then(argument("showClouds", BoolArgumentType.bool())
                                                                        .executes(INTERDIM_ServerCommandHandler::sendSkyDisplayPacket))))))
                        )
                        .then(literal("reset").then(argument("targets", EntityArgumentType.players()).executes(INTERDIM_ServerCommandHandler::sendSkyDisplayResetPacket)))
                )
        );
    }
}
