package ru.crystallized_dreams.interdimensionallib.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.common.INTERDIM_ServerCommandHandler;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_DataComponents;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;
import ru.crystallized_dreams.interdimensionallib.content.item.PlushieBlockItem;
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
    public static final Identifier CAMERA_ANGLE_LIMIT_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "camera_angle_limit");
    public static final Identifier INPUT_LOCK_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "input_lock");
    public static final Identifier SHADER_ACTIVITY_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "shader_activity");
    public static final Identifier SHADER_SET_BOOL_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "shader_set_bool");
    public static final Identifier SHADER_SET_INT_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "shader_set_int");
    public static final Identifier SHADER_SET_FLOAT_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "shader_set_float");
    public static final Identifier SHADER_CLIENT_REQUEST_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "shader_client_request");
    public static final Identifier SHADER_CLIENT_RESPONSE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "shader_client_response");
    public static final Identifier UPDATE_PLUSHIE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "update_plushie");
    public static void regAll() {
        PayloadTypeRegistry.playC2S().register(GRIP.ID, GRIP.CODEC);
        PayloadTypeRegistry.playC2S().register(ShaderPayloads.ClientShaderResponse.ID, ShaderPayloads.ClientShaderResponse.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdatePlushiePayload.ID, UpdatePlushiePayload.CODEC);
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
        PayloadTypeRegistry.playS2C().register(InputPackets.LockInputs.ID, InputPackets.LockInputs.CODEC);
        PayloadTypeRegistry.playS2C().register(ShaderPayloads.SetActive.ID, ShaderPayloads.SetActive.CODEC);
        PayloadTypeRegistry.playS2C().register(ShaderPayloads.SetBoolean.ID, ShaderPayloads.SetBoolean.CODEC);
        PayloadTypeRegistry.playS2C().register(ShaderPayloads.SetInteger.ID, ShaderPayloads.SetInteger.CODEC);
        PayloadTypeRegistry.playS2C().register(ShaderPayloads.SetFloat.ID, ShaderPayloads.SetFloat.CODEC);
        PayloadTypeRegistry.playS2C().register(ShaderPayloads.ClientShaderRequest.ID, ShaderPayloads.ClientShaderRequest.CODEC);
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered client packet payloads (S2C)");
        ServerPlayNetworking.registerGlobalReceiver(GRIP.ID, (payload, ctx)-> {
            InterdimensionalLib.LOGGER.info("[INTERDIM]: Client tried to request data by using GRIP");
        });
        ServerPlayNetworking.registerGlobalReceiver(ShaderPayloads.ClientShaderResponse.ID, (payload, ctx)-> {
            for (var name : payload.names()) {
                if (!INTERDIM_ServerCommandHandler.SUGGESTIBLE_SHADERS.contains(name))
                    INTERDIM_ServerCommandHandler.SUGGESTIBLE_SHADERS.add(name);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(UpdatePlushiePayload.ID, (payload, ctx)->
            ctx.server().execute(() -> {
                var player = ctx.player();
                if (player == null) return;
                PlushieData newData = new PlushieData(payload.playerName(), payload.wide());
                ItemStack stack = player.getMainHandStack();
                if (stack.getItem() instanceof PlushieBlockItem) stack.set(INTERDIM_DataComponents.PLUSHIE_DATA, newData);
            })
        );
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered server packet receivers");
    }
}
