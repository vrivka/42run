package edu.school21.game.models;

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

    public void clear() {
        environmentGenerator.clear();
        obstacleGenerator.clear();
    }

    public void init() {
        environmentGenerator.init();
        obstacleGenerator.init();
    }
}
