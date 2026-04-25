package ru.alexalabai.interdimensionallib.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib.common.ClientGameOptions;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
@Mixin(ControlsOptionsScreen.class)
abstract class ControlsOptionsScreenMixin extends GameOptionsScreen {
    public ControlsOptionsScreenMixin() {
        super(null, null, null);
    }

    @Unique
    SimpleOption<?>[] options={
            ClientGameOptions.crawlToggle/*, ClientGameOptions.sitToggle*/
    };

    @Inject(method="addOptions",at=@At("TAIL"))
    private void kc$addAdditionalControls(CallbackInfo info) {
        this.body.addAll(options);
    }
}
