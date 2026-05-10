package ru.crystallized_dreams.interdimensionallib.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

public class HelperTags {
    public static final TagKey<Item> PIGLINS_LOVE_ARMOR=
            TagKey.of(RegistryKeys.ITEM, Identifier.of(InterdimensionalLib.MOD_ID, "piglins_love_armor"));
    public static final TagKey<Item> VILLAGER_FOLLOW_ITEMS=
            TagKey.of(RegistryKeys.ITEM, Identifier.of(InterdimensionalLib.MOD_ID, "villagers_follow"));
    public static final TagKey<Block> SCAFFOLDING_BLOCKS=
            TagKey.of(RegistryKeys.BLOCK, Identifier.of(InterdimensionalLib.MOD_ID,"scaffolding_blocks"));

    public static void regAll() { }
}
