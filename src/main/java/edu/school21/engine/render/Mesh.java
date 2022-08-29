package edu.school21.engine.render;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private final int vertexArrayObjectId;
    private final int vertexBufferObjectId;
    private final int indicesBufferObjectId;
    private final int texturesCordsBufferObjectId;
    private final int normalsBufferObjectId;
    private final int vertexCount;
    private final Vector3f color = new Vector3f(1f);
    private final Vector3f min;
    private final Vector3f max;

    public Mesh(float[] vertices, int[] indices, float[] textureCords, float[] normals) {
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer texturesCordsBuffer = null;
        FloatBuffer normalsBuffer = null;
        vertexCount = indices.length;
        min = new Vector3f();
        max = new Vector3f();
        setMinMax(vertices);

        try {
            vertexArrayObjectId = glGenVertexArrays();

            glBindVertexArray(vertexArrayObjectId);

            vertexBufferObjectId = glGenBuffers();
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            texturesCordsBufferObjectId = glGenBuffers();
            texturesCordsBuffer = MemoryUtil.memAllocFloat(textureCords.length);
            texturesCordsBuffer.put(textureCords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, texturesCordsBufferObjectId);
            glBufferData(GL_ARRAY_BUFFER, texturesCordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            normalsBufferObjectId = glGenBuffers();
            normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            normalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, normalsBufferObjectId);
            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            indicesBufferObjectId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBufferObjectId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
            if (textureCords != null) {
                MemoryUtil.memFree(texturesCordsBuffer);
            }
            if (normalsBuffer != null) {
                MemoryUtil.memFree(normalsBuffer);
            }
        }
    }

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color.set(color);
    }

    private void setMinMax(float[] vertices) {
        for (int i = 0; i < vertices.length; i += 3) {
            min.x = Math.min(min.x, vertices[i]);
            min.y = Math.min(min.y, vertices[i + 1]);
            min.z = Math.min(min.z, vertices[i + 2]);

            max.x = Math.max(max.x, vertices[i]);
            max.y = Math.max(max.y, vertices[i + 1]);
            max.z = Math.max(max.z, vertices[i + 2]);
        }
    }

    public void render() {
        glBindVertexArray(vertexArrayObjectId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertexBufferObjectId);
        glDeleteBuffers(indicesBufferObjectId);
        glDeleteBuffers(texturesCordsBufferObjectId);
        glDeleteBuffers(normalsBufferObjectId);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArrayObjectId);
    }
}
