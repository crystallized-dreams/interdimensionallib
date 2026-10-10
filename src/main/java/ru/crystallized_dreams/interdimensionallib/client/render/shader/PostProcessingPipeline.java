package ru.crystallized_dreams.interdimensionallib.client.render.shader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PostProcessingPipeline implements AutoCloseable {
    private final String name;
    private final Identifier configLocation;
    private final List<PostPass> passes=new ArrayList<>();
    private final Map<String, Framebuffer> namedFramebuffers=new HashMap<>();
    private final ShaderUniform globalUniforms=new ShaderUniform();
    private boolean initialized=false;

    public PostProcessingPipeline(String name, Identifier configLocation) {
        this.name=name;
        this.configLocation=configLocation;
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean active) {
        for (PostPass pass : passes) pass.program.setActive(active);
    }

    public void initialize() {
        try {
            loadConfig();
            initialized=true;
        } catch (Exception e) {
            InterdimensionalLib.LOGGER.error("[INTERDIM_CLIENT]: Failed to initialize pipeline: {}", name, e);
        }
    }

    private void loadConfig() throws Exception {
        MinecraftClient client=MinecraftClient.getInstance();
        Optional<Resource> resourceOpt=client.getResourceManager().getResource(configLocation);

        if (resourceOpt.isEmpty())
            throw new RuntimeException("Post-processing config not found: " + configLocation);

        String jsonStr;
        try (BufferedReader reader=new BufferedReader(
                new InputStreamReader(resourceOpt.get().getInputStream()))) {
            jsonStr=reader.lines().collect(Collectors.joining("\n"));
        }

        JsonObject config=JsonParser.parseString(jsonStr).getAsJsonObject();

        if (config.has("targets")) {
            JsonArray targets=config.getAsJsonArray("targets");
            int width=client.getWindow().getFramebufferWidth();
            int height=client.getWindow().getFramebufferHeight();

            for (JsonElement targetEl : targets) {
                String targetName=targetEl.getAsString();
                SimpleFramebuffer fb=new SimpleFramebuffer(width, height, true, false);
                namedFramebuffers.put(targetName, fb);
            }
        }

        if (config.has("passes")) {
            JsonArray passesArray=config.getAsJsonArray("passes");
            for (JsonElement passEl : passesArray) {
                JsonObject passObj=passEl.getAsJsonObject();
                String programName=passObj.get("program").getAsString();
                String input=passObj.has("input") ? passObj.get("input").getAsString() : "minecraft:main";
                String output=passObj.has("output") ? passObj.get("output").getAsString() : "minecraft:main";

                Identifier vsh=Identifier.of(InterdimensionalLib.MOD_ID, "shaders/program/" + programName + ".vsh");
                Identifier fsh=Identifier.of(InterdimensionalLib.MOD_ID, "shaders/program/" + programName + ".fsh");
                ShaderProgram program=ShaderProgram.create("post_" + programName, vsh, fsh);
                PostPass pass=new PostPass(program, input, output);

                if (passObj.has("uniforms")) {
                    JsonObject uniformsObj=passObj.getAsJsonObject("uniforms");
                    for (Map.Entry<String, JsonElement> uniformEntry : uniformsObj.entrySet()) {
                        JsonElement val=uniformEntry.getValue();
                        if (val.isJsonPrimitive()) {
                            if (val.getAsJsonPrimitive().isNumber()) {
                                pass.uniforms.set(uniformEntry.getKey(), val.getAsFloat());
                            }
                        } else if (val.isJsonArray()) {
                            JsonArray arr=val.getAsJsonArray();
                            if (arr.size()==2) {
                                pass.uniforms.set(uniformEntry.getKey(),
                                        arr.get(0).getAsFloat(), arr.get(1).getAsFloat());
                            } else if (arr.size()==3) {
                                pass.uniforms.set(uniformEntry.getKey(),
                                        arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
                            }
                        }
                    }
                }
                passes.add(pass);
            }
        }
    }

    public void setUniforms(Consumer<ShaderUniform> consumer) {
        consumer.accept(globalUniforms);
    }

    public void render(Framebuffer inputOverride, Framebuffer outputOverride, float tickDelta) {
        if (!initialized || passes.isEmpty()) return;

        for (PostPass pass : passes) {
            Framebuffer inputFb=resolveFramebuffer(pass.input, inputOverride, outputOverride, false);
            Framebuffer outputFb=resolveFramebuffer(pass.output, inputOverride, outputOverride, true);

            if (!pass.program.isActive()) {
                if (inputFb!=outputFb) {
                    outputFb.beginWrite(true);
                    RenderSystem.disableDepthTest();
                    RenderSystem.disableBlend();
                    inputFb.draw(outputFb.textureWidth, outputFb.textureHeight, false);
                }
                continue;
            }

            outputFb.beginWrite(true);

            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();

            pass.program.bind();
            pass.program.setTexture("DiffuseSampler", 0, inputFb.getColorAttachment());
            if (inputFb.getDepthAttachment() > 0) {
                pass.program.setTexture("DepthSampler", 1, inputFb.getDepthAttachment());
            }

            globalUniforms.applyTo(pass.program);
            pass.uniforms.applyTo(pass.program);

            drawFullscreenQuad();
            pass.program.unbind();
        }

        RenderSystem.enableDepthTest();
    }

    private Framebuffer resolveFramebuffer(String name, Framebuffer inputOverride, Framebuffer outputOverride, boolean isOutput) {
        if (name.equals("minecraft:main")) return isOutput ? outputOverride : inputOverride;
        return namedFramebuffers.getOrDefault(name, isOutput ? outputOverride : inputOverride);
    }

    private void drawFullscreenQuad() {
        Tessellator tessellator=Tessellator.getInstance();
        BufferBuilder bufferBuilder=tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        bufferBuilder.vertex(-1.0f, -1.0f, 0.0f).texture(0.0f, 0.0f);
        bufferBuilder.vertex(1.0f, -1.0f, 0.0f).texture(1.0f, 0.0f);
        bufferBuilder.vertex(1.0f, 1.0f, 0.0f).texture(1.0f, 1.0f);
        bufferBuilder.vertex(-1.0f, 1.0f, 0.0f).texture(0.0f, 1.0f);

        BufferRenderer.draw(bufferBuilder.end());
    }

    public void resize(int width, int height) {
        for (Framebuffer fb : namedFramebuffers.values()) {
            fb.resize(width, height, false);
        }
    }

    @Override
    public void close() {
        passes.forEach(pass -> pass.program.close());
        passes.clear();

        namedFramebuffers.values().forEach(Framebuffer::delete);
        namedFramebuffers.clear();

        initialized=false;
    }

    private static class PostPass {
        final ShaderProgram program;
        final String input;
        final String output;
        final ShaderUniform uniforms=new ShaderUniform();

        PostPass(ShaderProgram program, String input, String output) {
            this.program=program;
            this.input=input;
            this.output=output;
        }
    }
}