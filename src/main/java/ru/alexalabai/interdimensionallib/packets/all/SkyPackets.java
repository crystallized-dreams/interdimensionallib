package ru.alexalabai.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib.packets.INTERDIM_ServerPackets;

public class SkyPackets {
    public record SkyDisplayChangePayload(float sunSize, float moonSize, float starsBrightness, boolean showClouds) implements CustomPayload {
        public static final Id<SkyDisplayChangePayload> ID = new Id<>(INTERDIM_ServerPackets.SKY_DISPLAY_CHANGE_PACKET);
        public static final PacketCodec<RegistryByteBuf, SkyDisplayChangePayload> CODEC = PacketCodec.tuple(
                PacketCodecs.FLOAT, SkyDisplayChangePayload::sunSize,
                PacketCodecs.FLOAT, SkyDisplayChangePayload::moonSize,
                PacketCodecs.FLOAT, SkyDisplayChangePayload::starsBrightness,
                PacketCodecs.BOOL, SkyDisplayChangePayload::showClouds,
                SkyDisplayChangePayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record SkyColorChangePayload(float colorR, float colorG, float colorB, boolean reset) implements CustomPayload {
        public static final Id<SkyColorChangePayload> ID = new Id<>(INTERDIM_ServerPackets.SKY_COLOR_CHANGE_PACKET);
        public static final PacketCodec<RegistryByteBuf, SkyColorChangePayload> CODEC = PacketCodec.tuple(
                PacketCodecs.FLOAT, SkyColorChangePayload::colorR,
                PacketCodecs.FLOAT, SkyColorChangePayload::colorG,
                PacketCodecs.FLOAT, SkyColorChangePayload::colorB,
                PacketCodecs.BOOL, SkyColorChangePayload::reset,
                SkyColorChangePayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
