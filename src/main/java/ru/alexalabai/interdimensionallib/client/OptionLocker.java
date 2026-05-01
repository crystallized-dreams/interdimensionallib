package ru.alexalabai.interdimensionallib.client;

import net.minecraft.client.option.SimpleOption;
import org.jetbrains.annotations.NotNull;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;

import java.util.HashMap;
import java.util.Optional;

public class OptionLocker {
    private static final HashMap<String, Optional<?>> LOCKED_OPTIONS = new HashMap<>();
    private static final HashMap<SimpleOption<?>, String> OPTIONS_KEYS = new HashMap<>();
    private static <T> void lock(@NotNull String key, @NotNull T value) {
        if(LOCKED_OPTIONS.containsKey(key)) {
            InterdimensionalLib.LOGGER.info("[INTERDIM]: Tried to lock already locked option \"{}\"",key);
            return;
        }
        LOCKED_OPTIONS.put(key, Optional.of(value));
    }

    public static <T> void lockOption(@NotNull String option, @NotNull T value) {
        lock("options." + option, value);
    }
    public static <T> void lockSoundCategory(@NotNull String option, double value) {
        lock("soundCategory." + option, value);
    }

    public static Optional<?> valueOf(String key) {
        return LOCKED_OPTIONS.containsKey(key)?LOCKED_OPTIONS.get(key):Optional.empty();
    }

    public static boolean contains(SimpleOption<?> option) {
        return LOCKED_OPTIONS.containsKey(OPTIONS_KEYS.get(option));
    }
    public static void addOption(SimpleOption<?> option, String id) {
        OPTIONS_KEYS.put(option,id);
    }
    public static String getOptionName(SimpleOption<?> option) {
        return OPTIONS_KEYS.get(option);
    }
}
