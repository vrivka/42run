package edu.school21.game.models.obstacles;

import edu.school21.game.models.GameObject;
import edu.school21.game.utils.types.MeshType;
import edu.school21.game.utils.types.TextureType;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.TextureContainer.textures;

public class Board extends GameObject {
    public Board() {
        super(meshes.get(MeshType.BOARD), textures.get(TextureType.BOARD));
        mesh.getMin().y = 0.6f;
    }
}
