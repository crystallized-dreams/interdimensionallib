package ru.crystallized_dreams.interdimensionallib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import ru.crystallized_dreams.interdimensionallib.client.render.ClientCameraState;
import ru.crystallized_dreams.interdimensionallib.client.render.ClientRendererConfig;
import ru.crystallized_dreams.interdimensionallib.client.render.ScreenShakeHandler;
import ru.crystallized_dreams.interdimensionallib.client.render.shader.ShaderRegistry;
import ru.crystallized_dreams.interdimensionallib.client.render.shader.ShaderReloadListener;
import ru.crystallized_dreams.interdimensionallib.config.INTERDIM_ClientModConfig;
import ru.crystallized_dreams.interdimensionallib.packets.INTERDIM_ClientPackets;

public class InterdimensionalLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        INTERDIM_ClientModConfig.INSTANCE=INTERDIM_ClientModConfig.load();
        INTERDIM_ClientPackets.regAll();
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, client)->{
            ClientRendererConfig.reset();
            ClientCameraState.INSTANCE.reset();
            ScreenShakeHandler.stop();
            ClientInputManager.INSTANCE.reset();
        });
        ClientLifecycleEvents.CLIENT_STARTED.register(client->OptionLocker.handleClientReady());
        ClientLifecycleEvents.CLIENT_STOPPING.register(client->{INTERDIM_ClientModConfig.INSTANCE.save();});
        ShaderRegistry.load();
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new ShaderReloadListener());
    }
}
