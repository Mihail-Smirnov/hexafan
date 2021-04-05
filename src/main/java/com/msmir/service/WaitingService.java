package com.msmir.service;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameRoom;
import com.msmir.entity.user.User;
import com.msmir.repository.GameRepository;
import com.msmir.vm.GameRoomVm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaitingService {

  private static final int CODE_LENGTH = 4;
  private static final String CODE_DICT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private UserService userService;
  private final List<User> users = new ArrayList<>();
  private final Map<String, GameRoom> rooms = new HashMap<>();

  public Map<String, GameRoomVm> getActiveRooms(){
    return rooms.entrySet().stream()
        .filter(entry -> !entry.getValue().isHidden())
        .collect(Collectors.toMap(Entry::getKey, entry -> new GameRoomVm(entry.getValue())));
  }

  public String addRoom(GameRoom room){
    if(room != null && room.getUser1() != null && room.getSettings() != null){
      String code = generateCode();
      while(rooms.containsKey(code)){
        code = generateCode();
      }
      rooms.put(code, room);
      return code;
    }
    return null;
  }

  public void deleteRoom(User user){
    String code = getRoomCode(user);
    if(code != null){
      rooms.remove(code);
    }
  }

  public GameRoom enterRoom(String roomCode, User user){
    if(roomCode != null && user != null){
      if(rooms.containsKey(roomCode) && getRoomCode(user) == null){
        GameRoom room = rooms.get(roomCode);
        room.setUser2(user);
        Game game = gameRepository.newGame(room);
        if(game != null){
          room.setGameId(game.getId().toString());
          userService.joinGame(room.getUser1(), game);
          userService.joinGame(room.getUser2(), game);
          rooms.remove(roomCode);
          return room;
        }
      }
    }
    return null;
  }

  public String getRandomRoom(User user){
    for(String code : getActiveRooms().keySet()){
      return code;
    }
    return null;
  }

  public String getRoomCode(User user){
    if(user != null){
      for(var entry : rooms.entrySet()){
        if(user.getUsername().equals(entry.getValue().getUser1().getUsername())){
          return entry.getKey();
        }
      }
    }
    return null;
  }

  private String generateCode(){
    Random random = new Random();
    StringBuilder builder = new StringBuilder(CODE_LENGTH);
    for(int i = 0; i < CODE_LENGTH; i++){
      builder.append(CODE_DICT.charAt(random.nextInt(CODE_DICT.length())));
    }
    return builder.toString();
  }
}
