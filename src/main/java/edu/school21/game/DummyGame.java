package edu.school21.game;

import edu.school21.engine.render.Camera;
import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Renderer;
import edu.school21.engine.render.Texture;
import edu.school21.engine.shaders.fragment.struct.Attenuation;
import edu.school21.engine.shaders.fragment.struct.DirectionalLight;
import edu.school21.engine.shaders.fragment.struct.Material;
import edu.school21.engine.shaders.fragment.struct.PointLight;
import edu.school21.engine.window.MouseInput;
import edu.school21.engine.window.Window;
import edu.school21.utils.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements GameLogic {
    private static float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private final Renderer renderer;
    private final List<GameItem> gameItems;
    private final Vector3f cameraInc;
    private final Camera camera;
    private Vector3f ambientLight;
    private PointLight pointLight;
    private DirectionalLight directionalLight;
    private float lightAngle;


    public DummyGame() {
        this.renderer = new Renderer();
        this.gameItems = new LinkedList<>();
        this.cameraInc = new Vector3f();
        this.camera = new Camera();
    }

    @Override
    public void init() throws IOException {
        renderer.init();

        float reflectance = 0.1f;

        Mesh mesh = OBJLoader.loadMesh("/cube.obj");
        Texture texture = new Texture("src/main/resources/grassblock.png");
        Material material = new Material(texture);
        material.setAmbient(new Vector4f(0.7f, 0.7f, 0.7f, 1f));
        material.setReflectance(reflectance);

        mesh.setMaterial(material);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.25f);
        gameItem.setPosition(0, -2, -3);
        gameItem.setRotation(0, 0, 0);
        gameItems.add(gameItem);

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 0.1f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        Attenuation att = new Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        pointLight.setPosition(camera.getPosition());

        lightPosition = new Vector3f(-1, 0, 0);
        lightColour = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);

        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            CAMERA_POS_STEP = 0.5f;
        } else if (window.isKeyReleased(GLFW_KEY_LEFT_SHIFT)) {
            CAMERA_POS_STEP = 0.05f;
        }
    }

    @Override
    public void update(MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        lightAngle += 0.5f;

        if (lightAngle > 90) {
            directionalLight.setIntensity(0);

            if (lightAngle >= 90) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;

            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);

        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);

//        gameItems.get(0).setRotation(0, 5f * (float) glfwGetTime(), 0);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems.toArray(GameItem[]::new), ambientLight, pointLight, directionalLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanUp();
        for (GameItem gameItem : gameItems) {
            gameItem.cleanup();
        }
    }
}
