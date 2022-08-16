package edu.school21.engine.shaders;

import edu.school21.engine.shaders.exceptions.*;
import edu.school21.engine.shaders.fragment.struct.Attenuation;
import edu.school21.engine.shaders.fragment.struct.DirectionalLight;
import edu.school21.engine.shaders.fragment.struct.Material;
import edu.school21.engine.shaders.fragment.struct.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20C.*;

public class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() {
        uniforms = new HashMap<>();
        programId = glCreateProgram();

        if (programId == 0) {
            throw new ShaderProgramCreationFailException("Could not create Shader Program");
        }
    }

    public void createVertexShader(String shaderSource) {
        vertexShaderId = createShader(shaderSource, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderSource) {
        fragmentShaderId = createShader(shaderSource, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderSource, int shaderType) {
        int shaderId = glCreateShader(shaderType);

        if (shaderId == 0) {
            throw new ShaderCreationFailException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new ShaderCompileErrorException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);
        return shaderId;
    }

    public void createUniform(String uniformName) {
        if (uniforms.containsKey(uniformName)) {
            return;
        }
        int uniformLocation = glGetUniformLocation(programId, uniformName);

        if (uniformLocation < 0) {
            throw new UniformLocationErrorException("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void createPointLightUniform(String uniformName) {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".attenuation.constant");
        createUniform(uniformName + ".attenuation.linear");
        createUniform(uniformName + ".attenuation.exponent");
    }

    public void createMaterialUniform(String uniformName) {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void createDirectionalLightUniform(String uniformName) {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void setUniform(String uniformName, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer uniformBuffer = stack.mallocFloat(16);

            matrix.get(uniformBuffer);
            glUniformMatrix4fv(uniforms.get(uniformName), false, uniformBuffer);
        }
    }

    public void setUniform(String uniformName, Vector3f vector) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer uniformBuffer = stack.mallocFloat(3);

            vector.get(uniformBuffer);
            glUniform3fv(uniforms.get(uniformName), uniformBuffer);
        }
    }

    public void setUniform(String uniformName, Vector4f vector) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer uniformBuffer = stack.mallocFloat(4);

            vector.get(uniformBuffer);
            glUniform4fv(uniforms.get(uniformName), uniformBuffer);
        }
    }

    public void setUniform(String uniformName, int integer) {
        glUniform1i(uniforms.get(uniformName), integer);
    }

    public void setUniform(String uniformName, float floatVal) {
        glUniform1f(uniforms.get(uniformName), floatVal);
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        Attenuation attenuation = pointLight.getAttenuation();

        setUniform(uniformName + ".color", pointLight.getColor() );
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        setUniform(uniformName + ".attenuation.constant", attenuation.getConstant());
        setUniform(uniformName + ".attenuation.linear", attenuation.getLinear());
        setUniform(uniformName + ".attenuation.exponent", attenuation.getExponent());
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbient());
        setUniform(uniformName + ".diffuse", material.getDiffuse());
        setUniform(uniformName + ".specular", material.getSpecular());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void setUniform(String uniformName, DirectionalLight directionalLight) {
        setUniform(uniformName + ".color", directionalLight.getColor());
        setUniform(uniformName + ".direction", directionalLight.getDirection());
        setUniform(uniformName + ".intensity", directionalLight.getIntensity());

    }

    public void link() {
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new ShaderProgramLinkErrorException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }

        if (fragmentShaderId != 0) {
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

        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
