package ru.crystallized_dreams.interdimensionallib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.crystallized_dreams.interdimensionallib.client.render.ClientCameraState;
import ru.crystallized_dreams.interdimensionallib.client.render.ClientRendererConfig;
import ru.crystallized_dreams.interdimensionallib.client.render.ScreenShakeHandler;
import ru.crystallized_dreams.interdimensionallib.client.render.shader.ShaderRegistry;
import ru.crystallized_dreams.interdimensionallib.client.render.shader.ShaderReloadListener;
import ru.crystallized_dreams.interdimensionallib.common.INTERDIM_Particles;
import ru.crystallized_dreams.interdimensionallib.config.INTERDIM_ClientModConfig;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_Blocks;
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
        ClientTickEvents.END_CLIENT_TICK.register(client->{
            if(client.world == null || client.player == null) return;
            PlayerEntity player = client.player;
            World world = player.getWorld();

            if(client.world.getTime() % 5 == 0) {
                if(player.getMainHandStack().isOf(INTERDIM_Blocks.BARRIER_PANE.asItem()) ||
                        player.getOffHandStack().isOf(INTERDIM_Blocks.BARRIER_PANE.asItem())) {
                    BlockPos playerPos = player.getBlockPos();
                    int radius=8;

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                BlockPos pos = playerPos.add(x, y, z);

                                if (world.getBlockState(pos).isOf(INTERDIM_Blocks.BARRIER_PANE))
                                    world.addParticle(
                                            INTERDIM_Particles.BARRIER_PANE_EMISSION,
                                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                            0.0, 0.0, 0.0
                                    );
                            }
                        }
                    }
                }
            }
        });
        ShaderRegistry.load();
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new ShaderReloadListener());
        INTERDIM_Blocks.regAllRenderers();
    }
}
