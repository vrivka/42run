package edu.school21.game.hud;

import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Texture2D;
import edu.school21.game.models.GameObject;
import org.joml.Vector3f;

public class HudElement extends GameObject {
    public HudElement(Mesh mesh, Texture2D texture, Vector3f cameraRotation) {
        super(mesh, texture);
        this.scale = 0.05f;
        this.rotation = cameraRotation;
    }
}
