package ru.crystallized_dreams.interdimensionallib.client.render.shader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShaderProgram implements AutoCloseable {
    private static final Map<String, ShaderProgram> CACHE=new HashMap<>();

    private final String name;
    private int programId;
    private int vertId;
    private int fragId;
    private final Map<String, Integer> uniformLocations=new HashMap<>();
    private boolean compiled=false;
    private boolean active=false;

    private ShaderProgram(String name) {
        this.name=name;
    }

    public static ShaderProgram create(String name, Identifier vertexId, Identifier fragmentId) {
        if (CACHE.containsKey(name)) return CACHE.get(name);

        ShaderProgram program=new ShaderProgram(name);
        program.compile(vertexId, fragmentId);
        CACHE.put(name, program);
        return program;
    }

    public static void clearCache() {
        CACHE.values().forEach(ShaderProgram::close);
        CACHE.clear();
    }

    private void compile(Identifier vertexId, Identifier fragmentId) {
        try {
            String vertexSource=loadShaderSource(vertexId);
            String fragmentSource=loadShaderSource(fragmentId);

            vertId=GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            GL20.glShaderSource(vertId, vertexSource);
            GL20.glCompileShader(vertId);
            checkCompileErrors(vertId, "VERTEX");

            fragId=GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
            GL20.glShaderSource(fragId, fragmentSource);
            GL20.glCompileShader(fragId);
            checkCompileErrors(fragId, "FRAGMENT");

            programId=GL20.glCreateProgram();
            GL20.glAttachShader(programId, vertId);
            GL20.glAttachShader(programId, fragId);

            GL20.glBindAttribLocation(programId, 0, "Position");
            GL20.glBindAttribLocation(programId, 1, "UV0");
            GL20.glBindAttribLocation(programId, 2, "Color");
            GL20.glBindAttribLocation(programId, 3, "Normal");

            GL20.glLinkProgram(programId);
            checkLinkErrors(programId);

            compiled=true;
            InterdimensionalLib.LOGGER.info("[INTERDIM_CLIENT]: Shader \"{}\" compiled successfully (ID: {})", name, programId);
        } catch (Exception e) {
            InterdimensionalLib.LOGGER.error("[INTERDIM_CLIENT]: Failed to compile shader '{}'", name, e);
            cleanup();
        }
    }

    private String loadShaderSource(Identifier location) throws Exception {
        MinecraftClient client=MinecraftClient.getInstance();
        Optional<Resource> resourceOpt=client.getResourceManager().getResource(location);

        if (resourceOpt.isEmpty()) {
            throw new RuntimeException("Shader resource not found: " + location);
        }

        try (BufferedReader reader=new BufferedReader(new InputStreamReader(resourceOpt.get().getInputStream()))) {
            String source=reader.lines().collect(Collectors.joining("\n"));
            return processIncludes(source);
        }
    }

    private String processIncludes(String source) {
        StringBuilder result=new StringBuilder();
        for (String line : source.split("\n")) {
            if (line.trim().startsWith("#include")) {
                String includePath=line.trim()
                        .replace("#include", "").replace("\"", "")
                        .replace("<", "").replace(">", "").trim();

                try {
                    Identifier includeId=Identifier.of(InterdimensionalLib.MOD_ID, "shaders/include/" + includePath);
                    String includeSource=loadShaderSource(includeId);
                    result.append(includeSource).append("\n");
                } catch (Exception e) {
                    InterdimensionalLib.LOGGER.warn("[INTERDIM_CLIENT]: Failed to include shader file: {}", includePath);
                    result.append("//Failed to include: \"").append(includePath).append("\"\n");
                }
            } else result.append(line).append("\n");
        }
        return result.toString();
    }

    private void checkCompileErrors(int shaderId, String type) {
        int success=GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS);
        if (success==GL20.GL_FALSE) {
            String log=GL20.glGetShaderInfoLog(shaderId, 1024);
            throw new RuntimeException(type + " shader compilation failed for '" + name + "': " + log);
        }
    }

    private void checkLinkErrors(int programId) {
        int success=GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
        if (success==GL20.GL_FALSE) {
            String log=GL20.glGetProgramInfoLog(programId, 1024);
            throw new RuntimeException("Shader program linking failed for '" + name + "': " + log);
        }
    }

    public void bind() {
        if (compiled) GL20.glUseProgram(programId);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public int getUniformLocation(String uniformName) {
        return uniformLocations.computeIfAbsent(uniformName,
                n -> GL20.glGetUniformLocation(programId, n));
    }

    public void setUniform(String name, float value) {
        int loc=getUniformLocation(name);
        if (loc >= 0) GL20.glUniform1f(loc, value);
    }

    public void setUniform(String name, float x, float y) {
        int loc=getUniformLocation(name);
        if (loc >= 0) GL20.glUniform2f(loc, x, y);
    }

    public void setUniform(String name, float x, float y, float z) {
        int loc=getUniformLocation(name);
        if (loc >= 0) GL20.glUniform3f(loc, x, y, z);
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        int loc=getUniformLocation(name);
        if (loc >= 0) GL20.glUniform4f(loc, x, y, z, w);
    }

    public void setUniform(String name, int value) {
        int loc=getUniformLocation(name);
        if (loc >= 0) GL20.glUniform1i(loc, value);
    }

    public void setUniform(String name, boolean value) {
        setUniform(name, value ? 1 : 0);
    }

    public void setUniform(String name, Matrix4f matrix) {
        int loc=getUniformLocation(name);
        if (loc >= 0) {
            try (MemoryStack stack=MemoryStack.stackPush()) {
                FloatBuffer buffer=stack.mallocFloat(16);
                matrix.get(buffer);
                GL20.glUniformMatrix4fv(loc, false, buffer);
            }
        }
    }

    public void setTexture(String name, int textureUnit, int textureId) {
        GL20.glActiveTexture(GL20.GL_TEXTURE0 + textureUnit);
        GL20.glBindTexture(GL20.GL_TEXTURE_2D, textureId);
        setUniform(name, textureUnit);
    }

    private void cleanup() {
        if (vertId!=0) {
            GL20.glDeleteShader(vertId);
            vertId=0;
        }
        if (fragId!=0) {
            GL20.glDeleteShader(fragId);
            fragId=0;
        }
        if (programId!=0) {
            GL20.glDeleteProgram(programId);
            programId=0;
        }
        compiled=false;
    }

    @Override
    public void close() {
        cleanup();
        uniformLocations.clear();
    }

    public int getProgramId() {
        return programId;
    }

    public boolean isCompiled() {
        return compiled;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active=active;
    }
}