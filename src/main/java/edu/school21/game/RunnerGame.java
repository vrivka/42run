package edu.school21.game;

import edu.school21.engine.render.Camera;
import edu.school21.engine.render.Renderer;
import edu.school21.engine.window.MouseHandler;
import edu.school21.engine.window.Window;
import edu.school21.game.hud.HUDHandler;
import edu.school21.game.hud.MenuType;
import edu.school21.game.models.GameObject;
import edu.school21.game.models.PipelineHandler;
import edu.school21.game.models.environments.Floor;
import edu.school21.game.models.players.Player;
import edu.school21.game.utils.MeshContainer;
import edu.school21.game.utils.TextureContainer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class RunnerGame implements GameLogic {
    private static float CAMERA_POS_STEP = 0.05f;
    private static final Vector3f CAMERA_DEFAULT_POSITION = new Vector3f(0, 2.9f, 0);
    private static final Vector3f CAMERA_DEFAULT_ROTATION = new Vector3f(30.5f, 0, 0);
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float ACCELERATION = 0.005f;
    private static final int ACCELERATION_INTERVAL = 150;
    public static MenuType menu;
    public static boolean inPause = true;
    public static boolean cameraDefault = false;
    public static float gameSpeed = 0.01f;
    public static float savedSpeed = 0.01f;
    public static float scores = 0;
    private final Renderer renderer;
    private final PipelineHandler pipelineHandler;
    private final HUDHandler hudHandler;
    private Player player;
    private Floor floor;
    private final Vector3f cameraInc;
    private final Camera camera;

    public RunnerGame() {
        this.renderer = new Renderer();
        this.pipelineHandler = new PipelineHandler();
        this.cameraInc = new Vector3f();
        this.camera = new Camera(CAMERA_DEFAULT_POSITION, CAMERA_DEFAULT_ROTATION);
        this.hudHandler = new HUDHandler(camera);
        menu = MenuType.MAIN;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();
        pipelineHandler.init();
        player = new Player();
        floor = new Floor();
    }
    @Override
    public void input(Window window, MouseHandler mouseHandler) {
        cameraInc.set(0);

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
            player.setInRoll(true);
        }
        if (window.isKeyReleased(GLFW_KEY_DOWN)) {
            player.setInRoll(false);
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            player.setInAir();
        }
    }

    @Override
    public void update(Window window, float aspect, MouseHandler mouseHandler) {
        hudHandler.handle(window, mouseHandler, pipelineHandler);
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseHandler.isRightButtonPressed()) {
            Vector2f rotVec = mouseHandler.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        if (inPause) {
            gameSpeed = 0;
        } else {
            gameSpeed = savedSpeed;
        }
        scores += gameSpeed;

        if (scores != 0 && (int) scores % ACCELERATION_INTERVAL == 0) {
            savedSpeed += ACCELERATION;
        }

        if (pipelineHandler.getForwardObstacle().intersect(player)) {
            menu = MenuType.DEAD;
            glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }

        if (cameraDefault) {
            camera.setPosition(CAMERA_DEFAULT_POSITION);
            camera.setRotation(CAMERA_DEFAULT_ROTATION);
            cameraDefault = false;
        }

        hudHandler.update((int) scores, aspect);
        pipelineHandler.update();
        player.update(scores);
    }

    @Override
    public void render(float aspect) {
        List<GameObject> gameObjectList = pipelineHandler.collect();

        gameObjectList.add(player);
        gameObjectList.add(floor);
        gameObjectList.addAll(hudHandler.collect());
        renderer.render(aspect, camera, gameObjectList);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        MeshContainer.cleanup();
        TextureContainer.cleanup();
    }
}
