package ru.alexalabai.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib.packets.ModPackets;
import ru.alexalabai.interdimensionallib.common.types.MovementState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record MovementStatePayload(int state) implements CustomPayload {
    public static final Id<MovementStatePayload> ID = new Id<>(ModPackets.MOVEMENT_STATE_PACKET);
    public static final PacketCodec<RegistryByteBuf, MovementStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, MovementStatePayload::state,
            MovementStatePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static Map<UUID, MovementState> MOVEMENT_STATES = new HashMap<>();
    public static void setState(UUID player, MovementState state) {
        MOVEMENT_STATES.put(player,state);
    }
    public static void setState(UUID player, int stateV) {
        MovementState state=stateV==0?MovementState.NONE:stateV==2?MovementState.CRAWLING:MovementState.SITTING;
        MOVEMENT_STATES.put(player,state);
    }
}
