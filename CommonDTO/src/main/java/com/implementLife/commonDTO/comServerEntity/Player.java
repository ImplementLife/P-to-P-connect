package com.implementLife.commonDTO.comServerEntity;

import java.util.List;
import java.util.UUID;

public class Player {
    private UUID id;
    private String name;
    private String ip;
    private Room currentRoom;
    private boolean isHost;
    private List<Player> friends;

    public Player() {}

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public boolean isHost() {
        return isHost;
    }
    public void setHost(boolean host) {
        isHost = host;
    }

    public List<Player> getFriends() {
        return friends;
    }
    public void setFriends(List<Player> friends) {
        this.friends = friends;
    }
}
