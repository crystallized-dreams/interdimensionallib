package ru.alexalabai.interdimensionallib.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.common.CustomBiomeRegistry;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public class VanillaBiomeParametersMixin {
    @Inject(method="writeCaveBiomeParameters", at=@At("TAIL"))
    private void injectCaveBiomes$kc(
            Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters,
            MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity,
            MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion,
            MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome, CallbackInfo ci
    ) {
        CustomBiomeRegistry.injectAll(parameters);
    }
}
