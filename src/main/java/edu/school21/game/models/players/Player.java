package edu.school21.game.models.players;

import edu.school21.engine.render.Mesh;
import edu.school21.game.RunnerGame;
import edu.school21.game.models.CollisionModel;
import edu.school21.game.models.GameObject;
import edu.school21.game.utils.TextureContainer;
import edu.school21.game.utils.types.AnimationType;
import edu.school21.game.utils.types.MeshType;
import edu.school21.game.utils.types.TextureType;

import static edu.school21.game.utils.MeshContainer.animations;
import static edu.school21.game.utils.MeshContainer.meshes;

public class Player extends GameObject {
    private static final float PLAYER_SPEED = 0.05f;
    private static final float MAX_JUMP_HEIGHT = 1f;
    private final Mesh[] runAnimationFrames;
    private final Mesh[] rollAnimationFrames;
    private final Mesh[] jumpAnimationFrames;
    private boolean inAir = false;
    private boolean inRoll = false;
    private float altitude = 0f;
    private float jumpTargetHeight = 1f;

    public Player() {
        super();
        runAnimationFrames = animations.get(AnimationType.RUN);
        rollAnimationFrames = animations.get(AnimationType.ROLL);
        jumpAnimationFrames = animations.get(AnimationType.JUMP);
        this.mesh = runAnimationFrames[0];
        this.texture = TextureContainer.textures.get(TextureType.PLAYER);
        this.collisionModel = new CollisionModel(meshes.get(MeshType.PLAYER), this.position);
        this.mesh.setColor(0.8f);
        position.z -= 3f;
    }

    public void moveRight() {
        if (position.x < 0.6f && !inRoll && !RunnerGame.inPause) {
            moveX(PLAYER_SPEED);
        }
    }

    public void moveLeft() {
        if (position.x > -0.6f && !inRoll && !RunnerGame.inPause) {
            moveX(-PLAYER_SPEED);
        }
    }

    public void setInAir() {
        if (!inRoll && !RunnerGame.inPause) {
            this.inAir = true;
        }
    }

    public void setInRoll(boolean val) {
        if (!inAir && !RunnerGame.inPause) {
            inRoll = val;
        }
    }

    public void update(float scores) {
        mesh = runAnimationFrames[(int) (scores * 8) % runAnimationFrames.length];
        collisionModel.setMesh(mesh);
        if (inRoll) {
            mesh = rollAnimationFrames[(int) (scores * 8) % rollAnimationFrames.length];
            collisionModel.setMesh(mesh);
        }
        if (inAir) {
            int direction = altitude >= 0 ? 1 : -1;

            mesh = jumpAnimationFrames[direction == 1 ? (Math.min((int) (altitude / PLAYER_SPEED) / 2, 9)) : 9];
            collisionModel.setMesh(mesh);

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
