package ru.alexalabai.interdimensionallib.mixin;

import net.minecraft.block.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("unused")
@Mixin(BlockSetType.class)
public interface CustomBlockSetType {
    @Invoker("register")
    static BlockSetType invokeRegister(BlockSetType blockSetType) {
        throw new AssertionError();
    }
}
