package ru.alexalabai.interdimensionallib.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;
import ru.alexalabai.interdimensionallib.packets.all.*;

public class INTERDIM_ServerPackets {
    public static final Identifier GRIP_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "grip");
    public static final Identifier SCREENSHAKE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "screenshake");
    public static final Identifier GUI_DISPLAY_CHANGE_PACKET = Identifier.of(InterdimensionalLib.MOD_ID, "gui_display_change");
    public static void regAll() {
        PayloadTypeRegistry.playC2S().register(GRIP.ID, GRIP.CODEC);
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered client packet payloads (C2S)");
        PayloadTypeRegistry.playS2C().register(ScreenShakePayload.ID, ScreenShakePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GuiDisplayChangePayload.ID, GuiDisplayChangePayload.CODEC);
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered client packet payloads (S2C)");
        ServerPlayNetworking.registerGlobalReceiver(GRIP.ID, (payload, ctx)-> {
            InterdimensionalLib.LOGGER.info("[INTERDIM]: Client tried to request data by using GRIP");
        });
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered server packet receivers");
    }
}
