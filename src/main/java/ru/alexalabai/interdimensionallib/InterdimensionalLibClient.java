package ru.alexalabai.interdimensionallib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.alexalabai.interdimensionallib.common.ClientCommandHandler;
import ru.alexalabai.interdimensionallib.common.ClientGameOptions;
import ru.alexalabai.interdimensionallib.common.ScreenShakeHandler;
import ru.alexalabai.interdimensionallib.config.ModClientConfig;
import ru.alexalabai.interdimensionallib.entity.ModEntities;
import ru.alexalabai.interdimensionallib.packets.ModClientPackets;
import ru.alexalabai.interdimensionallib.packets.all.GRIP;
import ru.alexalabai.interdimensionallib.packets.all.MovementStatePayload;

public class InterdimensionalLibClient implements ClientModInitializer {
    public static boolean activelyCrawlingForceFromCommand = false;
    public static boolean activelyCrawling = false;
    public static boolean activelySitting = false;
    private static boolean activelyCrawlingJustChanged = false;
    private static boolean crawlKeyPressedLast=false;
    public static boolean isSittingAllowedOnServer=true;
    public static boolean isCrawlingAllowedOnServer=true;

    @Override
    public void onInitializeClient() {
        ModClientConfig.INSTANCE = ModClientConfig.load();
        InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Registered client config");
        ModClientPackets.regAll();
        ClientGameOptions.regAll();
        ClientGameOptions.crawlToggle.setValue(ModClientConfig.INSTANCE.crawlToggleable);
        ClientGameOptions.sitToggle.setValue(ModClientConfig.INSTANCE.sitOnBlockClick);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access)->ClientCommandHandler.regAll(dispatcher));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            ScreenShakeHandler.update();
            if (isCrawlingAllowedOnServer) {
                boolean crawlIsToggleable = ClientGameOptions.crawlToggle.getValue();

                if (activelyCrawlingJustChanged) {
                    activelySitting = false;
                    ClientPlayNetworking.send(new MovementStatePayload(activelyCrawling ? 2 : 0));
                    activelyCrawlingJustChanged = false;
                } else {
                    if (crawlIsToggleable) {
                        if (ClientGameOptions.crawlKey.wasPressed()) activelyCrawling = !activelyCrawling;
                    } else if (!activelyCrawlingForceFromCommand) {
                        if (ClientGameOptions.crawlKey.isPressed()) activelyCrawling = true;
                        else if (activelyCrawling) activelyCrawling = false;
                    }
                    if (crawlKeyPressedLast != activelyCrawling) activelyCrawlingJustChanged = true;
                    crawlKeyPressedLast = activelyCrawling;
                }
            } else if (ClientGameOptions.crawlKey.wasPressed()) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("text.interdimensionallib.crawling_disallowed"));
            }

            if(ClientGameOptions.sitKey.wasPressed()) {
                if(isSittingAllowedOnServer) {
                    activelySitting = !activelySitting;
                    activelyCrawling = false;
                    ClientPlayNetworking.send(new MovementStatePayload(activelySitting ? 1 : 0));
                } else MinecraftClient.getInstance().player.sendMessage(Text.translatable("text.interdimensionallib.sitting_disallowed"));
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client)-> {
            ClientPlayNetworking.send(new GRIP(1));
            ClientPlayNetworking.send(new GRIP(2));
            ClientPlayNetworking.send(new GRIP(3));
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client)->{
            activelyCrawling = false;
            activelySitting = false;
            activelyCrawlingJustChanged = false;
            crawlKeyPressedLast=false;
            isSittingAllowedOnServer=true;
            isCrawlingAllowedOnServer=true;
        });
        ModEntities.regRenderers();
    }
}
