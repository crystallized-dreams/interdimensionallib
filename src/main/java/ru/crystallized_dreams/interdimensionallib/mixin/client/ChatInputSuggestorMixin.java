package ru.crystallized_dreams.interdimensionallib.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.crystallized_dreams.interdimensionallib.client.acess.ChatInputSuggestorAccess;

import java.util.List;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin implements ChatInputSuggestorAccess {
    @Shadow
    private int x;
    @Shadow @Final
    private List<OrderedText> messages;
    @Shadow @Final
    TextRenderer textRenderer;
    @Shadow
    private int width;
    @Shadow @Final
    int color;

    @Unique
    int interdim$y=72;
    @Unique
    boolean interdim$overrideY=false;

    @Override
    public void interdim$setPos(int x, int y) {
        this.x=x;
        this.interdim$y=y;
    }

    @Override
    public void interdim$setPos(Vec2f pos) {
        this.x=(int)pos.x;
        this.interdim$y=(int)pos.y;
    }

    @Override
    public Vec2f interdim$getPos() {
        return new Vec2f(x,interdim$y);
    }

    @Override
    public void interdim$setYOverride(boolean override) {
        interdim$overrideY=override;
    }

    @Inject(method = "renderMessages", at = @At("HEAD"), cancellable = true)
    void renderMessages(DrawContext context, CallbackInfo info) {
        if(!interdim$overrideY) return;
        info.cancel();

        int i=0;
        for(OrderedText orderedText : this.messages) {
            int y = interdim$y+12*i;
            context.fill(this.x-1, y, this.x+this.width+1, y+12, this.color);
            context.drawTextWithShadow(this.textRenderer, orderedText, this.x, y+2, -1);
            ++i;
        }
    }
}
