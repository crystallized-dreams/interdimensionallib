package ru.alexalabai.interdimensionallib.common;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemUtils {
    public static int getEnchantmentLevel(ItemStack stack, World world, RegistryKey<Enchantment> enchantment) {
        return stack.getEnchantments().getLevel(getEntry(world, enchantment));
    }
    public static RegistryEntry<Enchantment> getEntry(@Nullable World world, RegistryKey<Enchantment> enchantment) {
        if(world==null) world=MinecraftClient.getInstance().world;
        return world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment);
    }
    public static boolean hasEnchantmentLevel(ItemStack stack, World world, RegistryKey<Enchantment> enchantment) {
        return getEnchantmentLevel(stack, world, enchantment)>=1;
    }

    public static void tryToInsertStack(PlayerEntity player, ItemStack stack) {
        player.getInventory().insertStack(stack);
        if(!stack.isEmpty()) ItemScatterer.spawn(player.getWorld(),player.getPos().x,player.getPos().y,player.getPos().z,stack);
    }
}
