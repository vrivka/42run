package edu.school21.game;

import edu.school21.engine.render.Camera;
import edu.school21.engine.render.Renderer;
import edu.school21.engine.window.MouseInput;
import edu.school21.engine.window.Window;
import edu.school21.game.hud.HUDHandler;
import edu.school21.game.hud.HudElement;
import edu.school21.utils.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class RunnerGame implements GameLogic {
    private static float CAMERA_POS_STEP = 0.05f;
    public static float SCROLL_SPEED = 0.1f;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private final Renderer renderer;
    private final PipelineHandler pipelineHandler;
    private final HUDHandler hudHandler;
    private Player player;
    private Floor floor;
    private final Vector3f cameraInc;
    private final Camera camera;
    public static boolean inPause = false;
    public static boolean cameraDefault = false;

    public RunnerGame() {
        this.renderer = new Renderer();
        this.pipelineHandler = new PipelineHandler();
        this.cameraInc = new Vector3f();
        this.camera = new Camera(new Vector3f(0, 2.9f, 0), new Vector3f(30.5f, 0, 0));
        this.hudHandler = new HUDHandler(camera);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();
        hudHandler.init();
        pipelineHandler.init();
        player = new Player(OBJLoader.loadMesh("player_cube.obj"));
        floor = new Floor();
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

        if (window.isKeyPressed(GLFW_KEY_E)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            cameraInc.y = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            CAMERA_POS_STEP = 0.5f;
        } else if (window.isKeyReleased(GLFW_KEY_LEFT_SHIFT)) {
            CAMERA_POS_STEP = 0.05f;
        }

        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            player.moveRight();
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            player.moveLeft();
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            player.setInSlide(true);
        }
        if (window.isKeyReleased(GLFW_KEY_DOWN)) {
            player.setInSlide(false);
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            player.setInAir();
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

        if (inPause) {
            SCROLL_SPEED = 0;
        } else {
            SCROLL_SPEED = 0.1f;
        }

        if (pipelineHandler.getForwardObstacle().intersect(player)) {
            System.out.println("Ups...");
        }

        if (cameraDefault) {
            camera.setPosition(0, 2.9f, 0);
            camera.setRotation(30.5f, 0, 0);
            cameraDefault = false;
        }

        pipelineHandler.update();
        player.update();
    }

    @Override
    public void render(Window window) {
        Renderer.clear();
        List<GameObject> gameObjectList = pipelineHandler.collect();

        gameObjectList.add(player);
        gameObjectList.add(floor);
        gameObjectList.addAll(hudHandler.collect());
        renderer.render(window, camera, gameObjectList);
    }

    @Override
    public void cleanup() {
        renderer.cleanUp();
        pipelineHandler.cleanup();
    }
}
