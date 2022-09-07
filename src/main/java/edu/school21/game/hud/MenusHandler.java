package edu.school21.game.hud;

import edu.school21.engine.window.MouseHandler;
import edu.school21.engine.window.Window;
import edu.school21.game.RunnerGame;
import edu.school21.game.models.PipelineHandler;

import org.joml.Vector2d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static edu.school21.game.RunnerGame.inPause;
import static edu.school21.game.RunnerGame.menu;
import static edu.school21.game.utils.types.ButtonType.*;
import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.types.MeshType.BUTTON;
import static edu.school21.game.utils.types.MeshType.TITLE;
import static edu.school21.game.utils.TextureContainer.buttonTextures;
import static edu.school21.game.utils.TextureContainer.titleTextures;
import static edu.school21.game.utils.types.TitleType.GAME_OVER;
import static edu.school21.game.utils.types.TitleType.MAIN_MENU;

import static org.lwjgl.glfw.GLFW.*;

public class MenusHandler {
    private final List<HudElement> elements;
    private final Vector3f cameraRotation;

    public MenusHandler(Vector3f cameraRotation) {
        this.cameraRotation = cameraRotation;
        this.elements = new ArrayList<>();
    }

    public List<HudElement> collect() {
        return elements;
    }

    public void update() {
        if (menu.equals(MenuType.IN_GAME)) {
            return;
        }
        HudElement upButton = new HudElement(meshes.get(BUTTON), null, cameraRotation);
        HudElement downButton = new HudElement(meshes.get(BUTTON), null, cameraRotation);
        HudElement title = new HudElement(meshes.get(TITLE), null, cameraRotation);

        upButton.setScale(0.5f);
        upButton.setPosition(0.25f, 2.5f, -1f);

        downButton.setScale(0.5555f);
        downButton.setPosition(0.275f, 2.2f, -1f);

        title.setScale(0.7f);
        title.setPosition(0.7f, 2.88f, -1f);


        if (menu.equals(MenuType.MAIN)) {
            upButton.setTexture(buttonTextures.get(NEW_GAME));
            downButton.setTexture(buttonTextures.get(EXIT));
            title.setTexture(titleTextures.get(MAIN_MENU));
            elements.add(title);
        } else if (menu.equals(MenuType.PAUSE)) {
            upButton.setTexture(buttonTextures.get(CONTINUE));
            downButton.setTexture(buttonTextures.get(EXIT));
        } else if (menu.equals(MenuType.GAME_OVER)) {
            upButton.setTexture(buttonTextures.get(TRY_AGAIN));
            downButton.setTexture(buttonTextures.get(EXIT));
            title.setTexture(titleTextures.get(GAME_OVER));
            elements.add(title);
        }
        elements.add(upButton);
        elements.add(downButton);
    }

    public void clear() {
        elements.clear();
    }

    public void handle(Window window, MouseHandler mouseHandler, PipelineHandler pipelineHandler) {
        Vector2d mousePos = mouseHandler.getCurrentPos();
        Predicate<Void> upButton = (v) -> mouseHandler.isLeftButtonPressed() && ((mousePos.x >= 620 && mousePos.x <= 980) && (mousePos.y >= 330 && mousePos.y <= 480));
        Predicate<Void> downButton = (v) -> mouseHandler.isLeftButtonPressed() && ((mousePos.x >= 620 && mousePos.x <= 980) && (mousePos.y >= 510 && mousePos.y <= 650));

        if (menu.equals(MenuType.MAIN) || menu.equals(MenuType.GAME_OVER)) {
            inPause = true;
            glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

            if (upButton.test(null)) {
                RunnerGame.scores = 0;
                RunnerGame.increaseCount = 0;
                inPause = false;
                menu = MenuType.IN_GAME;
                glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                RunnerGame.savedSpeed = RunnerGame.GAME_DEFAULT_SPEED;
                pipelineHandler.clear();
            } else if (downButton.test(null)) {
                glfwSetWindowShouldClose(window.getWindow(), true);
            }
        } else if (menu.equals(MenuType.PAUSE) && inPause) {
            glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

            if (upButton.test(null)) {
                inPause = false;
                glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                menu = MenuType.IN_GAME;
            } else if (downButton.test(null)) {
                glfwSetWindowShouldClose(window.getWindow(), true);
            }
        }
    }
}
