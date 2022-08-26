package edu.school21.game.obstacles;

import edu.school21.engine.render.Mesh;
import edu.school21.game.GameObject;
import edu.school21.game.ObstacleIDs;
import edu.school21.utils.RandomGenerator;

public class Sign extends GameObject {
    private final ObstacleIDs type = ObstacleIDs.SIGN;

    public Sign(Mesh mesh) {
        super(mesh);
    }

    public ObstacleIDs getType() {
        return type;
    }

    public void randomPositionRotation() {
        position.x = RandomGenerator.generate(-0.7f, 0.7f);
        rotation.y = RandomGenerator.generate(-20f, 45f);
    }
}
