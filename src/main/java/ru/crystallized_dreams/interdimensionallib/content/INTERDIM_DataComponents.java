package ru.crystallized_dreams.interdimensionallib.content;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class INTERDIM_DataComponents {
    public static final ComponentType<String> PLUSHIE_DATA=ComponentType.<String>builder().codec(Codec.STRING).build();
    public static void regAll() {}
}
