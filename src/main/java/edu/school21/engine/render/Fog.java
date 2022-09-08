package edu.school21.engine.render;

import org.joml.Vector3f;

public class Fog {
    private final Vector3f color;
    private final float density;
    private boolean active;

    public Fog(boolean active, Vector3f color, float density) {
        this.active = active;
        this.color = color;
        this.density = density;
    }

    public boolean isActive() {
        return active;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getDensity() {
        return density;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
