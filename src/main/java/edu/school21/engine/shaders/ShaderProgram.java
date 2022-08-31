package edu.school21.engine.shaders;

import edu.school21.engine.shaders.exceptions.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {
    private static final int ERROR_IDENTIFIER = 0;
    private static final int MATRIX_SIZE = 16;
    private static final int INFO_LOG_MAX_LENGTH = 1024;
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms = new HashMap<>();

    public ShaderProgram() {
        this.programId = glCreateProgram();

        if (programId == ERROR_IDENTIFIER) {
            throw new ShaderProgramCreationFailException("Could not create Shader Program");
        }
    }

    public void createUniform(String uniformName) {
        if (uniforms.containsKey(uniformName)) {
            return;
        }
        int uniformLocation = glGetUniformLocation(programId, uniformName);

        if (uniformLocation < ERROR_IDENTIFIER) {
            throw new UniformLocationErrorException("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer uniformBuffer = stack.mallocFloat(MATRIX_SIZE);

            matrix.get(uniformBuffer);
            glUniformMatrix4fv(uniforms.get(uniformName), false, uniformBuffer);
        }
    }

    public void setUniform(String uniformName, Vector3f vector) {
        glUniform3f(uniforms.get(uniformName), vector.x, vector.y, vector.z);
    }

    public void setUniform(String uniformName, int integer) {
        glUniform1i(uniforms.get(uniformName), integer);
    }

    public void createVertexShader(String shaderSource) {
        vertexShaderId = createShader(shaderSource, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderSource) {
        fragmentShaderId = createShader(shaderSource, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderSource, int shaderType) {
        int shaderId = glCreateShader(shaderType);

        if (shaderId == ERROR_IDENTIFIER) {
            throw new ShaderCreationFailException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == ERROR_IDENTIFIER) {
            throw new ShaderCompileErrorException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, INFO_LOG_MAX_LENGTH));
        }

        glAttachShader(programId, shaderId);
        return shaderId;
    }

    public void link() {
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == ERROR_IDENTIFIER) {
            throw new ShaderProgramLinkErrorException("Error linking Shader code: " + glGetProgramInfoLog(programId, INFO_LOG_MAX_LENGTH));
        }

        if (vertexShaderId != ERROR_IDENTIFIER) {
            glDetachShader(programId, vertexShaderId);
        }

        if (fragmentShaderId != ERROR_IDENTIFIER) {
            glDetachShader(programId, fragmentShaderId);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();

        if (programId != ERROR_IDENTIFIER) {
            glDeleteProgram(programId);
        }
    }
}
