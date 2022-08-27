package edu.school21.engine.render;

import edu.school21.game.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    private final Matrix4f perspective;
    private final Matrix4f ortho2D;
    private final Matrix4f modelView;
    private final Matrix4f view;

    public Transformation() {
        this.perspective = new Matrix4f();
        this.ortho2D = new Matrix4f();
        this.modelView = new Matrix4f();
        this.view = new Matrix4f();
    }

    public Matrix4f getPerspective(float fov, float aspect, float zNear, float zFar) {
        perspective.identity().perspective(fov, aspect, zNear, zFar);
        return perspective;
    }

    public Matrix4f getOrtho2D(float left, float right, float bottom, float top) {
        ortho2D.identity().ortho2D(left, right, bottom, top);
        return ortho2D;
    }

    public Matrix4f getModelView(GameObject gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getRotation();

        modelView.identity().translate(gameObject.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(gameObject.getScale());

        Matrix4f viewCurr = new Matrix4f(viewMatrix);

        return viewCurr.mul(modelView);
    }

    public Matrix4f getModelMatrix(GameObject gameObject) {
        Vector3f rotation = gameObject.getRotation();

        modelView.identity().translate(gameObject.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(gameObject.getScale());
        return modelView;
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        view.identity()
                .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return view;
    }
}
