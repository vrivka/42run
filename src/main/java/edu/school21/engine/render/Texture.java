package edu.school21.engine.render;

import edu.school21.engine.render.exceptions.LoadTextureFailException;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int id;

    public Texture(String fileName) {
        this(loadTexture(fileName));
    }

    private Texture(int id) {
        this.id = id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    private static int loadTexture(String fileName) {
        int width;
        int height;
        ByteBuffer buf;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(fileName, widthBuffer, heightBuffer, channels, 4);
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
        glDeleteTextures(id);
    }

}
