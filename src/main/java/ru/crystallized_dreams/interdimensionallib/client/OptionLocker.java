package ru.crystallized_dreams.interdimensionallib.client;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.SimpleOption;
import org.jetbrains.annotations.NotNull;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

import java.util.HashMap;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class OptionLocker {
    private static final HashMap<String, Optional<?>> LOCKED_OPTIONS = new HashMap<>();
    private static final BiMap<SimpleOption<?>, String> OPTIONS_KEYS = HashBiMap.create();
    private static <T> void lock(@NotNull String key, @NotNull T value) {
        if(LOCKED_OPTIONS.containsKey(key)) {
            InterdimensionalLib.LOGGER.error("[INTERDIM]: Tried to lock already locked option \"{}\"",key);
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

    public static void handleClientReady() {
        for(var entry : LOCKED_OPTIONS.entrySet()) {
            try {
                SimpleOption<?> option = OPTIONS_KEYS.inverse().get(entry.getKey());
                if (option != null) {
                    setValueUnchecked(option, entry.getValue().get());
                }
            } catch (Exception e) {
                InterdimensionalLib.LOGGER.error("[INTERDIM]: Failed to set option \"{}\": {}",entry.getKey(),e.getMessage());
            }
        }
    }
    @SuppressWarnings("unchecked")
    private static <T> void setValueUnchecked(SimpleOption<T> option, Object value) {
        option.setValue((T) value);
    }

    public static Optional<?> valueOf(String key) {
        return LOCKED_OPTIONS.containsKey(key)?LOCKED_OPTIONS.get(key):Optional.empty();
    }
    public static boolean contains(String optionName) {
        return LOCKED_OPTIONS.containsKey(optionName);
    }
    public static boolean contains(SimpleOption<?> option) {
        return LOCKED_OPTIONS.containsKey(OPTIONS_KEYS.get(option));
    }
    public static void addOption(SimpleOption<?> option, String id) {
        if(LOCKED_OPTIONS.containsKey(id)) {
            InterdimensionalLib.LOGGER.warn("[INTERDIM]: Overriding existing option \"{}\"",id);
            LOCKED_OPTIONS.remove(id);
        }
        OPTIONS_KEYS.put(option,id);
    }
    public static String getOptionName(SimpleOption<?> option) {
        return OPTIONS_KEYS.get(option);
    }
}
