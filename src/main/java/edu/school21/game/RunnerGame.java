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
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

import static org.lwjgl.glfw.GLFW.*;

public class RunnerGame implements GameLogic {
    private static float CAMERA_POS_STEP = 0.05f;
    private static final Vector3f CAMERA_DEFAULT_POSITION = new Vector3f(0, 2.9f, 0);
    private static final Vector3f CAMERA_DEFAULT_ROTATION = new Vector3f(30.5f, 0, 0);
    public static float SCROLL_SPEED = 0.1f;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private final Renderer renderer;
    private final PipelineHandler pipelineHandler;
    private final HUDHandler hudHandler;
    public static MenuType menu;
    private Player player;
    private Floor floor;
    private final Vector3f cameraInc;
    private final Camera camera;
    public static boolean inPause = true;
    public static boolean cameraDefault = false;
    private float scores = 0;

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
    public void update(Window window, float aspect, MouseHandler mouseHandler) {
        Vector2d mousePos = mouseHandler.getCurrentPos();
        Predicate<Void> upButton = (v) -> mouseHandler.isLeftButtonPressed() && ((mousePos.x >= 620 && mousePos.x <= 980) && (mousePos.y >= 330 && mousePos.y <= 480));
        Predicate<Void> downButton = (v) -> mouseHandler.isLeftButtonPressed() && ((mousePos.x >= 620 && mousePos.x <= 980) && (mousePos.y >= 510 && mousePos.y <= 650));

        if (menu.equals(MenuType.MAIN) || menu.equals(MenuType.DEAD)) {
            inPause = true;
            glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

            if (upButton.test(null)) {
                scores = 0;
                inPause = false;
                menu = MenuType.IN_GAME;
                glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                pipelineHandler.clear();
            } else if (downButton.test(null)) {
                glfwSetWindowShouldClose(window.getWindow(), true);
            }
        } else if (menu.equals(MenuType.PAUSE) && inPause) {
            glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

            if (upButton.test(null)) {
                inPause = false;
                glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                menu = MenuType.IN_GAME;
            } else if (downButton.test(null)) {
                glfwSetWindowShouldClose(window.getWindow(), true);
            }
        }

        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseHandler.isRightButtonPressed()) {
            Vector2f rotVec = mouseHandler.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        if (inPause) {
            SCROLL_SPEED = 0;
        } else {
            SCROLL_SPEED = 0.1f;
        }
        scores += SCROLL_SPEED;

        if (pipelineHandler.getForwardObstacle().intersect(player)) {
            menu = MenuType.DEAD;
            glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }

        if (cameraDefault) {
            camera.setPosition(CAMERA_DEFAULT_POSITION);
            camera.setRotation(CAMERA_DEFAULT_ROTATION);
            cameraDefault = false;
        }

        hudHandler.update(menu, (int) scores, aspect);
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

//620 330
//980 480

//620 510
//980 650
