package ru.alexalabai.interdimensionallib.common;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Utils {
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

    public static float smoothStep(float t) {
        return t * t * (3.0F - 2.0F * t);
    }
    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }
    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }
    public static Vec3d lerp(Vec3d a, Vec3d b, double t) {
        return a.add(b.subtract(a).multiply(t));
    }
    public static float valueNoise(float x, float y, float z) {
        int x0 =(int)Math.floor(x);
        int y0 =(int)Math.floor(y);
        int z0 =(int)Math.floor(z);
        float dx = smoothStep(x - (float)x0);
        float dy = smoothStep(y - (float)y0);
        float dz = smoothStep(z - (float)z0);
        float v000 = Objects.hash(x0, y0, z0);
        float v100 = Objects.hash(x0 + 1, y0, z0);
        float v010 = Objects.hash(x0, y0 + 1, z0);
        float v110 = Objects.hash(x0 + 1, y0 + 1, z0);
        float v001 = Objects.hash(x0, y0, z0 + 1);
        float v101 = Objects.hash(x0 + 1, y0, z0 + 1);
        float v011 = Objects.hash(x0, y0 + 1, z0 + 1);
        float v111 = Objects.hash(x0 + 1, y0 + 1, z0 + 1);
        float x00 = lerp(v000, v100, dx);
        float x10 = lerp(v010, v110, dx);
        float x01 = lerp(v001, v101, dx);
        float x11 = lerp(v011, v111, dx);
        return lerp(lerp(x00, x10, dy), lerp(x01, x11, dy), dz);
    }
}
