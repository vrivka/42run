package edu.school21.game;

import edu.school21.engine.render.Camera;
import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Renderer;
import edu.school21.engine.render.Texture;
import edu.school21.engine.window.MouseInput;
import edu.school21.engine.window.Window;
import edu.school21.utils.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;

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

    public DummyGame() {
        this.renderer = new Renderer();
        this.gameItems = new LinkedList<>();
        this.cameraInc = new Vector3f();
        this.camera = new Camera();
    }

    @Override
    public void init() throws IOException {
        renderer.init();

        float[] vertices = new float[]{
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,

                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.0f,
                0.5f, 0.5f,

                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f
        };
        int[] indices = new int[]{
                0, 1, 3,
                3, 1, 2,

                8, 10, 11,
                9, 8, 11,

                12, 13, 7,
                5, 12, 7,

                14, 15, 6,
                4, 14, 6,

                16, 18, 19,
                17, 16, 19,

                4, 6, 7,
                5, 4, 7
        };

        Mesh mesh = new Mesh(vertices, indices, textCoords, new float[]{});
        mesh.setTexture(new Texture("src/main/resources/grassblock.png"));
        mesh.setColor(new Vector3f(0f, 1f, 0f));
        GameItem gameItem1 = new GameItem(mesh);
        gameItem1.setScale(0.5f);
        gameItem1.setPosition(0, 0, -2);

        GameItem gameItem2 = new GameItem(mesh);
        gameItem2.setScale(0.5f);
        gameItem2.setPosition(0.5f, 0.5f, -2);

        GameItem gameItem3 = new GameItem(mesh);
        gameItem3.setScale(0.5f);
        gameItem3.setPosition(0, 0, -2.5f);

        GameItem gameItem4 = new GameItem(mesh);
        gameItem4.setScale(0.5f);

        gameItem4.setPosition(0.5f, 0, -2.5f);
        gameItems.add(gameItem1);
        gameItems.add(gameItem2);
        gameItems.add(gameItem3);
        gameItems.add(gameItem4);

        mesh = OBJLoader.loadMesh("/teapot2.obj");
        mesh.setColor(new Vector3f(1f, 1f, 0f));
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.1f);
        gameItem.setPosition(0, 0.4f, -2);
        gameItem.setRotation(0, 0.5f, 0);
        gameItems.add(gameItem);
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
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems.toArray(GameItem[]::new));
    }

    @Override
    public void cleanup() {
        renderer.cleanUp();
        for (GameItem gameItem : gameItems) {
            gameItem.cleanup();
        }
    }
}
