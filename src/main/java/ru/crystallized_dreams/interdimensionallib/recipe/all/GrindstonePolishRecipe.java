package ru.crystallized_dreams.interdimensionallib.recipe.all;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import ru.crystallized_dreams.interdimensionallib.recipe.INTERDIM_Recipes;

public record GrindstonePolishRecipe(Identifier id, Ingredient input, ItemStack output) implements Recipe<SingleStackRecipeInput> {
    public GrindstonePolishRecipe {
        output=output.copy();
    }
    public static GrindstonePolishRecipe fromJson(Ingredient input, ItemStack output) {
        return new GrindstonePolishRecipe(null,input,output);
    }

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return this.input.test(input.item());
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GrindstonePolishRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return INTERDIM_Recipes.GRINDSTONE_POLISH_RECIPE;
    }

    public static class GrindstonePolishRecipeSerializer implements RecipeSerializer<GrindstonePolishRecipe> {
        public static final GrindstonePolishRecipeSerializer INSTANCE = new GrindstonePolishRecipeSerializer();

        private static final MapCodec<GrindstonePolishRecipe> CODEC = RecordCodecBuilder.mapCodec(i ->
                i.group(
                        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(r -> r.input),
                        ItemStack.CODEC.fieldOf("output").forGetter(r -> r.output)
                ).apply(i, (in,out)->new GrindstonePolishRecipe(null,in,out)));
        private static final PacketCodec<RegistryByteBuf, GrindstonePolishRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, GrindstonePolishRecipe::input,
                ItemStack.PACKET_CODEC, GrindstonePolishRecipe::output,
                GrindstonePolishRecipe::fromJson
        );

        @Override
        public MapCodec<GrindstonePolishRecipe> codec() {
            return CODEC;
        }
        @Override
        public PacketCodec<RegistryByteBuf, GrindstonePolishRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
