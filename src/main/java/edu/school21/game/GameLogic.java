package edu.school21.game;

import edu.school21.engine.window.MouseInput;
import edu.school21.engine.window.Window;

import java.io.IOException;

public interface GameLogic {
    void init() throws IOException;
    void input(Window window, MouseInput mouseInput);
    void update(MouseInput mouseInput);
    void render(Window window);
    void cleanup();
}
