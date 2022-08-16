package edu.school21.engine.window;

import edu.school21.engine.window.exceptions.WindowInitFailException;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.io.Closeable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Closeable {
    private final String windowTitle;
    private long window;
    private int width;
    private int height;
    private boolean resized;

    public Window(String windowTitle, int width, int height) {
        this.windowTitle = windowTitle;
        this.width = width;
        this.height = height;
        this.resized = false;
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

    public void setClearColor(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public boolean isResized() {
        return resized;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new WindowInitFailException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(this.width, this.height, windowTitle, NULL, NULL);

        if (window == NULL) {
            throw new WindowInitFailException("Failed to create the GLFW window");
        }

        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
        });

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> glViewport(0, 0, width, height));

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);
        GL.createCapabilities();
        setClearColor(0.5f, 0.5f, 0.5f, 0f);
        glEnable(GL_DEPTH_TEST);
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
