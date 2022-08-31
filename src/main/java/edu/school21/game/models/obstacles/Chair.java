package edu.school21.game.models.obstacles;

import edu.school21.game.models.GameObject;
import edu.school21.utils.RandomGenerator;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.types.MeshType.CHAIR;
import static edu.school21.game.utils.TextureContainer.getRandomClusterTexture;

public class Chair extends GameObject {
    public Chair() {
        super(meshes.get(CHAIR), getRandomClusterTexture());
        type = CHAIR;
    }

    public void randomPositionRotation() {
        position.x = RandomGenerator.generateOne(-0.5f, 0.5f);
        rotation.y = RandomGenerator.generate(90f, 270f);
    }
}
