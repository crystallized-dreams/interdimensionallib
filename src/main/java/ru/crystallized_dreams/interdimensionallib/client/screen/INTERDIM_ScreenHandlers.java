package ru.crystallized_dreams.interdimensionallib.client.screen;

import net.minecraft.client.MinecraftClient;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;

public class INTERDIM_ScreenHandlers {
    public static void openPlushieRenameScreen(PlushieData plushieData) {
        MinecraftClient.getInstance().setScreen(new PlushieRenameScreen(plushieData));
    }
}
