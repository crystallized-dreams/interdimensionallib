package ru.crystallized_dreams.interdimensionallib.client.render.shader;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.joml.Matrix4f;

public class ShaderEvents {
    public static final Event<PrePostProcess> PRE_POST_PROCESS=EventFactory.createArrayBacked(
            PrePostProcess.class, listeners->(tickDelta)->{
                for (PrePostProcess listener : listeners)
                    listener.onPrePostProcess(tickDelta);
            });

    public static final Event<PostPostProcess> POST_POST_PROCESS=EventFactory.createArrayBacked(
            PostPostProcess.class, listeners->(tickDelta)->{
                for (PostPostProcess listener : listeners)
                    listener.onPostPostProcess(tickDelta);
            });

    public static final Event<SetupUniforms> SETUP_UNIFORMS=EventFactory.createArrayBacked(
            SetupUniforms.class, listeners->(projMatrix, viewMatrix, tickDelta)->{
                for (SetupUniforms listener : listeners)
                    listener.onSetupUniforms(projMatrix, viewMatrix, tickDelta);
            });

    @FunctionalInterface
    public interface PrePostProcess {
        void onPrePostProcess(float tickDelta);
    }

    @FunctionalInterface
    public interface PostPostProcess {
        void onPostPostProcess(float tickDelta);
    }

    @FunctionalInterface
    public interface SetupUniforms {
        void onSetupUniforms(Matrix4f projectionMatrix, Matrix4f viewMatrix, float tickDelta);
    }
}
