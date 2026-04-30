package ru.alexalabai.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib.packets.INTERDIM_ServerPackets;

public record SkyDisplayChangePayload(float colorR, float colorG, float colorB, boolean reset) implements CustomPayload {
    public static final Id<SkyDisplayChangePayload> ID = new Id<>(INTERDIM_ServerPackets.SKY_DISPLAY_CHANGE_PACKET);
    public static final PacketCodec<RegistryByteBuf, SkyDisplayChangePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, SkyDisplayChangePayload::colorR,
            PacketCodecs.FLOAT, SkyDisplayChangePayload::colorG,
            PacketCodecs.FLOAT, SkyDisplayChangePayload::colorB,
            PacketCodecs.BOOL, SkyDisplayChangePayload::reset,
            SkyDisplayChangePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
