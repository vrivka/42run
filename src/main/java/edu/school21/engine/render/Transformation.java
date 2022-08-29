package edu.school21.engine.render;

import edu.school21.game.models.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    private static final Vector3f X_ROTATION_AXIS = new Vector3f(1, 0, 0);
    private static final Vector3f Y_ROTATION_AXIS = new Vector3f(0, 1, 0);
    private final Matrix4f perspective = new Matrix4f();
    private final Matrix4f modelView = new Matrix4f();
    private final Matrix4f view = new Matrix4f();

    public Matrix4f getPerspective(float fov, float aspect, float zNear, float zFar) {
        perspective.identity().perspective(fov, aspect, zNear, zFar);
        return perspective;
    }

    public Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getRotation();
        Matrix4f viewCurr = new Matrix4f(viewMatrix);

        modelView.identity().translate(gameObject.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(gameObject.getScale());
        return viewCurr.mul(modelView);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        view.identity()
                .rotate((float) Math.toRadians(rotation.x), X_ROTATION_AXIS)
                .rotate((float) Math.toRadians(rotation.y), Y_ROTATION_AXIS)
                .translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return view;
    }
}
