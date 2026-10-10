package ru.crystallized_dreams.interdimensionallib.client.render.shader;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

public class ShaderReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final Logger LOGGER = LoggerFactory.getLogger(InterdimensionalLib.MOD_ID);
    public static final Identifier ID = Identifier.of(InterdimensionalLib.MOD_ID, "shader_listener");
    
    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        ShaderHandler.getInstance().reload();
    }
}
