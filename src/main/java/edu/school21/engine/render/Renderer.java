package edu.school21.engine.render;

import edu.school21.engine.shaders.ShaderProgram;
import edu.school21.game.models.GameObject;
import edu.school21.utils.Utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Renderer {
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final Vector3f FOG_COLOR = new Vector3f(0.8f);
    private static final float FOG_DENSITY = 0.025f;
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final String PATH_TO_VERTEX_SHADER_FILE = "/shaders/vertex.glsl";
    private static final String PATH_TO_FRAGMENT_SHADER_FILE = "/shaders/fragment.glsl";
    private final Transformation transformation = new Transformation();
    private final Fog fog = new Fog(true, FOG_COLOR, FOG_DENSITY);
    private ShaderProgram shaderProgram = null;

    public void init() throws IOException {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource(PATH_TO_VERTEX_SHADER_FILE));
        shaderProgram.createFragmentShader(Utils.loadResource(PATH_TO_FRAGMENT_SHADER_FILE));
        shaderProgram.link();
        shaderProgram.createUniform("projection");
        shaderProgram.createUniform("model_view");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("color");
        shaderProgram.createUniform("useColor");
        shaderProgram.createFogUniform("fog");
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void switchFog() {
        fog.setActive(!fog.isActive());
    }

    public void render(float aspect, Camera camera, List<GameObject> gameObjects) {
        clear();

        shaderProgram.bind();

        Matrix4f projection = transformation.getPerspectiveProjection(FOV, aspect, Z_NEAR, Z_FAR);
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("projection", projection);
        shaderProgram.setUniform("texture_sampler", 0);
        shaderProgram.setUniform("fog", fog);

        for (GameObject gameObject : gameObjects) {
            Matrix4f world = transformation.getModelViewMatrix(gameObject, viewMatrix);

            shaderProgram.setUniform("model_view", world);
            shaderProgram.setUniform("color", gameObject.getMesh().getColor());
            shaderProgram.setUniform("useColor", gameObject.isTextured());
            gameObject.render();
        }
        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
