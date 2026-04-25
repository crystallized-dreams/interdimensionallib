package ru.alexalabai.interdimensionallib.emi;

import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;

public class EmiUtils {
    public static void addTexture(WidgetHolder ctx, Identifier texture, int x, int y, int width, int height) {
        ctx.addTexture(texture,x,y,width,height,0,0,width,height,width,height);
    }
}
