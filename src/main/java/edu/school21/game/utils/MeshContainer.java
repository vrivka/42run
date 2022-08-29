package edu.school21.game.utils;

import edu.school21.app.Main;
import edu.school21.engine.render.Mesh;
import edu.school21.game.utils.types.MeshType;
import edu.school21.utils.OBJLoader;

import java.util.HashMap;
import java.util.Map;

public class MeshContainer {
    public static Map<MeshType, Mesh> meshes = new HashMap<>();

    static {
        try {
            meshes.put(MeshType.PLAYER, OBJLoader.loadMesh("models/player_cube.obj"));
            meshes.put(MeshType.SIGN, OBJLoader.loadMesh("models/sign.obj"));
            meshes.put(MeshType.CHAIR, OBJLoader.loadMesh("models/chair.obj"));
            meshes.put(MeshType.BOARD, OBJLoader.loadMesh("models/board.obj"));
            meshes.put(MeshType.CLUSTER, OBJLoader.loadMesh("models/cluster.obj"));
            meshes.put(MeshType.FLOOR, OBJLoader.loadMesh("models/floor.obj"));
            meshes.put(MeshType.NUMBER, OBJLoader.loadMesh("models/number.obj"));
            meshes.put(MeshType.BUTTON, OBJLoader.loadMesh("models/button.obj"));
            meshes.put(MeshType.TITLE, OBJLoader.loadMesh("models/title.obj"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(Main.ERROR_EXIT_CODE);
        }
    }

    public static void cleanup() {
        meshes.forEach((t, m) -> m.cleanup());
    }
}
