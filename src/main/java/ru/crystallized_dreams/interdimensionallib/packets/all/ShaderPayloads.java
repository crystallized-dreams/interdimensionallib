package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

public class ShaderPayloads {
    public record SetActive(String name, boolean active) implements CustomPayload {
        public static final Id<ShaderPayloads.SetActive> ID = new Id<>(INTERDIM_ServerPackets.SHADER_ACTIVITY_PACKET);
        public static final PacketCodec<RegistryByteBuf, ShaderPayloads.SetActive> CODEC = PacketCodec.tuple(
                PacketCodecs.STRING, ShaderPayloads.SetActive::name,
                PacketCodecs.BOOL, ShaderPayloads.SetActive::active,
                ShaderPayloads.SetActive::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
