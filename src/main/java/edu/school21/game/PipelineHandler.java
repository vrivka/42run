package edu.school21.game;

import java.util.ArrayList;
import java.util.List;

public class PipelineHandler {
    private final EnvironmentGenerator environmentGenerator = new EnvironmentGenerator();
    private final ObstacleGenerator obstacleGenerator = new ObstacleGenerator();

    public GameObject getForwardObstacle() {
        return obstacleGenerator.getPipeline().getFirst();
    }

    public List<GameObject> collect() {
        List<GameObject> result = new ArrayList<>();

        result.addAll(environmentGenerator.getPipeline());
        result.addAll(obstacleGenerator.getPipeline());
        return result;
    }

    public void update() {
        environmentGenerator.update();
        obstacleGenerator.update();
    }

    public void init() throws Exception {
        environmentGenerator.init();
        obstacleGenerator.init("sign.obj", "chair.obj", "board.obj");
    }

    public void cleanup() {
        environmentGenerator.cleanup();
        obstacleGenerator.cleanup();
    }
}
