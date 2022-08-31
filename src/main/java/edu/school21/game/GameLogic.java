package edu.school21.game;

import edu.school21.engine.window.MouseHandler;
import edu.school21.engine.window.Window;

public interface GameLogic {
    void init(Window window) throws Exception;
    void input(Window window, MouseHandler mouseHandler);
    void update(Window window, MouseHandler mouseHandler);
    void render(float aspect);
    void cleanup();
}
