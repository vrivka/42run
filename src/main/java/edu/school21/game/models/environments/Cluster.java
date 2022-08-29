package edu.school21.game.models.environments;

import edu.school21.game.models.GameObject;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.TextureContainer.getRandomClusterTexture;
import static edu.school21.game.utils.types.MeshType.CLUSTER;

public class Cluster extends GameObject {
    public Cluster() {
        super(meshes.get(CLUSTER), getRandomClusterTexture());
    }
}
