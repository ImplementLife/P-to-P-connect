package com.implementLife.commonDTO.game;

import java.io.Serializable;

public class Vec2D implements Serializable {
    private static final long serialVersionUID = -4089757495899237184L;

    private double x;
    private double y;

    public Vec2D() {
        this(0, 0);
    }
    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vec2D(" + x + ',' + y + ')';
    }
}
