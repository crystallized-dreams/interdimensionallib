package ru.crystallized_dreams.interdimensionallib.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.packets.all.*;

public class INTERDIM_ServerPackets {
    public static final Identifier GRIP_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "grip");
    public static final Identifier SCREENSHAKE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "screenshake");
    public static final Identifier GUI_DISPLAY_CHANGE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "gui_display_change");
    public static final Identifier FOG_DISPLAY_CHANGE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "fog_display_change");
    public static final Identifier SKY_COLOR_CHANGE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "sky_color_change");
    public static final Identifier SKY_DISPLAY_CHANGE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "sky_display_change");
    public static final Identifier CAMERA_RESET_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_reset");
    public static final Identifier CAMERA_FADE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_fade");
    public static final Identifier CAMERA_DETACH_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_detach");
    public static final Identifier CAMERA_ATTACH_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_attach");
    public static final Identifier CAMERA_FOV_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_fov");
    public static final Identifier CAMERA_POSITION_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_position");
    public static final Identifier CAMERA_ROTATION_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_rotation");
    public static void regAll() {
        PayloadTypeRegistry.playC2S().register(GRIP.ID, GRIP.CODEC);
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered client packet payloads (C2S)");
        PayloadTypeRegistry.playS2C().register(ScreenShakePayload.ID, ScreenShakePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GuiDisplayChangePayload.ID, GuiDisplayChangePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FogDisplayChangePayload.ID, FogDisplayChangePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SkyPackets.SkyColorChangePayload.ID, SkyPackets.SkyColorChangePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SkyPackets.SkyDisplayChangePayload.ID, SkyPackets.SkyDisplayChangePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPackets.CameraResetPayload.ID, CameraPackets.CameraResetPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPackets.CameraFadePayload.ID, CameraPackets.CameraFadePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPackets.CameraAttachPayload.ID, CameraPackets.CameraAttachPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPackets.CameraDetachPayload.ID, CameraPackets.CameraDetachPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPackets.CameraFovPayload.ID, CameraPackets.CameraFovPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPackets.CameraPositionPayload.ID, CameraPackets.CameraPositionPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPackets.CameraRotationPayload.ID, CameraPackets.CameraRotationPayload.CODEC);
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered client packet payloads (S2C)");
        ServerPlayNetworking.registerGlobalReceiver(GRIP.ID, (payload, ctx)-> {
            InterdimensionalLib.LOGGER.info("[INTERDIM]: Client tried to request data by using GRIP");
        });
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered server packet receivers");
    }
}
