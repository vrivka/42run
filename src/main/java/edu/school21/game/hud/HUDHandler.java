package edu.school21.game.hud;

import edu.school21.engine.render.Camera;
import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Texture;
import edu.school21.game.GameObject;
import edu.school21.utils.OBJLoader;

import java.util.*;

public class HUDHandler {
    private final List<HudElement> elements;
    private final Map<Integer, Texture> numberTextures;
    private final Map<ButtonType, Texture> buttonsTextures;
    private final Map<Integer, Texture> titleTextures;
    private Mesh counterMesh;
    private Mesh buttonMesh;
    private Mesh titleMesh;
    private final Camera camera;

    public HUDHandler(Camera camera) {
        this.camera = camera;
        this.elements = new ArrayList<>();
        this.numberTextures = new HashMap<>();
        this.buttonsTextures = new HashMap<>();
        this.titleTextures = new HashMap<>();
    }

    public List<GameObject> collect() {
        List<GameObject> result = new ArrayList<>();

        result.addAll(elements);
        return result;
    }

    public void init() throws Exception {
        counterMesh = OBJLoader.loadMesh("number.obj");
        buttonMesh = OBJLoader.loadMesh("button.obj");
        titleMesh = OBJLoader.loadMesh("title.obj");

        for (int i = 0; i < 10; i++) {
            numberTextures.put(i, new Texture("HUD/" + i + ".png"));
        }
        buttonsTextures.put(ButtonType.MAIN, new Texture("HUD/MainMenuButton.png"));
        buttonsTextures.put(ButtonType.NEW, new Texture("HUD/NewGameButton.png"));
        buttonsTextures.put(ButtonType.AGAIN, new Texture("HUD/TryAgainButton.png"));
        buttonsTextures.put(ButtonType.EXIT, new Texture("HUD/ExitButton.png"));
        titleTextures.put(0, new Texture("HUD/GameTitle.png"));
        titleTextures.put(1, new Texture("HUD/GameOverTitle.png"));
    }

    public void showCount(int count, float aspect) {
        for (int i = 0; count != 0; count /= 10, i++) {
            HudElement hudElement = new HudElement(camera, counterMesh, numberTextures.get(count % 10));
            hudElement.setPosition((aspect / 2) - (i * 0.05f), 2.88f, -1f);

            elements.add(hudElement);
        }
    }

    public void showMenu(MenuType menu) {
        if (menu.equals(MenuType.IN_GAME)) {
            return ;
        }
        HudElement upButton = new HudElement(camera, buttonMesh, null);
        upButton.setScale(0.5f);
        upButton.setPosition(0.25f, 2.5f, -1f);
        HudElement downButton = new HudElement(camera, buttonMesh, null);
        downButton.setScale(0.5555f);
        downButton.setPosition(0.275f, 2.2f, -1f);
        HudElement title = new HudElement(camera, titleMesh, null);
        title.setScale(0.7f);
        title.setPosition(0.7f, 2.88f, -1f);


        if (menu.equals(MenuType.MAIN)) {
            upButton.setTexture(buttonsTextures.get(ButtonType.NEW));
            downButton.setTexture(buttonsTextures.get(ButtonType.EXIT));
            title.setTexture(titleTextures.get(0));//todo defines
            elements.add(title);
        } else if (menu.equals(MenuType.PAUSE)) {
            upButton.setTexture(buttonsTextures.get(ButtonType.MAIN));
            downButton.setTexture(buttonsTextures.get(ButtonType.EXIT));
        } else if (menu.equals(MenuType.DEAD)) {
            upButton.setTexture(buttonsTextures.get(ButtonType.AGAIN));
            downButton.setTexture(buttonsTextures.get(ButtonType.EXIT));
            title.setTexture(titleTextures.get(1));//todo defines
            elements.add(title);
        }
        elements.add(upButton);
        elements.add(downButton);
    }

    public void clear() {
        elements.clear();
    }

    public void cleanup() {
        numberTextures.forEach((i, t) -> t.cleanup());
        buttonsTextures.forEach((i, t) -> t.cleanup());
        buttonMesh.cleanup();
        counterMesh.cleanup();
    }
}

//        HUD/ExitButton.png //todo remove comments
//        HUD/GameOverBackground.png
//        HUD/GameOverTitle.png
//        HUD/GameTitle.png
//        HUD/MainMenuButton.png
//        HUD/NewGameButton.png
//        HUD/TryAgainButton.png
// 0.66, 2.88, -1 = -0.05;
