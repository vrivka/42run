package edu.school21.engine;

import edu.school21.engine.window.MouseHandler;
import edu.school21.game.GameLogic;
import edu.school21.engine.window.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GameEngine implements Runnable {
    private final double FPS;
    private final Window window;
    private final GameLogic gameLogic;
    private final MouseHandler mouseHandler;

    public GameEngine(String windowTitle, int width, int height, double fps, GameLogic gameLogic) {
        this.FPS = fps;
        this.window = new Window(windowTitle, width, height);
        this.gameLogic = gameLogic;
        this.mouseHandler = new MouseHandler();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        gameLogic.cleanup();
        window.cleanup();
    }

    protected void init() throws Exception {
        window.init();
        mouseHandler.init(window);
        gameLogic.init(window);
    }

    protected void gameLoop() throws InterruptedException {
        double sigleFrameTime = 1.0 / FPS;
        double lastTime = 0.0;
        double currentTime;
        double sleepTime;

        while (!window.isShouldClose()) {
            currentTime = glfwGetTime();
            if (currentTime - lastTime < sigleFrameTime) {
                sleepTime = (sigleFrameTime - (currentTime - lastTime)) * 1000.0;
                Thread.sleep((long) sleepTime);
            }
            lastTime = glfwGetTime();
            input();
            update();
            render();
        }
    }

    protected void input() {
        mouseHandler.input();
        gameLogic.input(window, mouseHandler);
    }

    protected void update() {
        gameLogic.update(window, mouseHandler);
    }

    protected void render() {
        gameLogic.render(window.getAspect());
        window.update();
    }
}
