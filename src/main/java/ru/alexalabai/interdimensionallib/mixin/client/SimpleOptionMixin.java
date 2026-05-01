package ru.alexalabai.interdimensionallib.mixin.client;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.client.OptionLocker;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> {
    @Shadow T value;

    @Inject(method="<init>(Ljava/lang/String;Lnet/minecraft/client/option/SimpleOption$TooltipFactory;Lnet/minecraft/client/option/SimpleOption$ValueTextGetter;Lnet/minecraft/client/option/SimpleOption$Callbacks;Lcom/mojang/serialization/Codec;Ljava/lang/Object;Ljava/util/function/Consumer;)V", at=@At("TAIL"))
    public void addOption$interdim(String key, SimpleOption.TooltipFactory tooltipFactory, SimpleOption.ValueTextGetter valueTextGetter, SimpleOption.Callbacks callbacks, Codec codec, Object defaultValue, Consumer changeCallback, CallbackInfo ci) {
        OptionLocker.addOption((SimpleOption<?>)(Object)this,key);
        OptionLocker.valueOf(key).ifPresent(v->value=(T)v);
    }
    @ModifyVariable(method="setValue", at=@At("HEAD"), argsOnly=true)
    public T setValue$interdim(T original) {
        var value=OptionLocker.valueOf(OptionLocker.getOptionName((SimpleOption<?>)(Object)this));
        return value.isPresent()?(T)value.get():original;
    }
}
