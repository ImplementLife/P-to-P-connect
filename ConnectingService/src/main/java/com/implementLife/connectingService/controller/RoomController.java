package com.implementLife.connectingService.controller;

import com.implementLife.commonDTO.comServerEntity.Player;
import com.implementLife.connectingService.service.RoomService;
import com.implementLife.commonDTO.comServerEntity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.UUID;

@RestController("/")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping("/setNewHostIP")
    public ResponseEntity<String> setNewHostIP(HttpServletRequest request, @RequestParam String roomUUID) {
        String ip = roomService.setNewHostIP(request.getRemoteAddr(), roomUUID).getHostIP();
        return ResponseEntity.ok(ip);
    }

    @GetMapping("/getRoomHostIP")
    public ResponseEntity<String> getRoomHostIP(HttpServletRequest request, @RequestParam String uuid) {
        return ResponseEntity.ok(roomService.getRoom(uuid).getHostIP());
    }

    @GetMapping("/getPlayer")
    public ResponseEntity<Player> getPlayer(HttpServletRequest request, @RequestParam String uuid) {
        return ResponseEntity.ok(roomService.getPlayer(uuid));
    }

    @GetMapping("/getRoom")
    public ResponseEntity<Room> getRoom(HttpServletRequest request, @RequestParam String uuid) {
        return ResponseEntity.ok(roomService.getRoom(uuid));
    }

    @PostMapping("/reg")
    public ResponseEntity<Player> reg(HttpServletRequest request) {
        return ResponseEntity.ok(roomService.registration(request.getRemoteAddr()));
    }

    @PostMapping("/createRoom")
    public ResponseEntity<Room> createRoomIP(HttpServletRequest request,
                                             @RequestParam String playerUUID,
                                             @RequestParam(required = false, defaultValue = "") String name
    ) {
        Room newRoom = roomService.createNewRoom(UUID.fromString(playerUUID), name);
        return ResponseEntity.ok(newRoom);
    }

    //region Get All
    @GetMapping("/getAllRooms")
    public ResponseEntity<Collection<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/getAllPlayers")
    public ResponseEntity<Collection<Player>> getAllPlayers() {
        return ResponseEntity.ok(roomService.getAllPlayers());
    }

    //endregion
}
