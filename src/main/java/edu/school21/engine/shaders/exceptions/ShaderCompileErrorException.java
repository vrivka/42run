package edu.school21.engine.shaders.exceptions;

public class ShaderCompileErrorException extends RuntimeException {
    public ShaderCompileErrorException(String message) {
        super(message);
    }
}
