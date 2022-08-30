package edu.school21.game.models.players;

import edu.school21.engine.render.Mesh;
import edu.school21.game.RunnerGame;
import edu.school21.game.models.CollisionModel;
import edu.school21.game.models.GameObject;
import edu.school21.game.utils.TextureContainer;
import edu.school21.game.utils.types.AnimationType;
import edu.school21.game.utils.types.ClusterType;
import org.joml.Vector3f;

import static edu.school21.game.utils.MeshContainer.animations;

public class Player extends GameObject {
    private static final Vector3f IN_SLIDE_ROTATION = new Vector3f(-90f, 0, 0);
    private static final Vector3f OUT_SLIDE_ROTATION = new Vector3f(0, 0, 0);
    private static final float COLLISION_Y_IN_SLIDE = 0.5f;
    private static final float PLAYER_SPEED = 0.05f;
    private static final float MAX_JUMP_HEIGHT = 1f;
    private static float MAX_Y;

    private Mesh[] runAnimationFrames;
    private boolean inAir = false;
    private boolean inSlide = false;
    private float altitude = 0f;
    private float jumpTargetHeight = 1f;

    public Player() {
        super();
        runAnimationFrames = animations.get(AnimationType.RUN);
        this.mesh = runAnimationFrames[0];
        this.texture = TextureContainer.clusterTextures.get(ClusterType.EVOLUTION);
        this.collisionModel = new CollisionModel(this.mesh, this.position);
        this.mesh.setColor(0.8f);
        position.z -= 3f;
        MAX_Y = mesh.getMax().y;
    }

    public void moveRight() {
        if (position.x < 0.6f && !inSlide && !RunnerGame.inPause) {
            moveX(PLAYER_SPEED);
        }
    }

    public void moveLeft() {
        if (position.x > -0.4f && !inSlide && !RunnerGame.inPause) {
            moveX(-PLAYER_SPEED);
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

    public void update(float scores) {
        mesh = runAnimationFrames[(int) (scores * 8) % runAnimationFrames.length];
        collisionModel.setMesh(mesh);
        if (inSlide) {
            setRotation(IN_SLIDE_ROTATION);
            mesh.getMax().y = COLLISION_Y_IN_SLIDE;
        } else {
            setRotation(OUT_SLIDE_ROTATION);
            mesh.getMax().y = MAX_Y;
        }
        if (inAir) {
            int direction = altitude >= 0 ? 1 : -1;

            if (altitude < jumpTargetHeight) {
                moveY(PLAYER_SPEED * direction);
                altitude += PLAYER_SPEED;
            } else if (altitude >= jumpTargetHeight) {
                altitude = -jumpTargetHeight;
                jumpTargetHeight = 0;
            }
            if (direction < 0 && altitude >= 0) {
                inAir = false;
                jumpTargetHeight = MAX_JUMP_HEIGHT;
            }
        }
    }
}
