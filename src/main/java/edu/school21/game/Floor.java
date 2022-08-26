package edu.school21.game;

import edu.school21.utils.OBJLoader;
import org.joml.Vector3f;

public class Floor extends GameObject {
    public Floor() throws Exception {
        super(OBJLoader.loadMesh("floor.obj"));
        scale = 1000f;
        mesh.setColor(new Vector3f(1f));
    }
}
