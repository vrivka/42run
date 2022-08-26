package edu.school21.game;

import edu.school21.engine.render.Mesh;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class Player extends GameObject {
    private static final float PLAYER_SPEED = 0.05f;
    private static final float MAX_JUMP_HEIGHT = 1f;
    private static float MAX_Y;
    private boolean inAir = false;
    private boolean inSlide = false;
    private float altitude = 0f;
    private float jumpTargetHeight = 1f;

    public Player(Mesh mesh) {
        super(mesh);
        this.mesh.setColor(new Vector3f(0.8f));
        position.z -= 3f;
        MAX_Y = mesh.max.y;
    }

    public void moveRight() {
        if (position.x < 0.6f && !inSlide && !RunnerGame.inPause) {
            move(PLAYER_SPEED, 0, 0);
        }
    }

    public void moveLeft() {
        if (position.x > -0.4f && !inSlide && !RunnerGame.inPause) {
            move(-PLAYER_SPEED, 0, 0);
        }
    }

    public void setInAir() {
        if (!inSlide && !RunnerGame.inPause) {
            this.inAir = true;
        }
    }

    public void setInSlide(boolean val) {
        if (!inAir && !RunnerGame.inPause) {
            inSlide = val;
        }
    }

    public void update() {
        if (inSlide) {
            setRotation(-90f, 0, 0);
            mesh.max.y = 0.5f;
        } else {
            setRotation(0, 0, 0);
            mesh.max.y = MAX_Y;
        }
        if (inAir) {
            int side = altitude >= 0 ? 1 : -1;

            if (altitude < jumpTargetHeight) {
                move(0, PLAYER_SPEED * side, 0);
                altitude += PLAYER_SPEED;
            } else if (altitude >= jumpTargetHeight) {
                altitude = -jumpTargetHeight;
                jumpTargetHeight = 0;
            }
            if (side < 0 && altitude >= 0) {
                inAir = false;
                jumpTargetHeight = MAX_JUMP_HEIGHT;
            }
        }
    }
}
