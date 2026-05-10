package ru.crystallized_dreams.interdimensionallib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ClientPackets;

public class InterdimensionalLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        INTERDIM_ClientPackets.regAll();
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, client)->ClientRendererConfig.reset());
        ClientLifecycleEvents.CLIENT_STARTED.register(client->OptionLocker.handleClientReady());
    }
}
