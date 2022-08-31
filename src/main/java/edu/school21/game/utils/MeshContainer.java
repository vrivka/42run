package edu.school21.game.utils;

import edu.school21.app.Main;
import edu.school21.engine.render.Mesh;
import edu.school21.game.utils.types.AnimationType;
import edu.school21.game.utils.types.MeshType;
import edu.school21.utils.OBJLoader;

import java.util.HashMap;
import java.util.Map;

public class MeshContainer {
    public static Map<MeshType, Mesh> meshes = new HashMap<>();
    public static Map<AnimationType, Mesh[]> animations = new HashMap<>();

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
            meshes.put(MeshType.FENCE, OBJLoader.loadMesh("models/fence.obj"));
            meshes.put(MeshType.COLLECTABLE, OBJLoader.loadMesh("models/collectable.obj"));

            Mesh[] anim = new Mesh[16];

            for (int i = 1; i < 17; i++) {
                anim[i - 1] = OBJLoader.loadMesh(String.format("models/player/run/player_run_%06d.obj", i));
            }
            animations.put(AnimationType.RUN, anim);
            anim = new Mesh[17];

            for (int i = 1; i < 18; i++) {
                anim[i - 1] = OBJLoader.loadMesh(String.format("models/player/roll/player_roll_%06d.obj", i));
                anim[i - 1].getMax().y = 0.5f;
            }
            animations.put(AnimationType.ROLL, anim);
            anim = new Mesh[10];

            for (int i = 1; i < 11; i++) {
                anim[i - 1] = OBJLoader.loadMesh(String.format("models/player/jump/player_jump_%06d.obj", i));
            }
            animations.put(AnimationType.JUMP, anim);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(Main.ERROR_EXIT_CODE);
        }
    }

    public static void cleanup() {
        meshes.forEach((t, m) -> m.cleanup());
    }
}
