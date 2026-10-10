package ru.crystallized_dreams.interdimensionallib.content.recipe;

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
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_Recipes;

public record AnvilCrushRecipe(Identifier id, Ingredient input, ItemStack output) implements Recipe<SingleStackRecipeInput> {
    public AnvilCrushRecipe {
        output=output.copy();
    }
    public static AnvilCrushRecipe fromJson(Ingredient input, ItemStack output) {
        return new AnvilCrushRecipe(null,input,output);
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
        return AnvilCrushRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return INTERDIM_Recipes.ANVIL_CRUSH_RECIPE;
    }

    public static class AnvilCrushRecipeSerializer implements RecipeSerializer<AnvilCrushRecipe> {
        public static final AnvilCrushRecipeSerializer INSTANCE = new AnvilCrushRecipeSerializer();

        private static final MapCodec<AnvilCrushRecipe> CODEC = RecordCodecBuilder.mapCodec(i ->
                i.group(
                        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(r -> r.input),
                        ItemStack.CODEC.fieldOf("output").forGetter(r -> r.output)
                ).apply(i, (in,out)->new AnvilCrushRecipe(null,in,out)));
        private static final PacketCodec<RegistryByteBuf, AnvilCrushRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, AnvilCrushRecipe::input,
                ItemStack.PACKET_CODEC, AnvilCrushRecipe::output,
                AnvilCrushRecipe::fromJson
        );

        @Override
        public MapCodec<AnvilCrushRecipe> codec() {
            return CODEC;
        }
        @Override
        public PacketCodec<RegistryByteBuf, AnvilCrushRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
