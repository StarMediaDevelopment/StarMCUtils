package com.starmediadev.plugins.starmcutils.util;

import java.util.Objects;

public class Position {
    protected int x, y, z;
    protected float yaw, pitch;

    public Position() {
        this(0, 0, 0);
    }

    public Position(int x, int y, int z) {
        this(x, y, z, 0, 0);
    }

    public Position(int x, int y, int z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
    }

    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
