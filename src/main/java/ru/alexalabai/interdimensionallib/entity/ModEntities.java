package ru.alexalabai.interdimensionallib.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib.InterdimensionalLib;
import ru.alexalabai.interdimensionallib.entity.renderer.*;

public class ModEntities {
    public static final EntityType<SeatEntity> SEAT_ENTITY=reg("seat",
            EntityType.Builder.create(SeatEntity::new, SpawnGroup.MISC)
                    .disableSummon()
                    .passengerAttachments(0f)
                    .build());

    public static void regAll() {
        InterdimensionalLib.LOGGER.info("Registered entities");
    }

    @Environment(EnvType.CLIENT)
    public static void regRenderers() {
        EntityRendererRegistry.register(SEAT_ENTITY, SeatEntityRenderer::new);
        InterdimensionalLib.LOGGER.info("Registered entity renderers");
    }

    private static <T extends Entity> EntityType<T> reg(String name, EntityType<T> entityType) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(InterdimensionalLib.MOD_ID, name),
                entityType
        );
    }
}
