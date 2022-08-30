package edu.school21.game.models;

import edu.school21.engine.render.Mesh;
import org.joml.Vector3f;

public class CollisionModel {
    private Mesh mesh;
    private final Vector3f position;

    public CollisionModel(Mesh mesh, Vector3f position) {
        this.position = position;
        this.mesh = mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public boolean calc(CollisionModel model) {
        float aMinX = mesh.getMin().x + position.x;
        float aMinY = mesh.getMin().y + position.y;
        float aMinZ = mesh.getMin().z + position.z;
        float aMaxX = mesh.getMax().x + position.x;
        float aMaxY = mesh.getMax().y + position.y;
        float aMaxZ = mesh.getMax().z + position.z;

        float bMinX = model.mesh.getMin().x + model.position.x;
        float bMinY = model.mesh.getMin().y + model.position.y;
        float bMinZ = model.mesh.getMin().z + model.position.z;
        float bMaxX = model.mesh.getMax().x + model.position.x;
        float bMaxY = model.mesh.getMax().y + model.position.y;
        float bMaxZ = model.mesh.getMax().z + model.position.z;

        return (aMinX <= bMaxX && aMaxX >= bMinX) &&
                (aMinY <= bMaxY && aMaxY >= bMinY) &&
                (aMinZ <= bMaxZ && aMaxZ >= bMinZ);
    }
}
