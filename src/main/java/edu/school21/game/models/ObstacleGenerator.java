package edu.school21.game.models;

import edu.school21.game.RunnerGame;
import edu.school21.game.models.obstacles.*;
import edu.school21.game.utils.types.MeshType;
import edu.school21.utils.RandomGenerator;

import java.util.ArrayDeque;
import java.util.Deque;

import static edu.school21.game.utils.types.MeshType.*;

public class ObstacleGenerator {
    private static final float GENERATE_DISTANCE = -50f;
    private static final int OBSTACLES_COUNT = 5;
    private static final float DEFAULT_RANGE = 10f;
    private final Deque<GameObject> pipeline = new ArrayDeque<>();
    private float movedDistance = 0;
    private float range = DEFAULT_RANGE;

    public Deque<GameObject> getPipeline() {
        return pipeline;
    }

    public void init() {
        generate();
    }

    public void clear() {
        pipeline.clear();
        range = DEFAULT_RANGE;
        movedDistance = 0f;
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
            range = RandomGenerator.generate(10f, 20f);
        }
    }

    public void generate() {
        int type = RandomGenerator.generate(OBSTACLES_COUNT);
        GameObject obstacle;

        if (type == CHAIR.ordinal()) {
            Chair chair = new Chair();

            chair.randomPositionRotation();
            obstacle = chair;
        } else if (type == SIGN.ordinal()) {
            Sign sign = new Sign();

            sign.randomPositionRotation();
            obstacle = sign;
        } else if (type == BOARD.ordinal()) {
            obstacle = new Board();
        } else if (type == FENCE.ordinal()) {
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
