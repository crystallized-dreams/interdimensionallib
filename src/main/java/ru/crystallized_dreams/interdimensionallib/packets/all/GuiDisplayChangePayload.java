package ru.crystallized_dreams.interdimensionallib.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ServerPackets;

public record GuiDisplayChangePayload(int id, boolean state) implements CustomPayload {
    public static final Id<GuiDisplayChangePayload> ID = new Id<>(INTERDIM_ServerPackets.GUI_DISPLAY_CHANGE_PACKET);
    public static final PacketCodec<RegistryByteBuf, GuiDisplayChangePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, GuiDisplayChangePayload::id,
            PacketCodecs.BOOL, GuiDisplayChangePayload::state,
            GuiDisplayChangePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
