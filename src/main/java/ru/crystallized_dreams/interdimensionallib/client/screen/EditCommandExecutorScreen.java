package ru.crystallized_dreams.interdimensionallib.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import ru.crystallized_dreams.interdimensionallib.content.component.CommandExecutorData;
import ru.crystallized_dreams.interdimensionallib.content.item.CommandExecutorItem;
import ru.crystallized_dreams.interdimensionallib.packets.all.UpdateCommandExecutorPayload;

import java.util.ArrayList;
import java.util.List;

public class EditCommandExecutorScreen extends Screen {
    private TextFieldWidget[] commandFields;
    private int type;
    private final List<String> commands;
    private ChatInputSuggestor commandSuggestor;
    private TextFieldWidget activeField; // Текущее активное поле

    public EditCommandExecutorScreen(CommandExecutorData data) {
        super(Text.translatable("text.interdimensionallib.command_executor_editor.title"));
        type = data.controllerType();
        commands = new ArrayList<>(data.commands());
    }

    @Override
    protected void init() {
        super.init();
        int centerX = width / 2;
        int centerY = height / 2;

        while (commands.size() < 4) commands.add("");
        commandFields = new TextFieldWidget[4];

        int startY = centerY - 60;
        int fieldSpacing = 25;

        for (int i = 0; i < 4; i++) {
            commandFields[i] = new TextFieldWidget(
                    textRenderer,
                    centerX - 100,
                    startY + (i * fieldSpacing),
                    200,
                    20,
                    Text.translatable("text.interdimensionallib.command_executor_editor.command")
            );
            commandFields[i].setMaxLength(1024);
            commandFields[i].setText(commands.get(i));
            addSelectableChild(commandFields[i]);
        }

        activeField = commandFields[0];
        activeField.setFocused(true);
        commandSuggestor = new ChatInputSuggestor(client, this,
                activeField, textRenderer, true, true,
                0, 7, false, -1);
        commandSuggestor.setWindowActive(true);
        commandSuggestor.refresh();

        addDrawableChild(ButtonWidget.builder(
                Text.translatable(CommandExecutorItem.getName(type)),
                btn -> {
                    type = (type + 1) % 3;
                    btn.setMessage(Text.translatable(CommandExecutorItem.getName(type)));
                }
        ).dimensions(centerX - 60, centerY + 45, 120, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("text.interdimensionallib.plushie_screen.confirm"),
                btn -> commitAndClose()
        ).dimensions(centerX + 5, centerY + 75, 55, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("text.interdimensionallib.plushie_screen.cancel"),
                btn -> close()
        ).dimensions(centerX - 60, centerY + 75, 55, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 85, 0xFFFFFF);

        int visibleFields = (type == 2) ? 4 : 1;
        for (int i = 0; i < visibleFields; i++)
            commandFields[i].render(context, mouseX, mouseY, delta);

        if (commandSuggestor != null) commandSuggestor.render(context, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (commandSuggestor != null && commandSuggestor.keyPressed(keyCode, scanCode, modifiers))
            return true;

        if (keyCode == 258) {
            switchToNextField(modifiers == 1);
            return true;
        }
        if (keyCode == 257 || keyCode == 335) {
            commitAndClose();
            return true;
        }
        if (activeField != null && activeField.keyPressed(keyCode, scanCode, modifiers))
            return true;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (activeField != null && activeField.charTyped(chr, modifiers)) {
            if (commandSuggestor != null) commandSuggestor.refresh();
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int visibleFields = (type == 2) ? 4 : 1;
        for (int i = 0; i < visibleFields; i++) {
            if (commandFields[i].isMouseOver(mouseX, mouseY)) {
                setActiveField(commandFields[i]);
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void switchToNextField(boolean reverse) {
        int visibleFields = (type == 2) ? 4 : 1;
        int currentIndex = -1;

        for (int i = 0; i < visibleFields; i++) {
            if (commandFields[i] == activeField) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) currentIndex = 0;

        int nextIndex;
        if (reverse) nextIndex = (currentIndex - 1 + visibleFields) % visibleFields;
        else nextIndex = (currentIndex + 1) % visibleFields;

        setActiveField(commandFields[nextIndex]);
    }

    private void setActiveField(TextFieldWidget newField) {
        if (activeField != null) activeField.setFocused(false);

        activeField = newField;
        activeField.setFocused(true);

        if (commandSuggestor != null) {
            commandSuggestor = new ChatInputSuggestor(this.client, this,
                    activeField, textRenderer, true, true,
                    0, 7, false, Integer.MIN_VALUE);
            commandSuggestor.setWindowActive(true);
            commandSuggestor.refresh();
        }
    }

    void commitAndClose() {
        for (int i = 0; i < 4; i++)
            commands.set(i, commandFields[i].getText());

        ClientPlayNetworking.send(new UpdateCommandExecutorPayload(commands, type));
        close();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String activeFieldText = activeField != null ? activeField.getText() : "";
        super.resize(client, width, height);

        if (activeField != null) activeField.setText(activeFieldText);
        if (commandSuggestor != null) commandSuggestor.refresh();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}