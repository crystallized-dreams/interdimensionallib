package ru.crystallized_dreams.interdimensionallib.client.screen;

import net.minecraft.client.MinecraftClient;

public class INTERDIM_ScreenHandlers {
    public static void openPlushieRenameScreen() {
        MinecraftClient.getInstance().setScreen(new PlushieRenameScreen());
    }
}
