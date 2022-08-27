package edu.school21.app;

import edu.school21.engine.GameEngine;
import edu.school21.game.RunnerGame;
import edu.school21.game.GameLogic;

public class Main {
    public static void main(String[] args) {
        try {
            GameLogic gameLogic = new RunnerGame();
            GameEngine gameEng = new GameEngine("42run", 1600, 900, gameLogic);
            gameEng.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
