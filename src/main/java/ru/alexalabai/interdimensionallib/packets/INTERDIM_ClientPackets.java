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
                    switch (payload.id()) {
                        case 0:
                            ClientRendererConfig.Gui.setAll(payload.state());
                            break;
                        case 1:
                            ClientRendererConfig.Gui.shouldRenderDebug=payload.state();
                            break;
                        case 2:
                            ClientRendererConfig.Gui.shouldRenderCrosshair=payload.state();
                            break;
                        case 3:
                            ClientRendererConfig.Gui.shouldRenderHotbar=payload.state();
                            break;
                        case 4:
                            ClientRendererConfig.Gui.shouldRenderItemText=payload.state();
                            break;
                        case 5:
                            ClientRendererConfig.Gui.shouldRenderOverlayMessage=payload.state();
                            break;
                        case 6:
                            ClientRendererConfig.Gui.shouldRenderTitleAndSubtitle=payload.state();
                            break;
                        case 7:
                            ClientRendererConfig.Gui.shouldRenderStatusEffectOverlay=payload.state();
                            break;
                        case 8:
                            ClientRendererConfig.Gui.shouldRenderExperienceBar=payload.state();
                            break;
                        case 9:
                            ClientRendererConfig.Gui.shouldRenderHealth=payload.state();
                            break;
                        case 10:
                            ClientRendererConfig.Gui.shouldRenderArmor=payload.state();
                            break;
                        case 11:
                            ClientRendererConfig.Gui.shouldRenderAir=payload.state();
                            break;
                        case 12:
                            ClientRendererConfig.Gui.shouldRenderMountHealth=payload.state();
                            break;
                        case 13:
                            ClientRendererConfig.Gui.shouldRenderFood=payload.state();
                            break;
                        case 14:
                            ClientRendererConfig.Gui.shouldRenderScoreboardSidebar=payload.state();
                            break;
                        case 15:
                            ClientRendererConfig.Gui.shouldRenderBossbars=payload.state();
                            break;
                        case 16:
                            ClientRendererConfig.Gui.shouldRenderChat=payload.state();
                            break;
                        case 17:
                            ClientRendererConfig.Gui.shouldRenderPlayerList=payload.state();
                            break;
                        case 18:
                            ClientRendererConfig.Gui.shouldRenderMisc=payload.state();
                            break;
                    }
                }));
    }
}
