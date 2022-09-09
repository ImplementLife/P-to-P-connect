package com.implementLife.commonDTO.game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Unit implements Serializable {
    private static final long serialVersionUID = 2663269020281263773L;

    private int id;
    private Vec2D pos;
    private Vec2D speedVec;
    private Vec2D speedVecAccelerate;
    private LinkedList<Vec2D> members;
    private double angle;

    public Unit() {
    }
    public Unit(int id, Vec2D pos, double angle) {
        this.id = id;
        this.pos = pos;
        this.angle = angle;
    }
    public Unit(Unit other) {
        this.id = other.id;
        this.pos = other.pos;
        this.speedVec = other.speedVec;
        this.speedVecAccelerate = other.speedVecAccelerate;
        this.members = other.members;
        this.angle = other.angle;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Vec2D getPos() {
        return pos;
    }
    public void setPos(Vec2D pos) {
        this.pos = pos;
    }

    public double getAngle() {
        return angle;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Vec2D getSpeedVec() {
        return speedVec;
    }
    public void setSpeedVec(Vec2D speedVec) {
        this.speedVec = speedVec;
    }

    public Vec2D getSpeedVecAccelerate() {
        return speedVecAccelerate;
    }
    public void setSpeedVecAccelerate(Vec2D speedVecAccelerate) {
        this.speedVecAccelerate = speedVecAccelerate;
    }

    public LinkedList<Vec2D> getMembers() {
        return members;
    }
    public void setMembers(LinkedList<Vec2D> members) {
        this.members = members;
    }
    public void addMember(Vec2D member) {
        if (members == null) members = new LinkedList<>();
        members.add(member);
    }

    @Override
    public String toString() {
        return "Unit {" +
            "\n id=" + id +
            ",\n pos=" + pos.toString() +
            ",\n speedVec=" + speedVec.toString() +
            ",\n speedVecAccelerate=" + speedVecAccelerate.toString() +
            ",\n members=[\n    " + members.stream().map(Vec2D::toString).collect(Collectors.joining(",\n    ")) + "]" +
            ",\n angle=" + angle +
            "\n}";
    }
}
