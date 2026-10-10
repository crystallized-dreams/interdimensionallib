package ru.crystallized_dreams.interdimensionallib.client.render.shader;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

public class ShaderUniform {
    private final Map<String, Object> uniforms = new HashMap<>();

    public void set(String name, float value) {
        uniforms.put(name, value);
    }
    public void set(String name, float x, float y) {
        uniforms.put(name, new Vector2f(x, y));
    }
    public void set(String name, float x, float y, float z) {
        uniforms.put(name, new Vector3f(x, y, z));
    }
    public void set(String name, float x, float y, float z, float w) {
        uniforms.put(name, new Vector4f(x, y, z, w));
    }
    public void set(String name, int value) {
        uniforms.put(name, value);
    }
    public void set(String name, Matrix4f matrix) {
        uniforms.put(name, new Matrix4f(matrix));
    }

    public void applyTo(ShaderProgram program) {
        for (Map.Entry<String, Object> entry : uniforms.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Float f) program.setUniform(name, f);
            else if (value instanceof Vector2f v) program.setUniform(name, v.x, v.y);
            else if (value instanceof Vector3f v) program.setUniform(name, v.x, v.y, v.z);
            else if (value instanceof Vector4f v) program.setUniform(name, v.x, v.y, v.z, v.w);
            else if (value instanceof Integer i) program.setUniform(name, i);
            else if (value instanceof Matrix4f m) program.setUniform(name, m);
        }
    }

    public void clear() {
        uniforms.clear();
    }
}
