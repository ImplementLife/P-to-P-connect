package com.implementLife.client.UI.components;

import com.implementLife.commonDTO.comServerEntity.Room;

import java.util.UUID;

public class JTreeRoomView {
    private Room room;

    public JTreeRoomView(Room room) {
        this.room = room;
    }

    public UUID getId() {
        return room.getId();
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return room.getName();
    }
}
