package com.msmir.controller;

import com.msmir.containers.game.GameRoom;
import com.msmir.entity.user.User;
import com.msmir.repository.GameRepository;
import com.msmir.service.UserService;
import com.msmir.service.WaitingService;
import com.msmir.vm.GameRoomVm;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaitingController {

  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private WaitingService waitingService;

  @GetMapping(value = "/data/activeRooms", produces = "application/json")
  public Map<String, GameRoomVm> getActiveRooms(){
    return waitingService.getActiveRooms();
  }

  @GetMapping(value = "/data/myRoomCode", produces = "application/json")
  public String getRoomCode(){
    User user = userService.getCurrentUser();
    return waitingService.getRoomCode(user);
  }

  @PostMapping(value = "/data/connectRoom")
  public String connectRoomByCode(@RequestBody String roomCode){
    User user = userService.getCurrentUser();
    GameRoom room = waitingService.enterRoom(roomCode, user);
    if(room != null){
      return room.getGameId();
    }
    return null;
  }

  @PostMapping(value = "/data/fastGame")
  public String fastGame(){
    User user = userService.getCurrentUser();
    if(user != null){
      if(gameRepository.getGame(user.getGameId()) != null){
        return user.getGameId();
      }
      String code = waitingService.getRandomRoom(user);
      if(code != null){
        return code;
      }
    }
    return "";
  }

  @MessageMapping("/waiting/createRoom")
  @SendTo("/waiting/activeRooms")
  public Map<String, GameRoomVm> createRoom(@RequestBody GameRoom room, Principal principal) {
    User user = userService.getCurrentUser(principal);
    if(user != null && room != null && room.getSettings() != null && room.getSettings().isValid()){
      room.setUser1(user);
      if(gameRepository.getGame(user.getGameId()) == null &&
          waitingService.getRoomCode(user) == null) {
        waitingService.addRoom(room);
      }
    }
    return waitingService.getActiveRooms();
  }

  @MessageMapping("/waiting/deleteRoom")
  @SendTo("/waiting/activeRooms")
  public Map<String, GameRoomVm> deleteRoom(Principal principal) {
    User user = userService.getCurrentUser(principal);
    waitingService.deleteRoom(user);
    return waitingService.getActiveRooms();
  }

  @MessageMapping("/waiting/connectRoom/{code}")
  @SendTo("/waiting/room/{code}")
  public String connectRoom(@RequestBody String code, Principal principal){
    User user = userService.getCurrentUser(principal);
    if(user != null && code != null){
      if(gameRepository.getGame(user.getGameId()) == null){
        GameRoom room = waitingService.enterRoom(code, user);
        if(room != null){
          return room.getGameId();
        }
      }
    }
    return "";
  }
}
