package edu.school21.game.models;

import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Texture2D;
import edu.school21.game.utils.types.MeshType;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class GameObject {
    protected MeshType type;
    protected Mesh mesh = null;
    protected Texture2D texture = null;
    protected Vector3f position = new Vector3f();
    protected Vector3f rotation = new Vector3f();
    protected float scale = 1f;
    protected CollisionModel collisionModel = null;

    public GameObject() {}

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        this.collisionModel = new CollisionModel(this.mesh, this.position);
    }

    public GameObject(Mesh mesh, Texture2D texture) {
        this(mesh);
        this.texture = texture;
        this.collisionModel = new CollisionModel(this.mesh, this.position);
    }

    public boolean isTypeOf(MeshType type) {
        return this.type.equals(type);
    }

    public int isTextured() {
        return texture != null ? 0 : 1;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setTexture(Texture2D texture) {
        this.texture = texture;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
    }

    public boolean intersect(GameObject gameObject) {
        return collisionModel.calc(gameObject.collisionModel);
    }

    public void moveX(float x) {
        position.x += x;
    }

    public void moveY(float y) {
        position.y += y;
    }

    public void moveZ(float z) {
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
        mesh.cleanup();
    }
}
