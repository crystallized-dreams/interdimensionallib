package ru.crystallized_dreams.interdimensionallib.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.crystallized_dreams.interdimensionallib.common.types.Easing;

@Environment(EnvType.CLIENT)
public class ClientCameraState {
    public static final ClientCameraState INSTANCE=new ClientCameraState();

    private boolean posOverride=false;
    private Vec3d posStart=Vec3d.ZERO;
    private Vec3d posCur=Vec3d.ZERO;
    private Vec3d posEnd=Vec3d.ZERO;
    private float posDuration=0;
    private float posElapsed=0;
    private Easing posEasing=Easing.LINEAR;
    private boolean rotOverride=false;
    private boolean rotFixed=false;
    private Vec3d rotStart=Vec3d.ZERO;
    private Vec3d rotCur=Vec3d.ZERO;
    private Vec3d rotEnd=Vec3d.ZERO;
    private float rotDuration=0f;
    private float rotElapsed=0f;
    private Easing rotEasing=Easing.LINEAR;
    private boolean fovOverride=false;
    private float fovValue=70f;
    private boolean hasTarget=false;
    private int targetEntityId=-1;
    private boolean fading=false;
    private float fadeR, fadeG, fadeB;
    private float fadeDuration=0f;
    private float fadeElapsed=0f;
    private Easing fadeEasing=Easing.LINEAR;
    private boolean fadeIn=true;
    private float currentRoll=0f;
    public void reset() {
        posOverride=false;
        rotOverride=false;
        rotFixed=false;
        fovOverride=false;
        hasTarget=false;
        targetEntityId=-1;
        fading=false;
        currentRoll=0f;
        fovValue=70f;
    }
    public void startFade(float r, float g, float b, float time, Easing easing) {
        fadeR=r;
        fadeG=g;
        fadeB=b;
        fadeDuration=time;
        fadeElapsed=0f;
        fadeEasing=easing;
        fading=true;
        fadeIn=true;
    }
    public void attachTarget(int entityId) {
        hasTarget=true;
        targetEntityId=entityId;
    }
    public void detachTarget() {
        hasTarget=false;
        targetEntityId=-1;
    }
    public void setFov(float fov) {
        fovOverride=true;
        fovValue=fov;
    }
    public void setPosition(double x, double y, double z, float time, Easing easing) {
        MinecraftClient client=MinecraftClient.getInstance();
        Camera camera=client.gameRenderer.getCamera();

        posStart=camera.getPos();
        posEnd=new Vec3d(x, y, z);
        posDuration=time;
        posElapsed=0f;
        posEasing=easing;
        posOverride =true;

        if(time<=0) {
            posCur=posEnd;
            posElapsed=posDuration;
        }
    }
    public void setRotation(float pitch, float yaw, float roll, boolean fixed, float time, Easing easing) {
        MinecraftClient client=MinecraftClient.getInstance();
        Camera camera=client.gameRenderer.getCamera();

        rotStart=new Vec3d(camera.getPitch(), camera.getYaw(), currentRoll);
        rotEnd=new Vec3d(pitch, yaw, roll);
        rotFixed=fixed;
        rotDuration=time;
        rotElapsed=0f;
        rotEasing=easing;
        rotOverride=true;

        if(time<=0) {
            rotCur=rotEnd;
            currentRoll=roll;
            rotElapsed=rotDuration;
        }
    }

    public void tick(float deltaTime) {
        if (posOverride && posDuration > 0 && posElapsed < posDuration) {
            posElapsed=Math.min(posElapsed + deltaTime, posDuration);
            double t=posEasing.apply(posElapsed / posDuration);
            posCur=new Vec3d(
                    MathHelper.lerp(t, posStart.x, posEnd.x),
                    MathHelper.lerp(t, posStart.y, posEnd.y),
                    MathHelper.lerp(t, posStart.z, posEnd.z)
            );
        }
        if(rotOverride && rotDuration > 0 && rotElapsed < rotDuration) {
            rotElapsed=Math.min(rotElapsed + deltaTime, rotDuration);
            double t=rotEasing.apply(rotElapsed / rotDuration);
            rotCur=new Vec3d(
                    MathHelper.lerp(t, rotStart.x, rotEnd.x),
                    MathHelper.lerp(t, rotStart.y, rotEnd.y),
                    MathHelper.lerp(t, rotStart.z, rotEnd.z)
            );
            currentRoll=(float) rotCur.z;
        }
        if (fading && fadeDuration > 0) {
            fadeElapsed=Math.min(fadeElapsed + deltaTime, fadeDuration);
            if (fadeElapsed >= fadeDuration && fadeIn) {
                fadeIn=false;
                fadeElapsed=0f;
            } else if (fadeElapsed >= fadeDuration && !fadeIn) fading=false;
        }
    }

    public boolean hasPositionOverride() { return posOverride; }
    public Vec3d getCurrentPosition() { return posCur; }

    public boolean hasRotationOverride() { return rotOverride; }
    public boolean isRotFixed() { return rotFixed; }
    public Vec3d getCurrentRotation() { return rotCur; }
    public float getCurrentRoll() { return currentRoll; }

    public boolean hasFovOverride() { return fovOverride; }
    public float getFovValue() { return fovValue; }

    public boolean hasTarget() { return hasTarget; }
    public int getTargetEntityId() { return targetEntityId; }

    public boolean isFading() { return fading; }
    public float getFadeA() {
        if(!fading || fadeDuration <= 0) return 0f;
        double progress=fadeEasing.apply(fadeElapsed / fadeDuration);
        return fadeIn?(float) progress:(float)(1.0-progress);
    }
    public float getFadeR() { return fadeR; }
    public float getFadeG() { return fadeG; }
    public float getFadeB() { return fadeB; }
}
