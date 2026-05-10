package ru.crystallized_dreams.interdimensionallib.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.widget.OptionListWidget;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import ru.crystallized_dreams.interdimensionallib.client.OptionLocker;

@Environment(EnvType.CLIENT)
@Mixin(OptionListWidget.OptionWidgetEntry.class)
public class OptionWidgetEntryMixin {
    @WrapOperation(method="create(Lnet/minecraft/client/option/GameOptions;Lnet/minecraft/client/option/SimpleOption;Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;)Lnet/minecraft/client/gui/widget/OptionListWidget$OptionWidgetEntry;", at=@At(value="INVOKE", target="Lnet/minecraft/client/option/SimpleOption;createWidget(Lnet/minecraft/client/option/GameOptions;III)Lnet/minecraft/client/gui/widget/ClickableWidget;"))
    private static ClickableWidget deactivateLocked$interdim(SimpleOption<?> instance, GameOptions options, int x, int y, int width, Operation<ClickableWidget> original) {
        return widget$interdim(instance, original.call(instance, options, x, y, width));
    }
    @WrapOperation(method="create(Lnet/minecraft/client/option/GameOptions;Lnet/minecraft/client/option/SimpleOption;Lnet/minecraft/client/option/SimpleOption;Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;)Lnet/minecraft/client/gui/widget/OptionListWidget$OptionWidgetEntry;", at=@At(value="INVOKE", target="Lnet/minecraft/client/option/SimpleOption;createWidget(Lnet/minecraft/client/option/GameOptions;)Lnet/minecraft/client/gui/widget/ClickableWidget;"))
    private static ClickableWidget deactivateLocked$interdim(SimpleOption<?> instance, GameOptions options, Operation<ClickableWidget> original) {
        return widget$interdim(instance, original.call(instance, options));
    }
    @Unique
    private static ClickableWidget widget$interdim(SimpleOption<?> instance, ClickableWidget widget) {
        if(OptionLocker.contains(instance)) {
            widget.active=false;
            widget.setTooltip(Tooltip.of(Text.translatable("text.interdimensionallib.option_locked")));
        }
        return widget;
    }
}
