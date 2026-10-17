package ru.crystallized_dreams.interdimensionallib.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

public class PlushieRenameScreen extends Screen {
    private TextFieldWidget textfield;

    protected PlushieRenameScreen() {
        super(Text.literal("Paint plushie"));
    }

    @Override
    protected void init() {
        super.init();
        textfield=new TextFieldWidget(MinecraftClient.getInstance().textRenderer,200,20,Text.literal("Nickname"));
        addDrawableChild(new ButtonWidget.Builder(Text.literal("Paint plushie"), button -> {
            InterdimensionalLib.LOGGER.info(textfield.getText());
            MinecraftClient.getInstance().setScreen(null);
        }).build());
        addDrawableChild(textfield);
    }
}
