package ru.crystallized_dreams.interdimensionallib.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientInputManager {
    public static final ClientInputManager INSTANCE=new ClientInputManager();

    private boolean allKeyBindsLocked=false;
    private boolean allKeyBindsLockedTemp=false;
    public List<KeyBinding> allowedKeyBinds=new ArrayList<>();

    public void reset() {
        allKeyBindsLocked=false;
        allKeyBindsLockedTemp=false;
        allowedKeyBinds=new ArrayList<>();
    }

    public void toggleKeyBindsLock() {
        allKeyBindsLockedTemp=!allKeyBindsLockedTemp;
    }
    public void setKeyBindsLock(boolean value) {
        allKeyBindsLocked=value;
        allKeyBindsLockedTemp=value;
    }
    public void lockKeyBinds() {
        setKeyBindsLock(true);
    }
    public void unlockKeyBinds() {
        setKeyBindsLock(false);
    }

    public boolean areKeybindsLocked() {
        return allKeyBindsLockedTemp;
    }
}
