package edu.school21.game.models.obstacles;

import edu.school21.game.models.GameObject;
import edu.school21.game.utils.TextureContainer;
import edu.school21.game.utils.types.TextureType;
import edu.school21.utils.RandomGenerator;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.types.MeshType.SIGN;

public class Sign extends GameObject {
    public Sign() {
        super(meshes.get(SIGN), TextureContainer.textures.get(TextureType.SIGN));
    }

    public void randomPositionRotation() {
        position.x = RandomGenerator.generate(-0.7f, 0.7f);
        rotation.y = RandomGenerator.generate(-20f, 45f);
    }
}