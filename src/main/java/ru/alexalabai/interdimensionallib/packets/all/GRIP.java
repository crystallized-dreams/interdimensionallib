package ru.alexalabai.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib.packets.INTERDIM_ServerPackets;

/*
* Global Request Interface Packet
* Currently isn't used.
*/
public record GRIP(int id) implements CustomPayload {
    public static final Id<GRIP> ID = new Id<>(INTERDIM_ServerPackets.GRIP_PACKET);
    public static final PacketCodec<RegistryByteBuf, GRIP> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, GRIP::id,
            GRIP::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
