package ru.crystallized_dreams.interdimensionallib.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.FogShape;

@Environment(EnvType.CLIENT)
public class FogAccessor {
    public static class FogData {
        public final BackgroundRenderer.FogType fogType;
        public float fogStart;
        public float fogEnd;
        public FogShape fogShape = FogShape.SPHERE;

        public FogData(BackgroundRenderer.FogType fogType) {
            this.fogType = fogType;
        }
    }
}
