package ru.alexalabai.interdimensionallib.common;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;

public class ClientGameOptions {
    public static KeyBinding crawlKey;
    public static KeyBinding sitKey;
    public static SimpleOption<Boolean> crawlToggle;
    public static SimpleOption<Boolean> sitToggle;

    public static final Text ON = Text.translatable("options.on");
    public static final Text OFF = Text.translatable("options.off");
    public static final Text TOGGLE = Text.translatable("options.key.toggle");
    public static final Text HOLD = Text.translatable("options.key.hold");

    public static void regAll() {
        crawlToggle=new SimpleOption<>(
                "interdimensionallib.key.crawl",
                SimpleOption.emptyTooltip(),
                (text, value)->(value?TOGGLE:HOLD),
                SimpleOption.BOOLEAN,
                false,
                (val)->{});
        sitToggle=new SimpleOption<>(
                "kaban-interdimensionallib.key.click_to_sit",
                SimpleOption.emptyTooltip(),
                (text, value)->(value?ON:OFF),
                SimpleOption.BOOLEAN,
                true,
                (val)->{});
        crawlKey=new StickyKeyBinding(
                "interdimensionallib.key.crawl",
                GLFW.GLFW_KEY_Z,
                KeyBinding.MOVEMENT_CATEGORY,
                ()->crawlToggle.getValue());
        sitKey=new KeyBinding(
                "interdimensionallib.key.sit",
                GLFW.GLFW_KEY_X,
                KeyBinding.MOVEMENT_CATEGORY);
        KeyBindingHelper.registerKeyBinding(crawlKey);
        KeyBindingHelper.registerKeyBinding(sitKey);

        InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Registered key bindings");
    }
}
