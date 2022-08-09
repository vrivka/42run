package edu.school21.engine.render;

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
    private final int textureBufferObjectId;
    private final int vertexCount;
    private final Texture texture;

    public Mesh(float[] vertices, int[] indices, float[] textureCords, Texture texture) {
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer texturesCordsBuffer = null;
        vertexCount = indices.length;
        this.texture = texture;

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


            textureBufferObjectId = glGenBuffers();
            texturesCordsBuffer = MemoryUtil.memAllocFloat(textureCords.length);
            texturesCordsBuffer.put(textureCords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, textureBufferObjectId);
            glBufferData(GL_ARRAY_BUFFER, texturesCordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

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
        }
    }

    public void render() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        glBindVertexArray(vertexArrayObjectId);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertexBufferObjectId);
        glDeleteBuffers(indicesBufferObjectId);
        glDeleteBuffers(textureBufferObjectId);

        texture.cleanup();

        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArrayObjectId);
    }
}
