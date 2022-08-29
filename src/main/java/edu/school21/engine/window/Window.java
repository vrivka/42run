package edu.school21.engine.window;

import edu.school21.engine.window.exceptions.WindowInitFailException;
import edu.school21.game.RunnerGame;
import edu.school21.game.hud.MenuType;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.io.Closeable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Closeable {
    private static final Vector4f DEFAULT_CLEAR_COLOR = new Vector4f();
    private final String windowTitle;
    private long window;
    private final float aspect;
    private final int width;
    private final int height;

    public Window(String windowTitle, int width, int height) {
        this.windowTitle = windowTitle;
        this.width = width;
        this.height = height;
        this.aspect = (float) width / (float) height;
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public boolean isKeyReleased(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_RELEASE;
    }

    public boolean isShouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void setClearColor(Vector4f color) {
        glClearColor(color.x, color.y, color.z, color.w);
    }

    public float getAspect() {
        return aspect;
    }

    public long getWindow() {
        return window;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new WindowInitFailException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_SAMPLES, 8);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); //todo resizeable false?

        window = glfwCreateWindow(width, height, windowTitle, NULL, NULL);

        if (window == NULL) {
            throw new WindowInitFailException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                if (RunnerGame.menu.equals(MenuType.IN_GAME)) {
                    RunnerGame.menu = MenuType.PAUSE;
                    RunnerGame.inPause = true;
                } else if (RunnerGame.menu.equals(MenuType.PAUSE)) {
                    RunnerGame.menu = MenuType.IN_GAME;
                    RunnerGame.inPause = false;
                }
            }
            if (isKeyPressed(GLFW_KEY_C)) {//todo remove?
                RunnerGame.cameraDefault = true;
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();

        setClearColor(DEFAULT_CLEAR_COLOR);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    @Override
    public void close() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
