package ru.alexalabai.interdimensionallib.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;
import ru.alexalabai.interdimensionallib.config.ModConfig;
import ru.alexalabai.interdimensionallib.entity.ModEntities;
import ru.alexalabai.interdimensionallib.entity.SeatEntity;
import ru.alexalabai.interdimensionallib.packets.all.*;
import ru.alexalabai.interdimensionallib.common.types.MovementState;

public class ModPackets {
    public static final Identifier GRIP_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "grip");
    public static final Identifier MOVEMENT_STATE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "movement_state");
    public static final Identifier RESPONSE_MOVEMENT_STATE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "response_movement_state");
    public static final Identifier RESPONSE_SITTING_ALLOWED_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "response_can_sit");
    public static final Identifier RESPONSE_CRAWLING_ALLOWED_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "response_can_crawl");
    public static final Identifier SCREENSHAKE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "screenshake");
    public static void regAll() {
        PayloadTypeRegistry.playC2S().register(GRIP.ID, GRIP.CODEC);
        PayloadTypeRegistry.playC2S().register(MovementStatePayload.ID, MovementStatePayload.CODEC);
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered client packet payloads (C2S)");
        PayloadTypeRegistry.playS2C().register(ResponseMovementStatePayload.ID, ResponseMovementStatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ResponseSittingAllowedPayload.ID, ResponseSittingAllowedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ResponseCrawlingAllowedPayload.ID, ResponseCrawlingAllowedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ScreenShakePayload.ID, ScreenShakePayload.CODEC);
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered client packet payloads (S2C)");
        ServerPlayNetworking.registerGlobalReceiver(GRIP.ID, (payload, ctx)-> {
            switch (payload.id()) {
                case 1:
                    MovementState state=MovementStatePayload.MOVEMENT_STATES.get(ctx.player().getUuid());
                    ctx.responseSender().sendPacket(new ResponseMovementStatePayload((state==MovementState.NONE)?0:(state==MovementState.CRAWLING)?2:1));
                    break;
                case 2:
                    ctx.responseSender().sendPacket(new ResponseSittingAllowedPayload(ModConfig.INSTANCE.canPlayersSit));
                    break;
                case 3:
                    ctx.responseSender().sendPacket(new ResponseSittingAllowedPayload(ModConfig.INSTANCE.canPlayersCrawl));
                    break;
            }

        });
        ServerPlayNetworking.registerGlobalReceiver(MovementStatePayload.ID, (payload, ctx)->
                ctx.player().server.execute(()->{
                    //if(!ModConfig.INSTANCE.canPlayersSit) return;
                    MovementStatePayload.setState(ctx.player().getUuid(), payload.state());
                    if(payload.state()==1 && ctx.player().isOnGround()) {
                        SeatEntity ent= ModEntities.SEAT_ENTITY.create(ctx.player().getWorld());
                        if(ent==null) {
                            InterdimensionalLib.LOGGER.error("[INTERDIM] Somehow creating seat entity at {} {} {} produced null",
                                    ctx.player().getX(),ctx.player().getY(),ctx.player().getZ());
                            return;
                        }
                        ent.setPosition(ctx.player().getPos());
                        ent.setYaw(ctx.player().getYaw());
                        ctx.player().getServerWorld().spawnEntity(ent);
                        ctx.player().startRiding(ent);
                    } else if(payload.state()==0 && ctx.player().hasVehicle()) ctx.player().stopRiding();
                }));
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered server packet receivers");
    }
}
