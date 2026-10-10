package ru.crystallized_dreams.interdimensionallib.mixin.client.campaign_ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.crystallized_dreams.interdimensionallib.config.INTERDIM_ClientModConfig;

import java.net.URI;
import java.net.URISyntaxException;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Shadow
    @Nullable
    private SplashTextRenderer splashText;
    @Shadow @Final
    private static Text COPYRIGHT;
    @Unique
    private static final Identifier LOGO_TEXTURE = Identifier.ofVanilla("textures/gui/title/minecraft.png");
    @Unique
    private static final Identifier EDITION_TEXTURE = Identifier.ofVanilla("textures/gui/title/edition.png");

    @Shadow @Nullable
    protected abstract Text getMultiplayerDisabledText();

    @Shadow
    private long backgroundFadeStart;
    @Shadow
    private boolean doBackgroundFade;
    @Shadow
    private float backgroundAlpha;

    @Shadow
    protected abstract void setWidgetAlpha(float alpha);

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo info) {
        if (client.isDemo() || !INTERDIM_ClientModConfig.INSTANCE.campaignTitleScreen) return;
        info.cancel();
        int yOffset = height - 18;
        int xOffset = 10;
        int buttonSizeX = 200;
        int buttonSizeY = 20;
        int spacingY = buttonSizeY + 4;
        // Splash.
        if (splashText == null) splashText = client.getSplashTextLoader().get();
        // Play. My. Game.
        addDrawableChild(ButtonWidget.builder(Text.translatable("menu.singleplayer"), (button) -> client.setScreen(new SelectWorldScreen(this)))
                .dimensions(xOffset, yOffset - 3*spacingY, buttonSizeX, buttonSizeY).build());
        Text text = getMultiplayerDisabledText();
        Tooltip tooltip = text != null ? Tooltip.of(text) : null;
        (addDrawableChild(ButtonWidget.builder(Text.translatable("menu.multiplayer"), (button) -> {
            Screen screen = (client.options.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this));
            client.setScreen(screen);
        }).dimensions(xOffset, yOffset - 2*spacingY, buttonSizeX, 20).tooltip(tooltip).build())).active = (text == null);
        // Options & exit.
        addDrawableChild(ButtonWidget.builder(Text.translatable("menu.options"), (button) ->
                client.setScreen(new OptionsScreen(this, client.options))).dimensions(xOffset, yOffset - spacingY, buttonSizeX, buttonSizeY).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("menu.quit"), (button) ->
                client.scheduleStop()).dimensions(xOffset, yOffset, buttonSizeX, buttonSizeY).build());
        // Accessibility & language.
        TextIconButtonWidget textIconButtonWidget = addDrawableChild(AccessibilityOnboardingButtons.createLanguageButton(20, (button) ->
                client.setScreen(new LanguageOptionsScreen(this, client.options, client.getLanguageManager())), true));
        textIconButtonWidget.setPosition(xOffset+buttonSizeX+4, yOffset-spacingY);
        TextIconButtonWidget textIconButtonWidget2 = addDrawableChild(AccessibilityOnboardingButtons.createAccessibilityButton(20, (button) ->
                client.setScreen(new AccessibilityOptionsScreen(this, client.options)), true));
        textIconButtonWidget2.setPosition(xOffset+buttonSizeX+4, yOffset);
        // Copyright.
        int copyrightWidth = textRenderer.getWidth(COPYRIGHT);
        int copyrightX = width - copyrightWidth - 2;
        int copyrightY = yOffset + 12;
        addDrawableChild(new PressableTextWidget(copyrightX, copyrightY, copyrightWidth, 10, COPYRIGHT, (button) ->
                client.setScreen(new CreditsAndAttributionScreen(this)), textRenderer));
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (client.isDemo() || !INTERDIM_ClientModConfig.INSTANCE.campaignTitleScreen) return;
        info.cancel();
        if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
        }

        float widgetAlpha = 1.0F;
        if (this.doBackgroundFade) {
            float time = (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 2000.0F;
            if (time > 1.0F) {
                this.doBackgroundFade = false;
                this.backgroundAlpha = 1.0F;
            } else {
                time = MathHelper.clamp(time, 0.0F, 1.0F);
                widgetAlpha = MathHelper.clampedMap(time, 0.5F, 1.0F, 0.0F, 1.0F);
                this.backgroundAlpha = MathHelper.clampedMap(time, 0.0F, 0.5F, 0.0F, 1.0F);
            }

            this.setWidgetAlpha(widgetAlpha);
        }

        this.renderPanoramaBackground(context, delta);
        int textAlpha = MathHelper.ceil(widgetAlpha * 255.0F) << 24;
        if ((textAlpha & -67108864) != 0) {
            super.render(context, mouseX, mouseY, delta);
            if (this.splashText != null && !(Boolean)this.client.options.getHideSplashTexts().getValue())
                this.splashText.render(context, this.width, this.textRenderer, textAlpha);

            drawLogo(context, 140, (int)(height*0.2f), 1.2f, widgetAlpha);
        }
    }

    @Unique
    void drawLogo(DrawContext context, int x, int y, float ratio, float alpha) {
        context.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        int logoX = x - 128;
        context.drawTexture(LOGO_TEXTURE, logoX, y, 0.0F, 0.0F, (int)(256*ratio), (int)(44*ratio), (int)(256*ratio), (int)(64*ratio));
        int editionX = x - 64;
        int editionY = y + (int)(44*ratio) - 7;
        context.drawTexture(EDITION_TEXTURE, editionX, editionY, 0.0F, 0.0F, (int)(128*ratio), (int)(14*ratio), (int)(128*ratio), (int)(16*ratio));
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
