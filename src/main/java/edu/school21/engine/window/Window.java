package edu.school21.engine.window;

import edu.school21.engine.window.exceptions.WindowCreationFailException;
import edu.school21.engine.window.exceptions.WindowInitFailException;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static final Vector4f DEFAULT_CLEAR_COLOR = new Vector4f();
    private final String windowTitle;
    private final float aspect;
    private final int width;
    private final int height;
    private long window;

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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(width, height, windowTitle, NULL, NULL);

        if (window == NULL) {
            throw new WindowCreationFailException("Failed to create the GLFW window");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(0);

        glfwShowWindow(window);

        createCapabilities();

        glClearColor(DEFAULT_CLEAR_COLOR.x, DEFAULT_CLEAR_COLOR.y, DEFAULT_CLEAR_COLOR.z, DEFAULT_CLEAR_COLOR.w);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
