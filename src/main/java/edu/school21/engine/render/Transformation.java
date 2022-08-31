package edu.school21.engine.render;

import edu.school21.game.models.GameObject;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    private static final Vector3f X_ROTATION_AXIS = new Vector3f(1, 0, 0);
    private static final Vector3f Y_ROTATION_AXIS = new Vector3f(0, 1, 0);
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f modelViewMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public Matrix4f getPerspectiveProjection(float fov, float aspect, float zNear, float zFar) {
        return projectionMatrix.identity().perspective(fov, aspect, zNear, zFar);
    }

    public Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getRotation();
        Matrix4f viewCurr = new Matrix4f(viewMatrix);

        modelViewMatrix.identity().translate(gameObject.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(gameObject.getScale());
        return viewCurr.mul(modelViewMatrix);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        return viewMatrix.identity()
                .rotate((float) Math.toRadians(rotation.x), X_ROTATION_AXIS)
                .rotate((float) Math.toRadians(rotation.y), Y_ROTATION_AXIS)
                .translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    }
}
