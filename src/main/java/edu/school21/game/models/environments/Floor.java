package edu.school21.game.models.environments;

import edu.school21.game.models.GameObject;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.types.MeshType.FLOOR;

public class Floor extends GameObject {
    public Floor() {
        super(meshes.get(FLOOR));
        scale = 1000f;
    }
}
