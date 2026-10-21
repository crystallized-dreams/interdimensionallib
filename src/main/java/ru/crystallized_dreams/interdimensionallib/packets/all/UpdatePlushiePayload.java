package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

public record UpdatePlushiePayload(String playerName, boolean wide) implements CustomPayload {
    public static final Id<UpdatePlushiePayload> ID = new Id<>(INTERDIM_ServerPackets.UPDATE_PLUSHIE_PACKET);
    public static final PacketCodec<RegistryByteBuf, UpdatePlushiePayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING, UpdatePlushiePayload::playerName,
                    PacketCodecs.BOOL, UpdatePlushiePayload::wide,
                    UpdatePlushiePayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
