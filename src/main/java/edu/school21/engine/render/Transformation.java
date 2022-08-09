package edu.school21.engine.render;

import edu.school21.game.GameItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    private final Matrix4f projection;
    private final Matrix4f modelView;
    private final Matrix4f view;

    public Transformation() {
        this.projection = new Matrix4f();
        this.modelView = new Matrix4f();
        this.view = new Matrix4f();
    }

    public Matrix4f getProjection(float fov, float width, float height, float zNear, float zFar) {
        float aspect = width / height;
        projection.identity().perspective(fov, aspect, zNear, zFar);
        return projection;
    }

    public Matrix4f getModelView(GameItem gameItem, Matrix4f viewMatrix) {
        Vector3f rotation = gameItem.getRotation();
        modelView.identity().translate(gameItem.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameItem.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelView);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        view.identity()
                .rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return view;
    }


}
