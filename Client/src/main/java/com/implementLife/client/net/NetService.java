package com.implementLife.client.net;

import com.implementLife.commonDTO.comServerEntity.Player;
import com.implementLife.commonDTO.comServerEntity.Room;

import java.lang.reflect.Proxy;
import java.util.List;

public interface NetService {
    Room getRoom(String uuid);

    Player registerPlayer();

    Player getPlayer();

    Room createRoom(String name);

    List<Room> getAllRooms();

    List<Player> getAllPlayers();
}
