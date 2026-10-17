package ru.crystallized_dreams.interdimensionallib.common;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

public class INTERDIM_Sounds {
    static final Identifier BLOCK_PLUSHIE_CLICK_SOUND_ID=Identifier.of(InterdimensionalLib.MOD_ID, "block.plushie.click");
    public static SoundEvent BLOCK_PLUSHIE_CLICK_SOUND=SoundEvent.of(BLOCK_PLUSHIE_CLICK_SOUND_ID);
    static final Identifier BLOCK_PLUSHIE_CLOWN_CLICK_SOUND_ID=Identifier.of(InterdimensionalLib.MOD_ID, "block.plushie.clown_click");
    public static SoundEvent BLOCK_PLUSHIE_CLOWN_CLICK_SOUND=SoundEvent.of(BLOCK_PLUSHIE_CLOWN_CLICK_SOUND_ID);

    public static void regAll() {
        Registry.register(Registries.SOUND_EVENT, BLOCK_PLUSHIE_CLICK_SOUND_ID, BLOCK_PLUSHIE_CLICK_SOUND);
        Registry.register(Registries.SOUND_EVENT, BLOCK_PLUSHIE_CLOWN_CLICK_SOUND_ID, BLOCK_PLUSHIE_CLOWN_CLICK_SOUND);
    }
}
