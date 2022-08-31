package edu.school21.game.hud;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.TextureContainer.numberTextures;
import static edu.school21.game.utils.types.MeshType.NUMBER;

public class CounterHandler {
    private static final Vector3f COUNTER_POSITION = new Vector3f(0, 2.88f, -1f);
    private final List<HudElement> elements;
    private final Vector3f cameraRotation;

    public CounterHandler(Vector3f cameraRotation) {
        this.cameraRotation = cameraRotation;
        this.elements = new ArrayList<>();
    }

    public List<HudElement> collect() {
        return elements;
    }

    public void update(int count, float aspect) {
        for (int i = 0; count != 0; count /= numberTextures.size(), i++) {
            HudElement hudElement = new HudElement(meshes.get(NUMBER), numberTextures.get(count % numberTextures.size()), cameraRotation);

            hudElement.setPosition(COUNTER_POSITION);
            hudElement.moveX((aspect / 2) - (i * 0.05f));
            elements.add(hudElement);
        }
    }

    public void clear() {
        elements.clear();
    }
}
