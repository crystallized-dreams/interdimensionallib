package ru.crystallized_dreams.interdimensionallib.content;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.content.block.BarrierPaneBlock;
import ru.crystallized_dreams.interdimensionallib.content.block.PlushieBlock;
import ru.crystallized_dreams.interdimensionallib.content.item.PlushieBlockItem;

public class INTERDIM_Blocks {
    public static final Block BATOONTECH_PLUSHIE=regBlock("batoontech_plushie",
            new PlushieBlock(AbstractBlock.Settings.copy(Blocks.BLACK_WOOL)));
    public static final Block WALL_OF_CLOUDS_PLUSHIE=regBlock("wall_of_clouds_plushie",
            new PlushieBlock(AbstractBlock.Settings.copy(Blocks.BLACK_WOOL)));
    public static final Block ALEX_ALABAI_PLUSHIE=regBlock("alex_alabai_plushie",
            new PlushieBlock(AbstractBlock.Settings.copy(Blocks.BLACK_WOOL)));
    public static final Block FORT_KIRILL_PLUSHIE=regBlock("fort_kirill_plushie",
            new PlushieBlock(AbstractBlock.Settings.copy(Blocks.BLACK_WOOL),true));
    public static final Block BLANK_PLUSHIE=regBlockWithoutItem("blank_plushie",
            new PlushieBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    public static final Block BARRIER_PANE=regBlockWithoutItem("barrier_pane",
            new BarrierPaneBlock(AbstractBlock.Settings.copy(Blocks.BARRIER)));

    @Environment(EnvType.CLIENT)
    public static void regAllRenderers() {
        BlockRenderLayerMap.INSTANCE.putBlock(BATOONTECH_PLUSHIE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(WALL_OF_CLOUDS_PLUSHIE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ALEX_ALABAI_PLUSHIE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FORT_KIRILL_PLUSHIE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLANK_PLUSHIE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BARRIER_PANE, RenderLayer.getTranslucent());
    }

    public static void regAll() {
        Registry.register(Registries.ITEM, Identifier.of(InterdimensionalLib.MOD_ID, "barrier_pane"),
                new BlockItem(BARRIER_PANE, new Item.Settings().rarity(Rarity.EPIC)));
        Registry.register(Registries.ITEM, Identifier.of(InterdimensionalLib.MOD_ID, "blank_plushie"),
                new PlushieBlockItem(BLANK_PLUSHIE, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS)
                .register(group->{
                    group.add(BLANK_PLUSHIE);
                    group.add(ALEX_ALABAI_PLUSHIE);
                    group.add(BATOONTECH_PLUSHIE);
                    group.add(FORT_KIRILL_PLUSHIE);
                    group.add(WALL_OF_CLOUDS_PLUSHIE);
                });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR)
                .register(group->{
                    group.add(BARRIER_PANE);
                });
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered blocks");
    }

    private static Block regBlock(String name, Block block) {
        regBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(InterdimensionalLib.MOD_ID, name), block);
    }
    private static Block regBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(InterdimensionalLib.MOD_ID, name), block);
    }
    private static void regBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(InterdimensionalLib.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }
}
