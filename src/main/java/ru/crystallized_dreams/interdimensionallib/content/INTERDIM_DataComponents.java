package ru.crystallized_dreams.interdimensionallib.content;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;

public class INTERDIM_DataComponents {
    public static final ComponentType<PlushieData> PLUSHIE_DATA= Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(InterdimensionalLib.MOD_ID, "plushie_data"),
            ComponentType.<PlushieData>builder().codec(PlushieData.CODEC).packetCodec(PlushieData.PACKET_CODEC).build()
    );
    public static void regAll() {}
}
