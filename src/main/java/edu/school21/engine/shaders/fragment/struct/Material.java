package edu.school21.engine.shaders.fragment.struct;

import edu.school21.engine.render.Texture;
import org.joml.Vector4f;

public class Material {
    private static final Vector4f DEFAULT_COLOR = new Vector4f(1, 1, 1, 1);
    private Vector4f ambient;
    private Vector4f diffuse;
    private Vector4f specular;
    private Texture texture;
    private float reflectance;

    public Material() {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, null, 0);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, 0);
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color, color, null, reflectance);
    }

    public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, Texture texture, float reflectance) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Vector4f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector4f ambient) {
        this.ambient = ambient;
    }

    public Vector4f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector4f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector4f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector4f specular) {
        this.specular = specular;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isTextured() {
        return texture != null;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }
}
