package ru.crystallized_dreams.interdimensionallib.mixin.client.sodium;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.crystallized_dreams.interdimensionallib.client.OptionLocker;

@Environment(EnvType.CLIENT)
@IfModLoaded("sodium")
@Mixin(OptionImpl.class)
public abstract class OptionImplMixin {
    @Shadow public abstract Text getName();

    @Inject(method="isAvailable",at=@At("RETURN"),cancellable=true)
    void isAvailable$interdim(CallbackInfoReturnable<Boolean> info) {
        if(OptionLocker.contains(((TranslatableTextContent)getName().getContent()).getKey())) info.setReturnValue(false);
    }

    @Inject(method="getTooltip",at=@At("RETURN"),cancellable=true)
    void getTooltip$interdim(CallbackInfoReturnable<Text> info) {
        if(OptionLocker.contains(((TranslatableTextContent)getName().getContent()).getKey()))
            info.setReturnValue(Text.translatable("text.interdimensionallib.option_locked"));
    }
}
