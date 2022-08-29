package edu.school21.engine.window;

import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler {
    private final Vector2d previousPos = new Vector2d(-1);
    private final Vector2d currentPos = new Vector2d();
    private final Vector2f displVec = new Vector2f();
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindow(), (win, xPos, yPos) -> currentPos.set(xPos, yPos));
        glfwSetCursorEnterCallback(window.getWindow(), (win, entered) -> inWindow = entered);
        glfwSetMouseButtonCallback(window.getWindow(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;

        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltaX = currentPos.x - previousPos.x;
            double deltaY = currentPos.y - previousPos.y;
            boolean rotateX = deltaX != 0;
            boolean rotateY = deltaY != 0;

            if (rotateX) {
                displVec.y = (float) deltaX;
            }

            if (rotateY) {
                displVec.x = (float) deltaY;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public Vector2d getCurrentPos() {
        return currentPos;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
