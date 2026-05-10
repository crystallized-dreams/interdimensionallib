package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

public class CameraPackets {
    public record CameraResetPayload() implements CustomPayload {
        public static final Id<CameraResetPayload> ID = new Id<>(INTERDIM_ServerPackets.CAMERA_RESET_PACKET);
        public static final PacketCodec<RegistryByteBuf, CameraResetPayload> CODEC = PacketCodec.unit(
                new CameraResetPayload()
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record CameraFadePayload(float colorR, float colorG, float colorB, float time, int easing) implements CustomPayload {
        public static final Id<CameraFadePayload> ID = new Id<>(INTERDIM_ServerPackets.CAMERA_FADE_PACKET);
        public static final PacketCodec<RegistryByteBuf, CameraFadePayload> CODEC = PacketCodec.tuple(
                PacketCodecs.FLOAT, CameraFadePayload::colorR,
                PacketCodecs.FLOAT, CameraFadePayload::colorG,
                PacketCodecs.FLOAT, CameraFadePayload::colorB,
                PacketCodecs.FLOAT, CameraFadePayload::time,
                PacketCodecs.INTEGER, CameraFadePayload::easing,
                CameraFadePayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record CameraAttachPayload(int id) implements CustomPayload {
        public static final Id<CameraAttachPayload> ID = new Id<>(INTERDIM_ServerPackets.CAMERA_ATTACH_PACKET);
        public static final PacketCodec<RegistryByteBuf, CameraAttachPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.INTEGER, CameraAttachPayload::id,
                CameraAttachPayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record CameraDetachPayload() implements CustomPayload {
        public static final Id<CameraDetachPayload> ID = new Id<>(INTERDIM_ServerPackets.CAMERA_DETACH_PACKET);
        public static final PacketCodec<RegistryByteBuf, CameraDetachPayload> CODEC = PacketCodec.unit(
                new CameraDetachPayload()
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record CameraFovPayload(float fov) implements CustomPayload {
        public static final Id<CameraFovPayload> ID = new Id<>(INTERDIM_ServerPackets.CAMERA_FOV_PACKET);
        public static final PacketCodec<RegistryByteBuf, CameraFovPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.FLOAT, CameraFovPayload::fov,
                CameraFovPayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record CameraPositionPayload(double x, double y, double z, float time, int easing) implements CustomPayload {
        public static final Id<CameraPositionPayload> ID = new Id<>(INTERDIM_ServerPackets.CAMERA_POSITION_PACKET);
        public static final PacketCodec<RegistryByteBuf, CameraPositionPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.DOUBLE, CameraPositionPayload::x,
                PacketCodecs.DOUBLE, CameraPositionPayload::y,
                PacketCodecs.DOUBLE, CameraPositionPayload::z,
                PacketCodecs.FLOAT, CameraPositionPayload::time,
                PacketCodecs.INTEGER, CameraPositionPayload::easing,
                CameraPositionPayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record CameraRotationPayload(float pitch, float yaw, float roll, boolean fixed, float time, int easing) implements CustomPayload {
        public static final Id<CameraRotationPayload> ID = new Id<>(INTERDIM_ServerPackets.CAMERA_ROTATION_PACKET);
        public static final PacketCodec<RegistryByteBuf, CameraRotationPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.FLOAT, CameraRotationPayload::pitch,
                PacketCodecs.FLOAT, CameraRotationPayload::yaw,
                PacketCodecs.FLOAT, CameraRotationPayload::roll,
                PacketCodecs.BOOL, CameraRotationPayload::fixed,
                PacketCodecs.FLOAT, CameraRotationPayload::time,
                PacketCodecs.INTEGER, CameraRotationPayload::easing,
                CameraRotationPayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
