package ru.alexalabai.interdimensionallib.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import ru.alexalabai.interdimensionallib.common.ScreenShakeHandler;
import ru.alexalabai.interdimensionallib.packets.all.ScreenShakePayload;

public class ModClientPackets {
    public static void regAll() {
        ClientPlayNetworking.registerGlobalReceiver(ScreenShakePayload.ID, (payload, ctx)->
                ctx.client().execute(()-> ScreenShakeHandler.start(payload.intensity(), payload.duration(), payload.rate(), payload.overwrite(),true)));
    }
}
