package edu.school21.utils;

import edu.school21.engine.render.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static Mesh loadMesh(String fileName) throws IOException {
        List<String> lines = Utils.readAllLines(fileName);
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textureCords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v" -> {
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                }
                case "vt" -> {
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textureCords.add(vec2f);
                }
                case "vn" -> {
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vec3fNorm);
                }
                case "f" -> {
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                }
                default -> {}
            }
        }
        return reorderLists(vertices, textureCords, normals, faces);
    }

    protected static class IdxGroup {

        public static final int NO_VALUE = -1;

        public int idxPos;

        public int idxTextCord;

        public int idxVecNormal;

        public IdxGroup() {
            idxPos = NO_VALUE;
            idxTextCord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }

    private static class Face {

        IdxGroup[] idxGroups;

        public Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];

            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        public IdxGroup parseLine(String group) {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = group.split("/");
            int length = lineTokens.length;

            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;

            if (length > 1) {
                idxGroup.idxTextCord = lineTokens[1].isEmpty() ? IdxGroup.NO_VALUE : Integer.parseInt(lineTokens[1]) - 1;

                if (length > 2) {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return idxGroup;
        }

        public IdxGroup[] getFaceVertexIndices() {
            return idxGroups;
        }
    }

    private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> textCordList, List<Vector3f> normList, List<Face> facesList) {
        List<Integer> indices = new ArrayList<>();
        float[] posArr = new float[posList.size() * 3];
        int i = 0;

        for (Vector3f pos : posList) {
            posArr[i * 3] = pos.x;
            posArr[i * 3 + 1] = pos.y;
            posArr[i * 3 + 2] = pos.z;
            i++;
        }

        float[] textCordArr = new float[posList.size() * 2];
        float[] normArr = new float[posList.size() * 3];

        for (Face face : facesList) {
            IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();

            for (IdxGroup indValue : faceVertexIndices) {
                processFaceVertex(indValue, textCordList, normList,
                        indices, textCordArr, normArr);
            }
        }
        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

        return new Mesh(posArr, indicesArr, textCordArr, normArr, null);
    }

    private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCordList, List<Vector3f> normList, List<Integer> indicesList, float[] texCordArr, float[] normArr) {
        int posIndex = indices.idxPos;

        indicesList.add(posIndex);

        if (indices.idxTextCord >= 0) {
            Vector2f textCord = textCordList.get(indices.idxTextCord);

            texCordArr[posIndex * 2] = textCord.x;
            texCordArr[posIndex * 2 + 1] = 1 - textCord.y;
        }
        if (indices.idxVecNormal >= 0) {
            Vector3f vecNorm = normList.get(indices.idxVecNormal);

            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }
}
