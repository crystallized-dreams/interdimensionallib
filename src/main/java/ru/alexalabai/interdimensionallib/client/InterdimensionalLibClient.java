package ru.alexalabai.interdimensionallib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import ru.alexalabai.interdimensionallib.packets.INTERDIM_ClientPackets;

public class InterdimensionalLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        INTERDIM_ClientPackets.regAll();
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, client) -> {
            ClientRendererConfig.Gui.setAll(true);
            ClientRendererConfig.Fog.override=false;
            ClientRendererConfig.Sky.override=false;
        });
    }
}
