package edu.school21.game.obstacles;

import edu.school21.engine.render.Mesh;
import edu.school21.game.GameObject;
import edu.school21.game.ObstacleIDs;

public class Board extends GameObject {
    private final ObstacleIDs type = ObstacleIDs.SIGN;

    public Board(Mesh mesh) {
        super(mesh);
        mesh.min.y = 0.6f;
    }

    public ObstacleIDs getType() {
        return type;
    }
}
