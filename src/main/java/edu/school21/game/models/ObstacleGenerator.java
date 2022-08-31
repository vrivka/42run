package edu.school21.game.models;

import edu.school21.game.RunnerGame;
import edu.school21.game.models.obstacles.*;
import edu.school21.game.utils.types.MeshType;
import edu.school21.utils.RandomGenerator;

import java.util.ArrayDeque;
import java.util.Deque;

public class ObstacleGenerator {
    private static final float GENERATE_DISTANCE = -80f;
    private final Deque<GameObject> pipeline = new ArrayDeque<>();
    private static final int OBSTACLES_COUNT = 5;
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
            gameObject.moveZ(RunnerGame.gameSpeed);

            if (gameObject.isTypeOf(MeshType.COLLECTABLE)) {
                gameObject.rotateY(10f);
            }
        }
        movedDistance += RunnerGame.gameSpeed;
        if (movedDistance >= range) {
            generate();
            movedDistance = 0f;
            range = RandomGenerator.generate(10f, 15f);
        }
    }

    public void generate() {
        int id = RandomGenerator.generate(0, OBSTACLES_COUNT);
        GameObject obstacle;

        if (id == 0) {
            Chair chair = new Chair();
            chair.randomPositionRotation();
            obstacle = chair;
        } else if (id == 1) {
            Sign sign = new Sign();
            sign.randomPositionRotation();
            obstacle = sign;
        } else if (id == 2) {
            obstacle = new Board();
        } else if (id == 3) {
            obstacle = new Fence();
        } else {
            Collectable collectable = new Collectable();

            collectable.randomPosition();
            obstacle = collectable;
        }

        obstacle.moveZ(GENERATE_DISTANCE);
        pipeline.addLast(obstacle);
    }
}
