package edu.school21.game.models.obstacles;

import edu.school21.game.models.GameObject;
import edu.school21.game.utils.types.TextureType;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.TextureContainer.textures;
import static edu.school21.game.utils.types.MeshType.FENCE;

public class Fence extends GameObject {
    public Fence() {
        super(meshes.get(FENCE), textures.get(TextureType.FENCE));
        type = FENCE;
    }
}
