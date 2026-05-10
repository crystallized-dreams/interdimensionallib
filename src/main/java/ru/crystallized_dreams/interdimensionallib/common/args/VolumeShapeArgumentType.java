package ru.crystallized_dreams.interdimensionallib.common.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import ru.crystallized_dreams.interdimensionallib.common.types.VolumeShape;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class VolumeShapeArgumentType implements ArgumentType<VolumeShape> {
    private static final DynamicCommandExceptionType INVALID_GROW_RATE_EXCEPTION =
            new DynamicCommandExceptionType(v-> Text.literal("Invalid volume shape: "+v));
    private static final Collection<String> EXAMPLES = Arrays.stream(VolumeShape.values())
            .map(VolumeShape::name).map(String::toLowerCase).toList();

    private VolumeShapeArgumentType() {}
    public static VolumeShapeArgumentType shape() {
        return new VolumeShapeArgumentType();
    }
    public static VolumeShape getShape(CommandContext<?> context, String name) {
        return context.getArgument(name, VolumeShape.class);
    }

    @Override
    public VolumeShape parse(StringReader reader) throws CommandSyntaxException {
        String arg = reader.readUnquotedString();
        try {
            return VolumeShape.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw INVALID_GROW_RATE_EXCEPTION.create(arg);
        }
    }
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemaining().toLowerCase();
        EXAMPLES.forEach(ex->{if(ex.startsWith(remaining)) builder.suggest(ex);});
        return builder.buildFuture();
    }
}
