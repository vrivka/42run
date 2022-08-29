package edu.school21.game.hud;

import edu.school21.engine.render.Camera;
import edu.school21.game.models.GameObject;

import java.util.ArrayList;
import java.util.List;

public class HUDHandler {
    private final CounterHandler hudCounter;
    private final MenusHandler hudMenus;

    public HUDHandler(Camera camera) {
        this.hudCounter = new CounterHandler(camera.getRotation());
        this.hudMenus = new MenusHandler(camera.getRotation());
    }

    public List<GameObject> collect() {
        List<GameObject> result = new ArrayList<>();

        result.addAll(hudMenus.collect());
        result.addAll(hudCounter.collect());
        return result;
    }

    public void update(MenuType menu, int count, float aspect) {
        clear();
        hudCounter.update(count, aspect);
        hudMenus.update(menu);
    }

    private void clear() {
        hudCounter.clear();
        hudMenus.clear();
    }
}
