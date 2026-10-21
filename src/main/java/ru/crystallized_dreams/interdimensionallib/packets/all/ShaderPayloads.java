package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

import java.util.List;

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
    public record SetBoolean(String name, String uniform, boolean value) implements CustomPayload {
        public static final Id<ShaderPayloads.SetBoolean> ID = new Id<>(INTERDIM_ServerPackets.SHADER_SET_BOOL_PACKET);
        public static final PacketCodec<RegistryByteBuf, ShaderPayloads.SetBoolean> CODEC = PacketCodec.tuple(
                PacketCodecs.STRING, ShaderPayloads.SetBoolean::name,
                PacketCodecs.STRING, ShaderPayloads.SetBoolean::uniform,
                PacketCodecs.BOOL, ShaderPayloads.SetBoolean::value,
                ShaderPayloads.SetBoolean::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record SetInteger(String name, String uniform, int value) implements CustomPayload {
        public static final Id<ShaderPayloads.SetInteger> ID = new Id<>(INTERDIM_ServerPackets.SHADER_SET_INT_PACKET);
        public static final PacketCodec<RegistryByteBuf, ShaderPayloads.SetInteger> CODEC = PacketCodec.tuple(
                PacketCodecs.STRING, ShaderPayloads.SetInteger::name,
                PacketCodecs.STRING, ShaderPayloads.SetInteger::uniform,
                PacketCodecs.INTEGER, ShaderPayloads.SetInteger::value,
                ShaderPayloads.SetInteger::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record SetFloat(String name, String uniform, float value) implements CustomPayload {
        public static final Id<ShaderPayloads.SetFloat> ID = new Id<>(INTERDIM_ServerPackets.SHADER_SET_FLOAT_PACKET);
        public static final PacketCodec<RegistryByteBuf, ShaderPayloads.SetFloat> CODEC = PacketCodec.tuple(
                PacketCodecs.STRING, ShaderPayloads.SetFloat::name,
                PacketCodecs.STRING, ShaderPayloads.SetFloat::uniform,
                PacketCodecs.FLOAT, ShaderPayloads.SetFloat::value,
                ShaderPayloads.SetFloat::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record ClientShaderRequest() implements CustomPayload {
        public static final Id<ShaderPayloads.ClientShaderRequest> ID = new Id<>(INTERDIM_ServerPackets.SHADER_CLIENT_REQUEST_PACKET);
        public static final PacketCodec<RegistryByteBuf, ShaderPayloads.ClientShaderRequest> CODEC = PacketCodec.unit(new ClientShaderRequest());

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record ClientShaderResponse(List<String> names) implements CustomPayload {
        public static final Id<ShaderPayloads.ClientShaderResponse> ID = new Id<>(INTERDIM_ServerPackets.SHADER_CLIENT_RESPONSE_PACKET);
        public static final PacketCodec<RegistryByteBuf, ShaderPayloads.ClientShaderResponse> CODEC = PacketCodec.tuple(
                PacketCodecs.STRING.collect(PacketCodecs.toList()), ShaderPayloads.ClientShaderResponse::names,
                ShaderPayloads.ClientShaderResponse::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
