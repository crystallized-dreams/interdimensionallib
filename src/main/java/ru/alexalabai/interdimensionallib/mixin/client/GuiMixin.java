package ru.alexalabai.interdimensionallib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.alexalabai.interdimensionallib.client.ClientRendererConfig;

@Environment(EnvType.CLIENT)
@Mixin(value = InGameHud.class, priority = 996699)
public class GuiMixin {
    @Inject(method = "renderMiscOverlays", at = @At("HEAD"), cancellable = true)
    void renderMiscOverlays$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderMisc) info.cancel();
    }
    @Inject(method = "renderOverlayMessage", at = @At("HEAD"), cancellable = true)
    void renderOverlayMessage$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderOverlayMessage) info.cancel();
    }
    @Inject(method = "renderTitleAndSubtitle", at = @At("HEAD"), cancellable = true)
    void renderTitleAndSubtitle$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderTitleAndSubtitle) info.cancel();
    }
    @Inject(method = "renderChat", at = @At("HEAD"), cancellable = true)
    void renderChat$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderChat) info.cancel();
    }
    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    void renderScoreboardSidebar$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderScoreboardSidebar) info.cancel();
    }
    @Inject(method = "renderPlayerList", at = @At("HEAD"), cancellable = true)
    void renderPlayerList$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderPlayerList) info.cancel();
    }
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    void renderCrosshair$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderCrosshair) info.cancel();
    }
    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    void renderStatusEffectOverlay$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderStatusEffectOverlay) info.cancel();
    }
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    void renderHotbar$dimlim(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderHotbar) info.cancel();
    }
    @Inject(method = "shouldRenderExperience", at = @At("HEAD"), cancellable = true)
    void shouldRenderExperience$dimlim(CallbackInfoReturnable<Boolean> info) {
        if(!ClientRendererConfig.Gui.shouldRenderExperienceBar) info.setReturnValue(false);
    }
    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    void renderHeldItemTooltip$dimlim(DrawContext context, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderItemText) info.cancel();
    }
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void renderArmor$dimlim(DrawContext context, PlayerEntity player, int i, int j, int k, int x, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderArmor) info.cancel();
    }
    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    void renderHealthBar$dimlim(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderHealth) info.cancel();
    }
    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    void renderFood$dimlim(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderFood) info.cancel();
    }
    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    void renderMountHealth$dimlim(DrawContext context, CallbackInfo info) {
        if(!ClientRendererConfig.Gui.shouldRenderMountHealth) info.cancel();
    }
    @ModifyReturnValue(
            method = "renderStatusBars",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"
            )
    )
    private boolean modifyIsSubmergedInWater(boolean original) {
        if(!ClientRendererConfig.Gui.shouldRenderAir) return false;
        return original;
    }
}
