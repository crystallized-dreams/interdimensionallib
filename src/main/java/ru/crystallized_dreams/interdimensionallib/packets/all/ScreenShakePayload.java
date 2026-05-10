package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

public record ScreenShakePayload(double intensity, int duration, int rate, boolean overwrite) implements CustomPayload {
    public static final Id<ScreenShakePayload> ID = new Id<>(INTERDIM_ServerPackets.SCREENSHAKE_PACKET);
    public static final PacketCodec<RegistryByteBuf, ScreenShakePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.DOUBLE, ScreenShakePayload::intensity,
            PacketCodecs.INTEGER, ScreenShakePayload::duration,
            PacketCodecs.INTEGER, ScreenShakePayload::rate,
            PacketCodecs.BOOL, ScreenShakePayload::overwrite,
            ScreenShakePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
