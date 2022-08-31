package edu.school21.game.models.obstacles;

import edu.school21.game.models.GameObject;
import edu.school21.game.utils.types.TextureType;

import static edu.school21.game.utils.MeshContainer.meshes;
import static edu.school21.game.utils.TextureContainer.textures;
import static edu.school21.game.utils.types.MeshType.BOARD;

public class Board extends GameObject {
    public Board() {
        super(meshes.get(BOARD), textures.get(TextureType.BOARD));
        type = BOARD;
        mesh.getMin().y = 0.6f;
    }
}
