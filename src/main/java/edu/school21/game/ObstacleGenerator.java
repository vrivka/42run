package edu.school21.game;

import edu.school21.engine.render.Mesh;
import edu.school21.game.obstacles.Board;
import edu.school21.game.obstacles.Chair;
import edu.school21.game.obstacles.Sign;
import edu.school21.utils.OBJLoader;
import edu.school21.utils.RandomGenerator;
import org.joml.Vector3f;

import java.util.ArrayDeque;
import java.util.Deque;

public class ObstacleGenerator {
    private static final float GENERATE_DISTANCE = -50f;
    private Mesh[] obstacles;
    private int obstaclesCount = 0;
    private final Deque<GameObject> pipeline = new ArrayDeque<>();
    private float movedDistance = 0;

    public Deque<GameObject> getPipeline() {
        return pipeline;
    }

    public void init(String... paths) throws Exception {
        obstaclesCount = paths.length;
        obstacles = new Mesh[obstaclesCount];

        for (int i = 0; i < obstaclesCount; i++) {
            obstacles[i] = OBJLoader.loadMesh(paths[i]);
            obstacles[i].setColor(new Vector3f(0.8f));
        }
        generate();
    }

    public void clear() {
        pipeline.clear();
        generate();
    }

    public void remove() {
        if (pipeline.getFirst().getPosition().z > 0) {
            pipeline.pop();
        }
    }

    public void update() {
        remove();
        for (GameObject gameObject : pipeline) {
            gameObject.move(0, 0, RunnerGame.SCROLL_SPEED);
        }
        movedDistance += RunnerGame.SCROLL_SPEED;
        if (movedDistance >= 10f) {
            generate();
            movedDistance = 0f;
        }
    }

    public void generate() {
        int id = RandomGenerator.generate(0, obstaclesCount);
        GameObject obstacle;

        if (id == 1) {
            Chair chair = new Chair(obstacles[1]);
            chair.randomPositionRotation();
            obstacle = chair;
        } else if (id == 0) {
            Sign sign = new Sign(obstacles[0]);
            sign.randomPositionRotation();
            obstacle = sign;
        } else {
            obstacle = new Board(obstacles[2]);
        }

        obstacle.move(0, 0, GENERATE_DISTANCE);
        pipeline.addLast(obstacle);
    }

    public void cleanup() {
        for (Mesh obstacle : obstacles) {
            obstacle.cleanup();
        }
    }
}
