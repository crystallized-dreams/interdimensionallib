package ru.alexalabai.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib.packets.ModPackets;

public record ResponseMovementStatePayload(int state) implements CustomPayload {
    public static final Id<ResponseMovementStatePayload> ID = new Id<>(ModPackets.RESPONSE_MOVEMENT_STATE_PACKET);
    public static final PacketCodec<RegistryByteBuf, ResponseMovementStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ResponseMovementStatePayload::state,
            ResponseMovementStatePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
