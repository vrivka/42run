package edu.school21.game.hud;

import edu.school21.engine.render.Camera;
import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Texture;
import edu.school21.game.GameObject;
import edu.school21.utils.OBJLoader;

import java.util.ArrayList;
import java.util.List;

public class HUDHandler {
    private List<HudElement> numbers;
    private List<HudElement> buttons;
    private final Camera camera;

    public HUDHandler(Camera camera) {
        this.camera = camera;
        this.numbers = new ArrayList<>();
        this.buttons = new ArrayList<>();
    }

    public List<GameObject> collect() {
        List<GameObject> result = new ArrayList<>();

        result.addAll(numbers);
        result.addAll(buttons);
        return result;
    }

    public void init() throws Exception {
        Mesh mesh = OBJLoader.loadMesh("number.obj");

        for (int i = 0; i < 10; i++) {
            Texture texture = new Texture("HUD/" + i + ".png");

            numbers.add(new HudElement(camera, mesh, texture));
            numbers.get(i).setPosition(0.66f - i * 0.05f, 2.88f, -1f);
        }
//        HUD/ExitButton.png
//        HUD/GameOverBackground.png
//        HUD/GameOverTitle.png
//        HUD/GameTitle.png
//        HUD/MainMenuButton.png
//        HUD/NewGameButton.png
//        HUD/TryAgainButton.png
        mesh = OBJLoader.loadMesh("button.obj");
        buttons.add(new HudElement(camera, mesh, new Texture("HUD/ExitButton.png")));
        buttons.get(0).setPosition(0,2.88f, -1f);
        buttons.get(0).setScale(0.3f);
        buttons.add(new HudElement(camera, mesh, new Texture("HUD/MainMenuButton.png")));
        buttons.get(1).setPosition(0,2.88f, -1f);
        buttons.get(1).setScale(0.3f);
        buttons.add(new HudElement(camera, mesh, new Texture("HUD/NewGameButton.png")));
        buttons.get(2).setPosition(0,2.88f, -1f);
        buttons.get(2).setScale(0.3f);
        buttons.add(new HudElement(camera, mesh, new Texture("HUD/TryAgainButton.png")));
        buttons.get(3).setPosition(0,2.88f, -1f);
        buttons.get(3).setScale(0.3f);
    }
}
