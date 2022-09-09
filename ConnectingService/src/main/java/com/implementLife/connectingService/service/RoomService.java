package com.implementLife.connectingService.service;

import com.implementLife.commonDTO.comServerEntity.Player;
import com.implementLife.commonDTO.comServerEntity.Room;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface RoomService {
    Player registration(String ip);
    Player updatePlayerIp(UUID uuid, String ip);
    Player getPlayer(String uuid);

    Room createNewRoom(UUID idPlayer, String name);
    Room setNewHostIP(String ip, String uuidRoom);
    Room getRoom(String uuid);

    void notifyPlayersHostChanged(Room room);
    void notifyPlayersHostIpChanged(Room room);
    void changeHost(Room room, Player player);
    void changeHostIp(Room room, Player player);

    Collection<Room> getAllRooms();
    Collection<Player> getAllPlayers();
}
