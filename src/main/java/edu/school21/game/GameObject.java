package edu.school21.game;

import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Texture;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class GameObject {
    protected Mesh mesh;
    protected Texture texture = null;
    protected Vector3f position;
    protected Vector3f rotation;
    protected float scale;
    protected CollisionModel collisionModel;

    protected GameObject() {
        this.position = new Vector3f(0, 0, 0);
        this.scale = 1f;
        this.rotation = new Vector3f(0, 0, 0);
    }

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        this.position = new Vector3f(0, 0, 0);
        this.collisionModel = new CollisionModel(mesh, this.position);
        this.scale = 1f;
        this.rotation = new Vector3f(0, 0, 0);
    }

    public GameObject(Mesh mesh, Texture texture) {
        this.mesh = mesh;
        this.position = new Vector3f(0, 0, 0);
        this.collisionModel = new CollisionModel(mesh, this.position);
        this.scale = 1f;
        this.rotation = new Vector3f(0, 0, 0);
        this.texture = texture;
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

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public boolean intersect(GameObject gameObject) {
        return collisionModel.calc(gameObject.collisionModel);
    }

    public void move(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void render() {
        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }
        mesh.render();
    }

    public void cleanup() {
        if (texture != null) {
            texture.cleanup();
        }
        mesh.cleanUp();
    }
}
