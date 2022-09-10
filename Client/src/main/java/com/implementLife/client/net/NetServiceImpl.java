package com.implementLife.client.net;

import com.implementLife.commonDTO.comServerEntity.Player;
import com.implementLife.commonDTO.comServerEntity.Room;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

import static com.implementLife.client.PropService.getPropService;

public final class NetServiceImpl implements NetService {
    //region Singleton
    private static NetService netService;
    public static NetService getNetService() {
        if (NetServiceImpl.netService == null) {
            netService = (NetService) Proxy.newProxyInstance(
                NetService.class.getClassLoader(),
                new Class[]{NetService.class},
                new ExceptionProxyHandler(new NetServiceImpl()));
        }
        return NetServiceImpl.netService;
    }
    private NetServiceImpl() {}
    //endregion

    private static final String BASE_URL = "http://ilfa.dp.ua:8080";
    private final RestTemplate consumer = new RestTemplate();
    private String uuidOfThisDevice = getPropService().getId();
    private Player currentPlayer;

    private Player getCurrentPlayer() {
        return currentPlayer;
    }
    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public Room getRoom(String uuid) {
        return consumer.getForObject(url("/getRoom", new P("uuid", uuid)), Room.class);
    }

    @Override
    public Player registerPlayer() {
        Player player = consumer.postForObject(url("/reg"), null, Player.class);
        if (player == null) {
            throw new RuntimeException("Bad Register");
        }
        setCurrentPlayer(player);
        getPropService().saveId(player.getId().toString());
        return player;
    }

    @Override
    public Player getPlayer() {
        if (getCurrentPlayer() != null) {
            return getCurrentPlayer();
        }
        if (uuidOfThisDevice == null) {
            uuidOfThisDevice = registerPlayer().getId().toString();
            return getCurrentPlayer();
        }
        Player player = consumer.getForObject(url("/getPlayer", new P("uuid", uuidOfThisDevice)), Player.class);
        if (player == null) {
            registerPlayer();
        } else {
            setCurrentPlayer(player);
        }
        return getCurrentPlayer();
    }

    @Override
    public Room createRoom(String name) {
        Player player = getPlayer();
        return consumer.postForObject(url("/createRoom"),
            body(new P("playerUUID", player.getId().toString()), new P("name", name)),
            Room.class
        );
    }

    @Override
    public List<Room> getAllRooms() {
        Room[] allRooms = consumer.getForObject(url("/getAllRooms"), Room[].class);
        return Arrays.stream(allRooms).collect(Collectors.toList());
    }

    @Override
    public List<Player> getAllPlayers() {
        Collection<Object> allRooms = consumer.getForObject(url("/getAllPlayers"), Collection.class);
        return allRooms.stream().map(e -> (Player) e).collect(Collectors.toList());
    }

    private String url(String path) {
        return BASE_URL + path;
    }

    private HttpEntity<MultiValueMap<String, Object>> body(P... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        for (P param : params) {
            map.add(param.getKey(), param.getValue());
        }
        return new HttpEntity<>(map, headers);
    }

    private Map<String, String> uri(P... params) {
        HashMap<String, String> result = new HashMap<>();
        for (P param : params) {
            result.put(param.getKey(), param.getValue().toString());
        }
        return result;
    }

    private String url(String path, P... getParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url(path));
        for (P param : getParams) {
            builder.queryParam(param.getKey(), param.getValue().toString());
        }
        return builder.build().toString();
    }

    private static class P {
        public P(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }

        private String key;
        private Object value;
    }
}
