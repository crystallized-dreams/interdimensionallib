package ru.crystallized_dreams.interdimensionallib.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record PlushieData(String nickname, boolean wide) {
    public static final Codec<PlushieData> CODEC = RecordCodecBuilder.create(i->
            i.group(
                    Codec.STRING.fieldOf("player").forGetter(PlushieData::nickname),
                    Codec.BOOL.fieldOf("wide").forGetter(PlushieData::wide)
            ).apply(i,PlushieData::new));
    public static final PacketCodec<RegistryByteBuf, PlushieData> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING, PlushieData::nickname,
                    PacketCodecs.BOOL, PlushieData::wide,
                    PlushieData::new
            );

    public static final PlushieData DEFAULT = new PlushieData("Steve", true);
}
