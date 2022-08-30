package edu.school21.game.models;

import edu.school21.game.RunnerGame;
import edu.school21.game.models.obstacles.Board;
import edu.school21.game.models.obstacles.Chair;
import edu.school21.game.models.obstacles.Sign;
import edu.school21.utils.RandomGenerator;

import java.util.ArrayDeque;
import java.util.Deque;

public class ObstacleGenerator {
    private static final float GENERATE_DISTANCE = -80f;
    private final Deque<GameObject> pipeline = new ArrayDeque<>();
    private static final int OBSTACLES_COUNT = 3;
    private float range = 10f;
    private float movedDistance = 0;

    public Deque<GameObject> getPipeline() {
        return pipeline;
    }

    public void init() {
        generate();
    }

    public void clear() {
        pipeline.clear();
        range = 10f;
        movedDistance = 0;
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
            gameObject.moveZ(RunnerGame.GAME_SPEED);
        }
        movedDistance += RunnerGame.GAME_SPEED;
        if (movedDistance >= range) {
            generate();
            movedDistance = 0f;
            range = RandomGenerator.generate(10f, 15f);
        }
    }

    public void generate() {
        int id = RandomGenerator.generate(0, OBSTACLES_COUNT);
        GameObject obstacle;

        if (id == 1) {
            Chair chair = new Chair();
            chair.randomPositionRotation();
            obstacle = chair;
        } else if (id == 0) {
            Sign sign = new Sign();
            sign.randomPositionRotation();
            obstacle = sign;
        } else {
            obstacle = new Board();
        }

        obstacle.moveZ(GENERATE_DISTANCE);
        pipeline.addLast(obstacle);
    }
}
