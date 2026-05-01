package ru.alexalabai.interdimensionallib.common.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import ru.alexalabai.interdimensionallib.common.types.Easing;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class EasingArgumentType implements ArgumentType<Easing> {
    private static final DynamicCommandExceptionType INVALID_GROW_RATE_EXCEPTION =
            new DynamicCommandExceptionType(v-> Text.literal("Invalid easing: "+v));
    private static final Collection<String> EXAMPLES = Arrays.stream(Easing.values())
            .map(Easing::name).map(String::toLowerCase).toList();

    private EasingArgumentType() {}
    public static EasingArgumentType easing() {
        return new EasingArgumentType();
    }
    public static Easing getEasing(CommandContext<?> context, String name) {
        return context.getArgument(name, Easing.class);
    }

    @Override
    public Easing parse(StringReader reader) throws CommandSyntaxException {
        String arg = reader.readUnquotedString();
        try {
            return Easing.valueOf(arg.toUpperCase());
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
