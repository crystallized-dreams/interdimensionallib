package ru.alexalabai.interdimensionallib.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class ClientCameraState {
    public static final ClientCameraState INSTANCE=new ClientCameraState();

    boolean positionOverride=false;
    boolean positionFixed=false;
    Vec3d posStart=Vec3d.ZERO;
    Vec3d posCur=Vec3d.ZERO;
    Vec3d posEnd=Vec3d.ZERO;
    float posDuration=0;
    float posElapsed=0;
}
