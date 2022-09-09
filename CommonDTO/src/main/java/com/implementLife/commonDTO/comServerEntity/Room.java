package com.implementLife.commonDTO.comServerEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Room {
    private UUID id;
    private List<Player> players;
    private Player hostPlayer;
    private String hostIP;
    private String name;
    private boolean isPublic;

    public Room() {
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }
    private List<Player> getNotNullPlayersList() {
        if (players == null) players = new LinkedList<>();
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void addPlayer(Player player) {
        getNotNullPlayersList().add(player);
    }
    public void addAllPlayer(List<Player> players) {
        getNotNullPlayersList().addAll(players);
    }

    public Player getHostPlayer() {
        return hostPlayer;
    }
    public void setHostPlayer(Player hostPlayer) {
        this.hostPlayer = hostPlayer;
    }

    public String getHostIP() {
        return hostIP;
    }
    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
