package ru.alexalabai.interdimensionallib.client;

import net.fabricmc.api.ClientModInitializer;
import ru.alexalabai.interdimensionallib.packets.INTERDIM_ClientPackets;

public class InterdimensionalLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        INTERDIM_ClientPackets.regAll();
    }
}
