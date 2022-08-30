package edu.school21.game.models;

import edu.school21.game.RunnerGame;
import edu.school21.game.models.environments.Cluster;

import java.util.ArrayDeque;
import java.util.Deque;

public class EnvironmentGenerator {
    private static final float SPAWN_OFFSET = -16.585f;
    private static final float OFFSET = -SPAWN_OFFSET / 2;
    private static final int COUNT = 5;
    private final Deque<GameObject> pipeline = new ArrayDeque<>();

    public Deque<GameObject> getPipeline() {
        return pipeline;
    }

    public void setEnvironment() {
        if (pipeline.isEmpty()) {
            while (pipeline.size() != COUNT) {
                generate();
            }
        }
        float firstPosition = pipeline.getFirst().getPosition().z;

        if (firstPosition >= OFFSET) {
            pipeline.pop();
            generate();
        }
    }

    private void generate() {
        Cluster cluster = new Cluster();

        cluster.setPosition(0, 0, SPAWN_OFFSET * (pipeline.size() + 1) + OFFSET);
        pipeline.addLast(cluster);
    }

    public void update() {
        setEnvironment();

        for (GameObject gameObject : pipeline) {
            gameObject.moveZ(RunnerGame.gameSpeed);
        }
    }

    public void clear() {
        pipeline.clear();
        setEnvironment();
    }

    public void init() {
        setEnvironment();
    }

}
