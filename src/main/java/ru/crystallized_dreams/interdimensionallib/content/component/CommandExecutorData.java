package ru.crystallized_dreams.interdimensionallib.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record CommandExecutorData(List<String> commands, int controllerType, int pressed) {
    public static final Codec<CommandExecutorData> CODEC = RecordCodecBuilder.create(i->
            i.group(
                    Codec.STRING.listOf().fieldOf("commands").forGetter(CommandExecutorData::commands),
                    Codec.INT.fieldOf("controller_type").forGetter(CommandExecutorData::controllerType),
                    Codec.INT.fieldOf("pressed").forGetter(CommandExecutorData::pressed)
            ).apply(i, CommandExecutorData::new));
    public static final PacketCodec<RegistryByteBuf, CommandExecutorData> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING.collect(PacketCodecs.toList()), CommandExecutorData::commands,
                    PacketCodecs.INTEGER, CommandExecutorData::controllerType,
                    PacketCodecs.INTEGER, CommandExecutorData::pressed,
                    CommandExecutorData::new
            );

    public static final CommandExecutorData DEFAULT = new CommandExecutorData(new ArrayList<>(Collections.singleton("")), 0, 0);
}
