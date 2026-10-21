package ru.crystallized_dreams.interdimensionallib.client.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;
import ru.crystallized_dreams.interdimensionallib.config.INTERDIM_ClientModConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShaderHandler {
    private static ShaderHandler INSTANCE;

    private final List<Shader> registered=new ArrayList<>();
    private final List<PostProcessingPipeline> activePipelines=new ArrayList<>();
    private final List<ShaderProgram> activeWorldShaders=new ArrayList<>();
    private final Set<String> activeShaderNames=new HashSet<>();

    private Framebuffer auxFBO;
    private Framebuffer outFBO;
    private float time=0f;
    private int logDelayTicks=0;

    private ShaderHandler() {}

    public static ShaderHandler getInstance() {
        if (INSTANCE==null) {
            INSTANCE=new ShaderHandler();
        }
        return INSTANCE;
    }

    public void register(String name, Identifier location, ShaderType type) {
        registered.add(new Shader(name, location, type));
        InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Registered shader: {}", name);
    }

    public void reload() {
        InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Reloading shaders");
        deactivateAll();
        ShaderProgram.clearCache();
        if (!registered.isEmpty()) activateAll();
        InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Shaders reloaded");
    }

    public boolean setUniform(String name, String uniformName, boolean value) {
        String targetName=name;
        boolean found=false;

        for (Shader entry : registered) {
            if (entry.name().equalsIgnoreCase(name) || entry.name().endsWith(":" + name)) {
                targetName=entry.name();
                found=true;
                break;
            }
        }
        if (!found||!activeShaderNames.contains(targetName)) return false;
        for (ShaderProgram shader : activeWorldShaders)
            if (shader.getName().equalsIgnoreCase(targetName) || shader.getName().endsWith(":" + targetName))
                shader.setUniform(uniformName, value);
        return true;
    }

    public boolean setUniform(String name, String uniformName, int value) {
        String targetName=name;
        boolean found=false;

        for (Shader entry : registered) {
            if (entry.name().equalsIgnoreCase(name) || entry.name().endsWith(":" + name)) {
                targetName=entry.name();
                found=true;
                break;
            }
        }
        if (!found||!activeShaderNames.contains(targetName)) return false;
        for (ShaderProgram shader : activeWorldShaders)
            if (shader.getName().equalsIgnoreCase(targetName) || shader.getName().endsWith(":" + targetName))
                shader.setUniform(uniformName, value);
        return true;
    }

    public boolean setUniform(String name, String uniformName, float value) {
        String targetName=name;
        boolean found=false;

        for (Shader entry : registered) {
            if (entry.name().equalsIgnoreCase(name) || entry.name().endsWith(":" + name)) {
                targetName=entry.name();
                found=true;
                break;
            }
        }
        if (!found||!activeShaderNames.contains(targetName)) return false;
        for (ShaderProgram shader : activeWorldShaders)
            if (shader.getName().equalsIgnoreCase(targetName) || shader.getName().endsWith(":" + targetName))
                shader.setUniform(uniformName, value);
        return true;
    }

    public boolean setActive(String name, boolean active) {
        String targetName=name;
        boolean found=false;

        for (Shader entry : registered) {
            if (entry.name().equalsIgnoreCase(name) || entry.name().endsWith(":" + name)) {
                targetName=entry.name();
                found=true;
                break;
            }
        }

        if (active) {
            activeShaderNames.add(targetName);
            InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Enabled shader '{}'", targetName);
        } else {
            activeShaderNames.remove(targetName);
            InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Disabled shader '{}'", targetName);
        }

        for (ShaderProgram shader : activeWorldShaders)
            if (shader.getName().equalsIgnoreCase(targetName) || shader.getName().endsWith(":" + targetName))
                shader.setActive(active);

        for (PostProcessingPipeline pipeline : activePipelines)
            if (pipeline.getName().equalsIgnoreCase(targetName) || pipeline.getName().endsWith(":" + targetName))
                pipeline.setActive(active);

        return found;
    }

    public boolean isShaderActive(String name) {
        for (String activeName : activeShaderNames)
            if (activeName.equalsIgnoreCase(name) || activeName.endsWith(":" + name)) return true;
        return false;
    }

    private void activateAll() {
        deactivateAll();
        if (registered.isEmpty()) return;

        for (Shader entry : registered) {
            try {
                boolean isEnabled=isShaderActive(entry.name());
                switch (entry.type()) {
                    case POST_PROCESSING -> {
                        PostProcessingPipeline pipeline=new PostProcessingPipeline(entry.name(), entry.location());
                        pipeline.initialize();
                        pipeline.setActive(isEnabled);
                        activePipelines.add(pipeline);
                    }
                    case WORLD -> {
                        ShaderProgram worldShader=ShaderProgram.create(entry.name(),
                                Identifier.of(InterdimensionalLib.MOD_ID, "shaders/program/" + entry.name() + ".vsh"),
                                Identifier.of(InterdimensionalLib.MOD_ID, "shaders/program/" + entry.name() + ".fsh")
                        );
                        worldShader.setActive(isEnabled);
                        activeWorldShaders.add(worldShader);
                    }
                }
            } catch (Exception e) {
                InterdimensionalLib.LOGGER.error("[INTERDIM_CLIENT]: Failed to activate shader: {}", entry.name(), e);
            }
        }
    }

    private void deactivateAll() {
        activePipelines.forEach(PostProcessingPipeline::close);
        activePipelines.clear();
        activeWorldShaders.forEach(ShaderProgram::close);
        activeWorldShaders.clear();
    }

    private void setupFramebuffers(int width, int height) {
        if (auxFBO!=null) auxFBO.delete();
        if (outFBO!=null) outFBO.delete();

        auxFBO=new SimpleFramebuffer(width, height, true, false);
        outFBO=new SimpleFramebuffer(width, height, true, false);
    }

    public void onPreWorldRender(Matrix4f projectionMatrix, Matrix4f viewMatrix, float tickDelta) {
        time += tickDelta * 0.05f;

        for (ShaderProgram shader : activeWorldShaders) {
            if (!shader.isActive()) continue;

            shader.bind();
            shader.setUniform("Time", time);
            shader.setUniform("ProjectionMatrix", projectionMatrix);
            shader.setUniform("ModelViewMatrix", viewMatrix);

            MinecraftClient client=MinecraftClient.getInstance();
            shader.setUniform("ScreenSize",
                    (float) client.getWindow().getFramebufferWidth(),
                    (float) client.getWindow().getFramebufferHeight());
        }
    }

    public void onPostWorldRender(float tickDelta) {
        List<PostProcessingPipeline> pipelinesToRender=new ArrayList<>();
        for (PostProcessingPipeline pipeline : activePipelines)
            if (isShaderActive(pipeline.getName())) pipelinesToRender.add(pipeline);

        if(INTERDIM_ClientModConfig.INSTANCE.logActiveShaders) {
            if (logDelayTicks++%100==0)
                InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Active Post Shaders rendering: {}",
                        pipelinesToRender.stream().map(PostProcessingPipeline::getName).toList());
        }

        if (pipelinesToRender.isEmpty()) return;

        MinecraftClient client=MinecraftClient.getInstance();
        Framebuffer mainFramebuffer=client.getFramebuffer();

        int width=mainFramebuffer.textureWidth;
        int height=mainFramebuffer.textureHeight;

        if (auxFBO==null || auxFBO.textureWidth!=width || auxFBO.textureHeight!=height)
            setupFramebuffers(width, height);
        auxFBO.clear(MinecraftClient.IS_SYSTEM_MAC);
        outFBO.clear(MinecraftClient.IS_SYSTEM_MAC);

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFramebuffer.fbo);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, auxFBO.fbo);
        GL30.glBlitFramebuffer(
                0, 0, width, height,
                0, 0, width, height,
                GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST
        );

        Framebuffer currentInput=auxFBO;
        Framebuffer currentOutput=outFBO;
        for (PostProcessingPipeline pipeline : pipelinesToRender) {
            pipeline.setUniforms(uniforms -> {
                uniforms.set("Time", time);
                uniforms.set("ScreenSize", (float) width, (float) height);
            });

            pipeline.render(currentInput, currentOutput, tickDelta);

            Framebuffer temp=currentInput;
            currentInput=currentOutput;
            currentOutput=temp;
        }

        mainFramebuffer.beginWrite(true);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();

        currentInput.draw(width, height, false);

        RenderSystem.enableDepthTest();
    }

    public void onResized(int width, int height) {
        activePipelines.forEach(pipeline -> pipeline.resize(width, height));
        if (auxFBO!=null) auxFBO.resize(width, height, false);
        if (outFBO!=null) outFBO.resize(width, height, false);
    }

    public List<String> getRegistered() {
        List<String> out=new ArrayList<>();
        registered.forEach(shader->out.add(shader.name));
        return out;
    }

    public enum ShaderType {
        POST_PROCESSING, WORLD
    }
    public record Shader(String name, Identifier location, ShaderType type) {}
}