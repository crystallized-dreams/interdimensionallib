package ru.crystallized_dreams.interdimensionallib.common;

import net.minecraft.block.Block;
import net.minecraft.sound.SoundEvent;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

import java.util.LinkedHashMap;
import java.util.Map;

public class NoteBlockInstrumentRegistry {
    public static final Map<Block, InstrumentSoundEntry> REGISTRY=new LinkedHashMap<>();

    public static void register(Block block, SoundEvent sound, float volume, boolean pitched) {
        try {
            REGISTRY.put(block, new InstrumentSoundEntry(sound, volume, pitched));
        } catch (Exception e) {
            InterdimensionalLib.LOGGER.error("[INTERDIM]: Failed to register custom note block instrument: "+e.getMessage());
        }
    }
    public static void register(Block block, SoundEvent sound, float volume) {
        register(block,sound,volume,true);
    }
    public static void register(Block block, SoundEvent sound) {
        register(block,sound,1,true);
    }
    public static boolean contains(Block block) {
        return REGISTRY.containsKey(block);
    }
    public static InstrumentSoundEntry get(Block block) {
        return REGISTRY.getOrDefault(block,null);
    }

    public record InstrumentSoundEntry(SoundEvent sound, float volume, boolean pitched) { }
}
