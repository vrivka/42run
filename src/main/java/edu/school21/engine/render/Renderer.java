package edu.school21.engine.render;

import edu.school21.engine.shaders.ShaderProgram;
import edu.school21.engine.window.Window;
import edu.school21.game.GameItem;
import edu.school21.utils.Utils;
import org.joml.Matrix4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private ShaderProgram shaderProgram;
    private final Transformation transformation;

    public Renderer() {
        this.transformation = new Transformation();
    }

    public void init() throws IOException {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.glsl"));
        shaderProgram.link();
        shaderProgram.createUniform("projection");
        shaderProgram.createUniform("model_view");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("color");
        shaderProgram.createUniform("useColor");
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameItem[] gameItems) {
        clear();

        shaderProgram.bind();

        Matrix4f projection = transformation.getProjection(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("projection", projection);
        shaderProgram.setUniform("texture_sampler", 0);

        for (GameItem gameItem : gameItems) {
            Matrix4f world = transformation.getModelView(gameItem, viewMatrix);
            Mesh mesh = gameItem.getMesh();

            shaderProgram.setUniform("model_view", world);
            shaderProgram.setUniform("color", mesh.getColor());
            shaderProgram.setUniform("useColor", mesh.isTextured() ? 0 : 1);
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void cleanUp() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
