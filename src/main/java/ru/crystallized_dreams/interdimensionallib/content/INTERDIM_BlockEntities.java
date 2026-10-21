package ru.crystallized_dreams.interdimensionallib.content;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.content.block.entity.PlushieBlockEntity;

public class INTERDIM_BlockEntities {
    public static final BlockEntityType<PlushieBlockEntity> PLUSHIE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE, Identifier.of(InterdimensionalLib.MOD_ID, "plushie"),
                    BlockEntityType.Builder.create(PlushieBlockEntity::new, INTERDIM_Blocks.BLANK_PLUSHIE).build(null)
            );

    public static void regAll() { }
}
