package ru.alexalabai.interdimensionallib.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import ru.alexalabai.interdimensionallib.client.ClientRendererConfig;
import ru.alexalabai.interdimensionallib.client.ScreenShakeHandler;
import ru.alexalabai.interdimensionallib.packets.all.GuiDisplayChangePayload;
import ru.alexalabai.interdimensionallib.packets.all.ScreenShakePayload;

public class INTERDIM_ClientPackets {
    public static void regAll() {
        ClientPlayNetworking.registerGlobalReceiver(ScreenShakePayload.ID, (payload, ctx)->
                ctx.client().execute(()-> ScreenShakeHandler.start(payload.intensity(), payload.duration(), payload.rate(), payload.overwrite(),true)));
        ClientPlayNetworking.registerGlobalReceiver(GuiDisplayChangePayload.ID, (payload, ctx)->
                ctx.client().execute(()->{
                    /*switch (payload.id()) {
                        case 0:
                            ClientRendererConfig.Gui.
                    }*/
                }));
    }
}
