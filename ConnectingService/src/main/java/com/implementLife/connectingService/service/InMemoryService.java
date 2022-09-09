package com.implementLife.connectingService.service;


import com.implementLife.commonDTO.comServerEntity.Player;
import com.implementLife.commonDTO.comServerEntity.Room;

import java.util.Collection;
import java.util.TreeMap;
import java.util.UUID;

public class InMemoryService implements RoomService {
    private final TreeMap<UUID, Room> rooms = new TreeMap<>();

    private final TreeMap<String, Player> playersByIp = new TreeMap<>(); //String -> ip
    private final TreeMap<UUID, Player> playersById = new TreeMap<>();   //UUID   -> id

    public InMemoryService() {}

    @Override
    public Player registration(String ip) {
        Player player = new Player();
        player.setId(UUID.randomUUID());
        player.setIp(ip);
        playersById.put(player.getId(), player);
        return player;
    }

    @Override
    public Player updatePlayerIp(UUID uuid, String ip) {
        Player player = playersById.get(uuid);
        player.setIp(ip);
        return player;
    }

    @Override
    public Player getPlayer(String uuid) {
        return playersById.get(UUID.fromString(uuid));
    }

    @Override
    public Room createNewRoom(UUID idPlayer, String name) {
        Player player = playersById.get(idPlayer);
        if (player == null) throw new IllegalArgumentException("User doe's not exist");
        Room room = new Room();
        room.addPlayer(player);
        room.setHostPlayer(player);
        room.setId(UUID.randomUUID());
        if (name == null || "".equals(name)) {
            room.setName(String.format("Room(%s)", rooms.values().size()));
        } else {
            room.setName(name);
        }
        rooms.put(room.getId(), room);
        return room;
    }

    @Override
    public Room setNewHostIP(String ip, String uuidRoom) {
        Room room = rooms.get(UUID.fromString(uuidRoom));
        room.setHostIP(ip);
        return room;
    }

    @Override
    public Room getRoom(String uuid) {
        return rooms.get(UUID.fromString(uuid));
    }

    @Override
    public void notifyPlayersHostChanged(Room room) {
        for (Player player : room.getPlayers()) {
            notifyPlayer(player);
        }
    }

    @Override
    public void notifyPlayersHostIpChanged(Room room) {

    }

    private void notifyPlayer(Player player) {

    }

    @Override
    public void changeHost(Room room, Player player) {

    }

    @Override
    public void changeHostIp(Room room, Player player) {

    }

    @Override
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    @Override
    public Collection<Player> getAllPlayers() {
        return playersById.values();
    }
}
