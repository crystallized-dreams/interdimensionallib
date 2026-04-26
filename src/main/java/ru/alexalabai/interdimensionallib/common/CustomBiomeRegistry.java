package ru.alexalabai.interdimensionallib.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.ParameterRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CustomBiomeRegistry {
    public static final ParameterRange TEMP_ICY=ParameterRange.of(-1.0F, -0.45F);
    public static final ParameterRange TEMP_COOL=ParameterRange.of(-0.45F, -0.15F);
    public static final ParameterRange TEMP_NEUTRAL=ParameterRange.of(-0.15F, 0.2F);
    public static final ParameterRange TEMP_WARM=ParameterRange.of(0.2F, 0.55F);
    public static final ParameterRange TEMP_HOT=ParameterRange.of(0.55F, 1.0F);

    public static final ParameterRange HUMIDITY_ARID=ParameterRange.of(-1.0F, -0.35F);
    public static final ParameterRange HUMIDITY_DRY=ParameterRange.of(-0.35F, -0.1F);
    public static final ParameterRange HUMIDITY_NEUTRAL=ParameterRange.of(-0.1F, 0.1F);
    public static final ParameterRange HUMIDITY_WET=ParameterRange.of(0.1F, 0.3F);
    public static final ParameterRange HUMIDITY_HUMID=ParameterRange.of(0.3F, 1.0F);

    public static final ParameterRange CONT_OCEAN=ParameterRange.of(-1.2F, -0.19F);
    public static final ParameterRange CONT_COAST=ParameterRange.of(-0.19F, -0.11F);
    public static final ParameterRange CONT_INLAND=ParameterRange.of(-0.11F, 1.0F);
    public static final ParameterRange CONT_FAR_INLAND=ParameterRange.of(0.3F, 1.0F);

    public static final ParameterRange DEPTH_SURFACE=ParameterRange.of(0.0F, 0.2F);
    public static final ParameterRange DEPTH_UNDERGROUND=ParameterRange.of(0.2F, 0.9F);
    public static final ParameterRange DEPTH_DEEP=ParameterRange.of(0.4F, 1.0F);
    public static final ParameterRange DEPTH_FULL_CAVES=ParameterRange.of(0.2F, 1.0F);

    public static final ParameterRange EROSION_LOW=ParameterRange.of(-1.0F, -0.375F);
    public static final ParameterRange EROSION_MID=ParameterRange.of(-0.375F, 0.45F);
    public static final ParameterRange EROSION_HIGH=ParameterRange.of(0.45F, 1.0F);

    public static final ParameterRange FULL_RANGE=ParameterRange.of(-1.0F, 1.0F);

    private static final List<CustomBiomeEntry> ENTRIES = new ArrayList<>();

    /**Full parameter registration (recommended).*/
    public static void register(
            ParameterRange temperature,
            ParameterRange humidity,
            ParameterRange continentalness,
            ParameterRange erosion,
            ParameterRange depth,
            ParameterRange weirdness,
            RegistryKey<Biome> biome
    ) {
        ENTRIES.add(new CustomBiomeEntry(
                temperature, humidity, continentalness,
                erosion, depth, weirdness, biome
        ));
    }
    /**Uses FULL_RANGE for erosion and weirdness, CONT_INLAND for continentalness.*/
    public static void register(ParameterRange temperature, ParameterRange humidity, ParameterRange depth, RegistryKey<Biome> biome) {
        register(temperature, humidity, CONT_INLAND, FULL_RANGE, depth, FULL_RANGE, biome);
    }
    /**Only temperature and humidity matter.*/
    public static void registerCave(
            ParameterRange temperature,
            ParameterRange humidity,
            RegistryKey<Biome> biome
    ) {
        register(temperature, humidity, FULL_RANGE, FULL_RANGE, DEPTH_FULL_CAVES, FULL_RANGE, biome);
    }
    public static void injectAll(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        for (CustomBiomeEntry entry : ENTRIES) {
            if (entry.biome() == null) continue;

            parameters.accept(Pair.of(
                    MultiNoiseUtil.createNoiseHypercube(
                            entry.temperature(),
                            entry.humidity(),
                            entry.continentalness(),
                            entry.erosion(),
                            entry.depth(),
                            entry.weirdness(),
                            0L
                    ),
                    entry.biome()
            ));
        }
    }

    public record CustomBiomeEntry(
            ParameterRange temperature,
            ParameterRange humidity,
            ParameterRange continentalness,
            ParameterRange erosion,
            ParameterRange depth,
            ParameterRange weirdness,
            RegistryKey<Biome> biome
    ) { }
}
