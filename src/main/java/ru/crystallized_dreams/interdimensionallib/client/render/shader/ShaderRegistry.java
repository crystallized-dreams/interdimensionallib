package ru.crystallized_dreams.interdimensionallib.client.render.shader;

import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

public class ShaderRegistry {
    public static void load() {
        ShaderHandler handler=ShaderHandler.getInstance();
        handler.register("interdimensionallib:grayscale", Identifier.of(InterdimensionalLib.MOD_ID, "shaders/post/grayscale.json"),
                ShaderHandler.ShaderType.POST_PROCESSING);
    }

    public static void registerPostShader(String modId, String name) {
        register(modId, name, ShaderHandler.ShaderType.POST_PROCESSING);
    }

    public static void registerWorldShader(String modId, String name) {
        register(modId, name, ShaderHandler.ShaderType.WORLD);
    }

    public static void register(String modId, String name, ShaderHandler.ShaderType type) {
        ShaderHandler.getInstance().register(modId + ":" + name,
                Identifier.of(modId, "shaders/post/"+name+".json"), type);
    }
}
