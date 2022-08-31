package edu.school21.engine.render;

import edu.school21.engine.render.exceptions.LoadTextureFailException;
import edu.school21.utils.Utils;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture2D {
    private final int id;

    public Texture2D(String fileName) throws Exception {
        this.id = loadTexture(fileName);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    private int loadTexture(String fileName) throws Exception {
        ByteBuffer buf;
        int width;
        int height;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load_from_memory(Utils.getBytesFromFile(fileName), widthBuffer, heightBuffer, channels, 4);

            if (buf == null) {
                throw new LoadTextureFailException("Image file [" + fileName + "] not loaded: " + stbi_failure_reason());
            }

            width = widthBuffer.get();
            height = heightBuffer.get();
        }

        int textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(buf);
        return textureId;
    }

    public void cleanup() {
        if (id != 0) {
            glDeleteTextures(id);
        }
    }
}
