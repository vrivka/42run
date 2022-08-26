package edu.school21.game.obstacles;

import edu.school21.engine.render.Mesh;
import edu.school21.game.GameObject;
import edu.school21.game.ObstacleIDs;
import edu.school21.utils.RandomGenerator;

public class Chair extends GameObject {
    private final ObstacleIDs type = ObstacleIDs.CHAIR;

    public Chair(Mesh mesh) {
        super(mesh);
    }

    public ObstacleIDs getType() {
        return type;
    }

    public void randomPositionRotation() {
        position.x = RandomGenerator.generateOne(-0.5f, 0.5f);
        rotation.y = RandomGenerator.generate(90f, 270f);
    }
}
