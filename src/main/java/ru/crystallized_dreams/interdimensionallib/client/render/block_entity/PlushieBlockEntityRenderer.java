package ru.crystallized_dreams.interdimensionallib.client.render.block_entity;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.common.SkinCache;
import ru.crystallized_dreams.interdimensionallib.content.block.RenameablePlushieBlock;
import ru.crystallized_dreams.interdimensionallib.content.block.entity.PlushieBlockEntity;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;

public class PlushieBlockEntityRenderer implements BlockEntityRenderer<PlushieBlockEntity> {
    public static final Identifier BLANK_SLIM=
            Identifier.of(InterdimensionalLib.MOD_ID, "block/blank_plushie");
    public static final Identifier BLANK_WIDE=
            Identifier.of(InterdimensionalLib.MOD_ID, "block/blank_wide_plushie");
    public static final Identifier TEMPLATE_SLIM=
            Identifier.of(InterdimensionalLib.MOD_ID, "block/template_plushie");
    public static final Identifier TEMPLATE_WIDE=
            Identifier.of(InterdimensionalLib.MOD_ID, "block/template_plushie_wide");

    public static void preloadModels() {
        ModelLoadingPlugin.register(ctx -> ctx.addModels(
                BLANK_SLIM, BLANK_WIDE, TEMPLATE_SLIM, TEMPLATE_WIDE
        ));
        InterdimensionalLib.LOGGER.info("[INTERDIM]: Registered extra plushie models");
    }

    private final Random random=Random.create(42);

    public PlushieBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    private BakedModel getModel(Identifier id) {
        var manager=MinecraftClient.getInstance().getBakedModelManager();
        return manager.getModel(id);
    }

    @Override
    public void render(PlushieBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        PlushieData data=entity.getData();
        boolean isBlank=data==null || data.equals(PlushieData.DEFAULT) || data.nickname().isBlank();
        boolean wide=isBlank?entity.getCachedState().get(RenameablePlushieBlock.WIDE):data.wide();

        Identifier modelId;
        if (isBlank) modelId=wide?BLANK_WIDE:BLANK_SLIM;
        else modelId=wide?TEMPLATE_WIDE:TEMPLATE_SLIM;

        BakedModel model=getModel(modelId);
        if (model==MinecraftClient.getInstance().getBakedModelManager().getMissingModel()) return;

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getCachedState().get(RenameablePlushieBlock.FACING).asRotation()-90));
        matrices.translate(-0.5, -0.5, -0.5);

        MatrixStack.Entry entry=matrices.peek();
        if (isBlank) {
            VertexConsumer vc=vertexConsumers.getBuffer(RenderLayer.getCutout());
            renderQuads(model, entry, vc, light, overlay, false);
        } else {
            Identifier skin=SkinCache.get(data.nickname());
            VertexConsumer vc=vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(skin));
            renderQuads(model, entry, vc, light, overlay, true);
        }
        matrices.pop();
    }

    private void renderQuads(BakedModel model, MatrixStack.Entry entry, VertexConsumer vc, int light, int overlay, boolean remapUv) {
        for (Direction dir : Direction.values())
            for(BakedQuad quad : model.getQuads(null, dir, random))
                emitQuad(quad, entry, vc, light, overlay, remapUv);
        for (BakedQuad quad : model.getQuads(null, null, random))
            emitQuad(quad, entry, vc, light, overlay, remapUv);
    }

    private void emitQuad(BakedQuad quad, MatrixStack.Entry entry, VertexConsumer vc, int light, int overlay, boolean remapUv) {
        int[] data=quad.getVertexData();
        Sprite sprite=quad.getSprite();
        float minU=sprite.getMinU();
        float maxU=sprite.getMaxU();
        float minV=sprite.getMinV();
        float maxV=sprite.getMaxV();
        float deltaU=maxU-minU;
        float deltaV=maxV-minV;

        float nx=0, ny=1, nz=0;
        if (quad.getFace()!=null){
            nx=quad.getFace().getOffsetX();
            ny=quad.getFace().getOffsetY();
            nz=quad.getFace().getOffsetZ();
        }

        for (int i=0;i<4;i++){
            int o=i*8;
            float x=Float.intBitsToFloat(data[o]);
            float y=Float.intBitsToFloat(data[o+1]);
            float z=Float.intBitsToFloat(data[o+2]);
            int color=data[o+3];
            float u=Float.intBitsToFloat(data[o+4]);
            float v=Float.intBitsToFloat(data[o+5]);

            if (remapUv) {
                u=(u-minU)/deltaU;
                v=(v-minV)/deltaV;
            }

            float r=((color >> 16) & 0xFF)/255f;
            float g=((color >> 8) & 0xFF)/255f;
            float b=(color & 0xFF)/255f;
            float a=((color >> 24) & 0xFF)/255f;

            vc.vertex(entry.getPositionMatrix(), x, y, z)
                    .color(r,g,b,a)
                    .texture(u, v)
                    .overlay(overlay)
                    .light(remapUv ? light : light)
                    .normal(entry, nx, ny, nz);
        }
    }
}