package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

import java.util.List;

public record UpdateCommandExecutorPayload(List<String> commands, int type) implements CustomPayload {
    public static final Id<UpdateCommandExecutorPayload> ID = new Id<>(INTERDIM_ServerPackets.UPDATE_COMMAND_EXECUTOR_PACKET);
    public static final PacketCodec<RegistryByteBuf, UpdateCommandExecutorPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING.collect(PacketCodecs.toList()), UpdateCommandExecutorPayload::commands,
            PacketCodecs.INTEGER, UpdateCommandExecutorPayload::type,
            UpdateCommandExecutorPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
