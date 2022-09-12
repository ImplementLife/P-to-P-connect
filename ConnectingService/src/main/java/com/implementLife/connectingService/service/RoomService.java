package com.implementLife.connectingService.service;

import com.implementLife.commonDTO.comServerEntity.Event;
import com.implementLife.commonDTO.comServerEntity.Player;
import com.implementLife.commonDTO.comServerEntity.Room;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface RoomService {
    Player registration(String ip);
    Player updatePlayerIp(UUID id, String ip);
    Player getPlayer(UUID id);

    Room createNewRoom(UUID ownerPlayerId, String name);
    Room setNewHostIP(UUID roomId, String ip);
    Room getRoom(UUID id);

    void sendEventToPlayers(UUID roomId, Event event);
    void changeHost(UUID roomId, UUID playerId, String ip);

    Collection<Room> getAllRooms();
    Collection<Player> getAllPlayers();
}
