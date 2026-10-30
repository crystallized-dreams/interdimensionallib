package ru.crystallized_dreams.interdimensionallib.content;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.content.component.CommandExecutorData;
import ru.crystallized_dreams.interdimensionallib.content.item.CommandExecutorItem;

import java.util.ArrayList;
import java.util.Collections;

public class INTERDIM_Items {
    public static Item COMMAND_EXECUTOR=regItem("command_executor", new CommandExecutorItem(new Item.Settings().maxCount(1)));

    public static void regAll() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR)
                .register(group->{
                    for (int i = 0; i < 3; i++) {
                        var stack=COMMAND_EXECUTOR.getDefaultStack();
                        stack.set(INTERDIM_DataComponents.COMMAND_EXECUTOR_DATA,
                                new CommandExecutorData(new ArrayList<>(Collections.singleton("")),i,0));
                        group.add(stack);
                    }
                });
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered items");
    }

    public static Item regItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(InterdimensionalLib.MOD_ID, name), item);
    }
}
