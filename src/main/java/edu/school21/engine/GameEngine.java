package edu.school21.engine;

import edu.school21.engine.window.MouseInput;
import edu.school21.game.GameLogic;
import edu.school21.engine.window.Window;

import java.io.IOException;

public class GameEngine implements Runnable {
    private final Window window;
    private final GameLogic gameLogic;
    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, GameLogic gameLogic) {
        window = new Window(windowTitle, width, height);
        this.gameLogic = gameLogic;
        this.mouseInput = new MouseInput();
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
        gameLogic.init();
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
        gameLogic.update(mouseInput);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
