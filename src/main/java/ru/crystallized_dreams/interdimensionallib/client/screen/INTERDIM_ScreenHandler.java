package ru.crystallized_dreams.interdimensionallib.client.screen;

import net.minecraft.client.MinecraftClient;
import ru.crystallized_dreams.interdimensionallib.content.component.CommandExecutorData;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;

public class INTERDIM_ScreenHandler {
    public static void openPlushieRenameScreen(PlushieData data) {
        MinecraftClient.getInstance().setScreen(new PlushieRenameScreen(data));
    }
    public static void openCommandExecutorEditorScreen(CommandExecutorData data) {
        MinecraftClient.getInstance().setScreen(new EditCommandExecutorScreen(data));
    }
}
