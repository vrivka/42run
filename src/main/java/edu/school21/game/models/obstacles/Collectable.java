package edu.school21.game.models.obstacles;

import edu.school21.game.models.GameObject;
import edu.school21.utils.RandomGenerator;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.types.MeshType.COLLECTABLE;

public class Collectable extends GameObject {
    public Collectable() {
        super(meshes.get(COLLECTABLE));
        mesh.getColor().set(1f, 0.84f, 0f);
        type = COLLECTABLE;
    }

    public void randomPosition() {
        position.x = RandomGenerator.generate(-0.7f, 0.7f);
    }
}
