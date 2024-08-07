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
import edu.school21.game.utils.types.MeshType;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class RunnerGame implements GameLogic {
    private static final Vector3f CAMERA_DEFAULT_POSITION = new Vector3f(0, 2.9f, 0);
    private static final Vector3f CAMERA_DEFAULT_ROTATION = new Vector3f(30.5f, 0, 0);
    private static final int ACCELERATION_MULTIPLIER = 2;
    private static final float ADDITIONAL_SCORE = 50f;
    private static final float ACCELERATION = 0.025f;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float ACCELERATION_AFTER_TRIP = 0.0002f;
    private static boolean cameraOnDefaultPosition = false;
    public static final float GAME_DEFAULT_SPEED = 0.1f;
    public static boolean inPause = true;
    public static MenuType menu = MenuType.MAIN;
    public static float savedSpeed = GAME_DEFAULT_SPEED;
    public static float gameSpeed = GAME_DEFAULT_SPEED;
    public static float scores = 0;
    public static float increaseCount = 0;
    private float targetScore;
    private final PipelineHandler pipelineHandler;
    private final HUDHandler hudHandler;
    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;
    private Player player;
    private Floor floor;

    public RunnerGame() {
        this.renderer = new Renderer();
        this.pipelineHandler = new PipelineHandler();
        this.cameraInc = new Vector3f();
        this.camera = new Camera(CAMERA_DEFAULT_POSITION, CAMERA_DEFAULT_ROTATION);
        this.hudHandler = new HUDHandler(camera);
        targetScore = 200f;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();
        pipelineHandler.init();
        player = new Player();
        floor = new Floor();
        glfwSetKeyCallback(window.getWindow(), (win, key, scancode, action, mods) -> {
            if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
                if (menu.equals(MenuType.IN_GAME)) {
                    menu = MenuType.PAUSE;
                    inPause = true;
                } else if (menu.equals(MenuType.PAUSE)) {
                    menu = MenuType.IN_GAME;
                    inPause = false;
                }
            }
            if (window.isKeyPressed(GLFW_KEY_C)) {
                cameraOnDefaultPosition = true;
            }
            if (window.isKeyPressed(GLFW_KEY_F)) {
                renderer.switchFog();
            }
        });
    }

    @Override
    public void input(Window window, MouseHandler mouseHandler) {
        cameraInc.set(0);

        if (window.isKeyPressed(GLFW_KEY_D)) {
            player.moveRight();
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            player.moveLeft();
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            player.setInRoll(true);
        }
        if (window.isKeyReleased(GLFW_KEY_S)) {
            player.setInRoll(false);
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            player.setInAir();
        }

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT_CONTROL)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(Window window, MouseHandler mouseHandler) {
        hudHandler.handle(window, mouseHandler, pipelineHandler);
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseHandler.isRightButtonPressed()) {
            Vector2f rotVec = mouseHandler.getDisplaceVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        if (inPause) {
            gameSpeed = 0;
        } else {
            gameSpeed = savedSpeed;
        }
        scores += gameSpeed;

        if (scores > targetScore) {
            savedSpeed += ACCELERATION;
            targetScore *= ACCELERATION_MULTIPLIER;
            increaseCount++;
        }

        if (pipelineHandler.getForwardObstacle().intersect(player)) {
            if (pipelineHandler.getForwardObstacle().isTypeOf(MeshType.SIGN)) {
                savedSpeed -= ACCELERATION;
            } else if (pipelineHandler.getForwardObstacle().isTypeOf(MeshType.COLLECTABLE)) {
                pipelineHandler.popCollectable();
                scores += ADDITIONAL_SCORE;
            } else {
                menu = MenuType.GAME_OVER;
                targetScore = 200f;
            }
            if (savedSpeed < 0) {
                menu = MenuType.GAME_OVER;
                targetScore = 200f;
            }
        }

        if (savedSpeed < GAME_DEFAULT_SPEED + ACCELERATION * increaseCount) {
            savedSpeed += ACCELERATION_AFTER_TRIP;
        }

        if (cameraOnDefaultPosition) {
            camera.setPosition(CAMERA_DEFAULT_POSITION);
            camera.setRotation(CAMERA_DEFAULT_ROTATION);
            cameraOnDefaultPosition = false;
        }

        hudHandler.update((int) scores, window.getAspect());
        pipelineHandler.update();
        player.update(scores);
    }

    private List<GameObject> getGameObjects() {
        List<GameObject> gameObjectList = pipelineHandler.collect();

        gameObjectList.add(player);
        gameObjectList.add(floor);
        gameObjectList.addAll(hudHandler.collect());
        return gameObjectList;
    }

    @Override
    public void render(float aspect) {
        renderer.render(aspect, camera, getGameObjects());
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        MeshContainer.cleanup();
        TextureContainer.cleanup();
    }
}
