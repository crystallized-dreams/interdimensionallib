package ru.alexalabai.interdimensionallib.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.client.ClientRendererConfig;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(method="render", at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", shift=At.Shift.AFTER))
    private static void modifyFogColor$interdim(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo info) {
        if(!ClientRendererConfig.Sky.overrideColor) return;
        RenderSystem.clearColor(
                (float) ClientRendererConfig.Sky.color.x,
                (float) ClientRendererConfig.Sky.color.y,
                (float) ClientRendererConfig.Sky.color.z,
                0
        );
    }
    @Inject(method="applyFogColor",at=@At("HEAD"), cancellable = true)
    private static void applyFogColor$interdim(CallbackInfo info) {
        if(!ClientRendererConfig.Sky.overrideColor) return;
        RenderSystem.setShaderFogColor(
                (float) ClientRendererConfig.Sky.color.x,
                (float) ClientRendererConfig.Sky.color.y,
                (float) ClientRendererConfig.Sky.color.z
        );
        info.cancel();
    }
    @Inject(method="applyFog",at=@At("TAIL"))
    private static void applyFog$interdim(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        //Either way modify clear color if needed.
        if(ClientRendererConfig.Sky.overrideColor)
            RenderSystem.setShaderFogColor(
                    (float) ClientRendererConfig.Sky.color.x,
                    (float) ClientRendererConfig.Sky.color.y,
                    (float) ClientRendererConfig.Sky.color.z
            );
        //Change fog data only if fog alteration is enabled.
        if(!ClientRendererConfig.Fog.override) return;
        float end=ClientRendererConfig.Fog.end;
        if(ClientRendererConfig.Fog.accountViewDistance) end*=viewDistance;
        RenderSystem.setShaderFogStart(ClientRendererConfig.Fog.start);
        RenderSystem.setShaderFogEnd(end);
        RenderSystem.setShaderFogShape(ClientRendererConfig.Fog.sphere?FogShape.SPHERE:FogShape.CYLINDER);
    }
}
