package edu.school21.game.hud;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static edu.school21.game.utils.types.ButtonType.*;
import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.types.MeshType.BUTTON;
import static edu.school21.game.utils.types.MeshType.TITLE;
import static edu.school21.game.utils.TextureContainer.buttonTextures;
import static edu.school21.game.utils.TextureContainer.titleTextures;
import static edu.school21.game.utils.types.TitleType.GAME_OVER;
import static edu.school21.game.utils.types.TitleType.MAIN_MENU;

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

    public void update(MenuType menu) {
        if (menu.equals(MenuType.IN_GAME)) {
            return;
        }
        HudElement upButton = new HudElement(meshes.get(BUTTON), null, cameraRotation);
        upButton.setScale(0.5f);
        upButton.setPosition(0.25f, 2.5f, -1f);
        HudElement downButton = new HudElement(meshes.get(BUTTON), null, cameraRotation);
        downButton.setScale(0.5555f);
        downButton.setPosition(0.275f, 2.2f, -1f);
        HudElement title = new HudElement(meshes.get(TITLE), null, cameraRotation);
        title.setScale(0.7f);
        title.setPosition(0.7f, 2.88f, -1f);


        if (menu.equals(MenuType.MAIN)) {
            upButton.setTexture(buttonTextures.get(NEW_GAME));
            downButton.setTexture(buttonTextures.get(EXIT));
            title.setTexture(titleTextures.get(MAIN_MENU));//todo defines
            elements.add(title);
        } else if (menu.equals(MenuType.PAUSE)) {
            upButton.setTexture(buttonTextures.get(CONTINUE));
            downButton.setTexture(buttonTextures.get(EXIT));
        } else if (menu.equals(MenuType.DEAD)) {
            upButton.setTexture(buttonTextures.get(TRY_AGAIN));
            downButton.setTexture(buttonTextures.get(EXIT));
            title.setTexture(titleTextures.get(GAME_OVER));//todo defines
            elements.add(title);
        }
        elements.add(upButton);
        elements.add(downButton);
    }

    public void clear() {
        elements.clear();
    }
}
