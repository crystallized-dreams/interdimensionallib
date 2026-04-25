package ru.alexalabai.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib.packets.ModPackets;

public record ResponseSittingAllowedPayload(boolean allowed) implements CustomPayload {
    public static final Id<ResponseSittingAllowedPayload> ID = new Id<>(ModPackets.RESPONSE_SITTING_ALLOWED_PACKET);
    public static final PacketCodec<RegistryByteBuf, ResponseSittingAllowedPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, ResponseSittingAllowedPayload::allowed,
            ResponseSittingAllowedPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
