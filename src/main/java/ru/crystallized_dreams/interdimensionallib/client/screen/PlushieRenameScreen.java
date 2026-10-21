package ru.crystallized_dreams.interdimensionallib.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;
import ru.crystallized_dreams.interdimensionallib.packets.all.UpdatePlushiePayload;

public class PlushieRenameScreen extends Screen {
    private TextFieldWidget nameField;
    private boolean wide;

    public PlushieRenameScreen(PlushieData current) {
        super(Text.translatable("text.interdimensionallib.plushie_screen.title"));
        wide = current.wide();
        initialName = current.nickname();
    }

    private final String initialName;

    @Override
    protected void init() {
        super.init();
        int centerX = width/2;
        int centerY = height/2;

        nameField = new TextFieldWidget(textRenderer, centerX-100, centerY-30, 200, 20,
                Text.translatable("text.interdimensionallib.plushie_screen.nickname"));
        nameField.setMaxLength(16);
        nameField.setText(initialName);
        nameField.setFocused(true);
        addDrawableChild(nameField);

        addDrawableChild(ButtonWidget.builder(
                Text.translatable(wide ? "text.interdimensionallib.plushie_screen.wide_arms" :
                        "text.interdimensionallib.plushie_screen.slim_arms"),
                btn -> { wide = !wide; btn.setMessage(
                        Text.translatable(wide ? "text.interdimensionallib.plushie_screen.wide_arms" :
                                "text.interdimensionallib.plushie_screen.slim_arms")); }
        ).dimensions(centerX-60, centerY+5, 120, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("text.interdimensionallib.plushie_screen.confirm"), btn -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "Steve";

            ClientPlayNetworking.send(new UpdatePlushiePayload(name, wide));
            close();
        }).dimensions(centerX+5, centerY+35, 55, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("text.interdimensionallib.plushie_screen.cancel"),
                        btn -> close()).dimensions(centerX-60, centerY+35, 55, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width/2, height/2-55, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}