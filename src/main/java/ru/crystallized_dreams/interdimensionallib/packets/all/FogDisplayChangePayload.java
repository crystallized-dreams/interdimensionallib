package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

public record FogDisplayChangePayload(float start, float end, boolean sphere, boolean accountViewDistance, boolean reset) implements CustomPayload {
    public static final Id<FogDisplayChangePayload> ID = new Id<>(INTERDIM_ServerPackets.FOG_DISPLAY_CHANGE_PACKET);
    public static final PacketCodec<RegistryByteBuf, FogDisplayChangePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, FogDisplayChangePayload::start,
            PacketCodecs.FLOAT, FogDisplayChangePayload::end,
            PacketCodecs.BOOL, FogDisplayChangePayload::sphere,
            PacketCodecs.BOOL, FogDisplayChangePayload::accountViewDistance,
            PacketCodecs.BOOL, FogDisplayChangePayload::reset,
            FogDisplayChangePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
