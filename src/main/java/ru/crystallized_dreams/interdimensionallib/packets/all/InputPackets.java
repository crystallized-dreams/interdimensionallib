package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

public class InputPackets {
    public record LockInputs(boolean unlock) implements CustomPayload {
        public static final Id<InputPackets.LockInputs> ID = new Id<>(INTERDIM_ServerPackets.INPUT_LOCK_PACKET);
        public static final PacketCodec<RegistryByteBuf, InputPackets.LockInputs> CODEC = PacketCodec.tuple(
                PacketCodecs.BOOL, InputPackets.LockInputs::unlock,
                InputPackets.LockInputs::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
