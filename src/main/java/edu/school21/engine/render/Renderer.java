package edu.school21.engine.render;

import edu.school21.engine.shaders.ShaderProgram;
import edu.school21.engine.shaders.fragment.struct.PointLight;
import edu.school21.engine.window.Window;
import edu.school21.game.GameItem;
import edu.school21.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private ShaderProgram shaderProgram;
    private final Transformation transformation;
    private float specularPower;

    public Renderer() {
        this.transformation = new Transformation();
        specularPower = 10f;
    }

    public void init() throws IOException {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.glsl"));
        shaderProgram.link();
        shaderProgram.createUniform("projection");
        shaderProgram.createUniform("model_view");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("specular_power");
        shaderProgram.createUniform("ambient_light");
        shaderProgram.createPointLightUniform("point_light");
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameItem[] gameItems, Vector3f ambientLight, PointLight pointLight) {
        clear();

        shaderProgram.bind();

        Matrix4f projection = transformation.getProjection(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("projection", projection);

        // Update Light Uniforms
        shaderProgram.setUniform("ambient_light", ambientLight);
        shaderProgram.setUniform("specular_power", specularPower);
        // Get a copy of the light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("point_light", currPointLight);

        shaderProgram.setUniform("texture_sampler", 0);

        for (GameItem gameItem : gameItems) {
            Matrix4f world = transformation.getModelView(gameItem, viewMatrix);
            Mesh mesh = gameItem.getMesh();

            shaderProgram.setUniform("model_view", world);
            shaderProgram.setUniform("material", mesh.getMaterial());
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
