package ru.crystallized_dreams.interdimensionallib.mixin.client;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.client.ClientInputManager;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Shadow
    private InputUtil.Key boundKey;

    @Inject(method="wasPressed", at=@At("RETURN"), cancellable=true)
    void wasPressed$interdim(CallbackInfoReturnable<Boolean> info) {
        if(!info.getReturnValue()) return;
        info.setReturnValue(isUnlocked$interdim());
    }
    @Inject(method="isPressed", at=@At("RETURN"), cancellable=true)
    void isPressed$interdim(CallbackInfoReturnable<Boolean> info) {
        if(!info.getReturnValue()) return;
        info.setReturnValue(isUnlocked$interdim());
    }

    @Unique
    boolean isUnlocked$interdim() {
        boolean slash=(boundKey.getCode()==GLFW.GLFW_KEY_SLASH);
        boolean escape=(boundKey.getCategory()==InputUtil.Type.KEYSYM && boundKey.getCode()==GLFW.GLFW_KEY_ESCAPE);

        if(escape) ClientInputManager.INSTANCE.toggleKeyBindsLock();
        else if(!slash) {
            if(!ClientInputManager.INSTANCE.areKeybindsLocked()) return true;
            KeyBinding keyBinding=(KeyBinding)(Object)this;
            if(!ClientInputManager.INSTANCE.allowedKeyBinds.contains(keyBinding)) {
                InterdimensionalLib.LOGGER.info("Pressed keybind is locked");
                return false;
            }
        }
        return true;
    }
}
