package edu.school21.game;

import edu.school21.engine.render.Mesh;
import edu.school21.engine.render.Texture;
import edu.school21.utils.OBJLoader;
import org.joml.Vector3f;

import java.util.ArrayDeque;
import java.util.Deque;

public class EnvironmentGenerator {
    private static final float SPAWN_OFFSET = -16.585f;
    private static final float OFFSET = -SPAWN_OFFSET / 2;
    private static final int COUNT = 5;
    private Mesh mesh = null;
    private final Deque<GameObject> pipeline = new ArrayDeque<>();

    public Deque<GameObject> getPipeline() {
        return pipeline;
    }

    public void setPipeline() {
        if (pipeline.isEmpty()) {
            while (pipeline.size() != COUNT) {
                generate();
            }
        }
        float firstPosition = pipeline.getFirst().getPosition().z;

        if (firstPosition > OFFSET) {
            pipeline.pop();
            generate();
        }
    }

    private void generate() {
        pipeline.addLast(new GameObject(mesh)); //todo add random textures
        pipeline.getLast().setPosition(0, 0, SPAWN_OFFSET * pipeline.size() + OFFSET);
    }

    public void update() {
        setPipeline();
        for (GameObject gameObject : pipeline) {
            gameObject.move(0, 0, RunnerGame.SCROLL_SPEED);
        }
    }

    public void init() throws Exception {
        mesh = OBJLoader.loadMesh("cluster.obj");
        mesh.setColor(new Vector3f(0.8f));
        setPipeline();
    }

    public void cleanup() {
        mesh.cleanUp();
    }
}
