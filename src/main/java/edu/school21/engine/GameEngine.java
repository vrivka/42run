package edu.school21.engine;

import edu.school21.engine.window.MouseHandler;
import edu.school21.game.GameLogic;
import edu.school21.engine.window.Window;

public class GameEngine implements Runnable {
    private final Window window;
    private final GameLogic gameLogic;
    private final MouseHandler mouseHandler;

    public GameEngine(String windowTitle, int width, int height, GameLogic gameLogic) {
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

    protected void gameLoop() {
        while (!window.isShouldClose()) {
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
