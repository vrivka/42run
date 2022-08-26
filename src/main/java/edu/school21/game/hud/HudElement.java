package edu.school21.game.hud;

import edu.school21.engine.render.Camera;
import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Texture;
import edu.school21.game.GameObject;

public class HudElement extends GameObject {
    public HudElement(Camera camera, Mesh mesh, Texture texture) {
        super(mesh, texture);
        scale = 0.05f;
        rotation = camera.getRotation();
    }
}
