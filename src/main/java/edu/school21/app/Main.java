package edu.school21.app;

import edu.school21.engine.GameEngine;
import edu.school21.game.GameLogic;
import edu.school21.game.RunnerGame;

public class Main {
    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 900;
    public static final String WINDOW_TITLE = "42run";
    public static final int ERROR_EXIT_CODE = -1;

    public static void main(String[] args) {
        try {
            GameLogic gameLogic = new RunnerGame();
            GameEngine gameEng = new GameEngine(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, gameLogic);
            gameEng.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(ERROR_EXIT_CODE);
        }
    }
}
