package edu.school21.engine;

import edu.school21.engine.window.MouseInput;
import edu.school21.game.GameLogic;
import edu.school21.engine.window.Window;

import java.io.IOException;

public class GameEngine implements Runnable {
    private final Window window;
    private final GameLogic gameLogic;
    private final MouseInput mouseInput;
    private float aspect;

    public GameEngine(String windowTitle, int width, int height, GameLogic gameLogic) {
        window = new Window(windowTitle, width, height);
        this.gameLogic = gameLogic;
        this.mouseInput = new MouseInput();
        this.aspect = (float)width / (float)height;
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        gameLogic.cleanup();
        window.close();
    }

    protected void init() throws IOException {
        window.init();
        mouseInput.init(window);
        try {
            gameLogic.init(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void gameLoop() {
        while (!window.isShouldClose()) {
            input();
            update();
            render();
        }
    }

    protected void input() {
        mouseInput.input();
        gameLogic.input(window, mouseInput);
    }

    protected void update() {
        aspect = (float)window.getWidth() / (float)window.getHeight();
        gameLogic.update(window, aspect, mouseInput);
    }

    protected void render() {
        gameLogic.render(aspect);
        window.update();
    }
}
