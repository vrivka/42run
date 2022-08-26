package edu.school21.utils;

import edu.school21.engine.render.Mesh;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class OBJLoader {
    public static final int ASSIMP_FLAGS = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals;

    public static Mesh loadMesh(String fileName) throws Exception {
        AIScene aiScene = aiImportFile(Utils.getPathToResource(fileName), ASSIMP_FLAGS);

        if (aiScene == null) {
            throw new Exception(aiGetErrorString());
        }
        PointerBuffer aiMeshes = aiScene.mMeshes();

        AIMesh aiMesh = AIMesh.create(aiMeshes.get(0));
        return processMesh(aiMesh);
    }

    private static Mesh processMesh(AIMesh aiMesh) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

        return new Mesh(Utils.listFloatToArray(vertices),
                Utils.listIntToArray(indices),
                Utils.listFloatToArray(textures),
                Utils.listFloatToArray(normals)
        );
    }

    private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();

        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();

            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
    }

    private static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
        AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);

        if (buffer == null) {
            return ;
        }

        while (buffer.remaining() > 0) {
            AIVector3D textCord = buffer.get();
            textures.add(textCord.x());
            textures.add(1 - textCord.y());
        }
    }

    private static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();

        if (aiNormals == null) {
            return ;
        }

        while (aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }

    private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();

        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }
}
