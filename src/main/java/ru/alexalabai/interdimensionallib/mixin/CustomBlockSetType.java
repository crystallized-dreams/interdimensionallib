package ru.alexalabai.interdimensionallib.mixin;

import net.minecraft.block.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("unused")
@Mixin(BlockSetType.class)
public interface CustomBlockSetType {
    /**
     * Adds given block type to the Vanilla registry.
     * <p><b>How new `canOpenByHand` parameter works: </b><br>
     * - `canOpenByHand=true, canButtonBeActivatedByArrows=true` -> button/plate and doors can be interacted freely <br>
     * - `canOpenByHand=true, canButtonBeActivatedByArrows=false` -> button/plate and doors can be interacted freely but arrows don't affect them <br>
     * - `canOpenByHand=false, canButtonBeActivatedByArrows=true` -> button/plate can be interacted freely but doors can't <br>
     * - `canOpenByHand=false, canButtonBeActivatedByArrows=false` -> button/plate and doors can't be interacted freely
    */
    @Invoker("register")
    static BlockSetType register(BlockSetType blockSetType) {
        throw new AssertionError();
    }
}
