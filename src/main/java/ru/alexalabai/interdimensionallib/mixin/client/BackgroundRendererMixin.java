package ru.alexalabai.interdimensionallib.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.client.ClientRendererConfig;
import ru.alexalabai.interdimensionallib.client.FogAccessor;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(method="applyFog",at=@At("TAIL"))
    private static void applyFog$interdim(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        if(!ClientRendererConfig.overrideFog) return;
        //FogAccessor.FogData fogData=new FogAccessor.FogData(fogType);
        FogAccessor.FogData fogData=ClientRendererConfig.FOG;
        RenderSystem.setShaderFogStart(fogData.fogStart);
        RenderSystem.setShaderFogEnd(fogData.fogEnd);
        RenderSystem.setShaderFogShape(fogData.fogShape);
    }
}
