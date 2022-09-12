package com.implementLife.connectingService.service;


import com.implementLife.commonDTO.comServerEntity.Event;
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
    public Player updatePlayerIp(UUID id, String ip) {
        Player player = playersById.get(id);
        player.setIp(ip);
        return player;
    }

    @Override
    public Player getPlayer(UUID id) {
        return playersById.get(id);
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
    public Room setNewHostIP(UUID uuidRoom, String ip) {
        Room room = rooms.get(uuidRoom);
        room.setHostIP(ip);
        return room;
    }

    @Override
    public Room getRoom(UUID uuid) {
        return rooms.get(uuid);
    }

    @Override
    public void sendEventToPlayers(UUID roomId, Event event) {

    }

    @Override
    public void changeHost(UUID roomId, UUID playerId, String ip) {

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
