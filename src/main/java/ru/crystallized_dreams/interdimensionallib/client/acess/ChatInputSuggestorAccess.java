package ru.crystallized_dreams.interdimensionallib.client.acess;

import net.minecraft.util.math.Vec2f;

public interface ChatInputSuggestorAccess {
    void interdim$setPos(int x, int y);
    void interdim$setPos(Vec2f pos);
    Vec2f interdim$getPos();
    void interdim$setYOverride(boolean override);
}
