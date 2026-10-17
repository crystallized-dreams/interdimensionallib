package ru.crystallized_dreams.interdimensionallib.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

public class INTERDIM_Particles {
    public static final SimpleParticleType BARRIER_PANE_EMISSION = FabricParticleTypes.simple();

    public static void regAll() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(InterdimensionalLib.MOD_ID,"barrier_pane_emission"), BARRIER_PANE_EMISSION);
    }

    @Environment(EnvType.CLIENT)
    public static void regAllRenderers() {
        ParticleFactoryRegistry.getInstance().register(BARRIER_PANE_EMISSION, StaticBillboardParticle.Factory::new);
    }
}
