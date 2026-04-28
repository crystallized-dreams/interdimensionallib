package ru.alexalabai.interdimensionallib.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.client.ClientRendererConfig;

@Environment(EnvType.CLIENT)
@Mixin(value = BossBarHud.class, priority = 996699)
public class BossBarHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void render$dimlib(DrawContext context, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderBossbars) info.cancel();
    }
}
