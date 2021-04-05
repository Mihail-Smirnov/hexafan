package com.msmir.controller;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameStatus;
import com.msmir.containers.game.messages.PlayerMessage;
import com.msmir.containers.game.messages.SystemMessage;
import com.msmir.entity.user.User;
import com.msmir.repository.GameRepository;
import com.msmir.service.UserService;
import com.msmir.vm.GameFigureMoveVm;
import com.msmir.vm.GameVm;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GameController {

  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private UserService userService;

  @GetMapping(value = "/data/currentGame", produces = "application/json")
  @ResponseBody
  public String getCurrentGame(){
    User user = userService.getCurrentUser();
    if(user != null){
      Game game = gameRepository.getGame(user.getGameId());
      if(game != null){
        return game.getId().toString();
      }
    }
    return "";
  }

  @GetMapping(value = "/data/{gameId}/game", produces = "application/json")
  @ResponseBody
  public GameVm getGame(@PathVariable(name = "gameId") String gameId) {
    User user = userService.getCurrentUser();
    Game game = gameRepository.getGame(gameId);

    if (user != null && game != null) {
      return new GameVm(game, user);
    } else {
      return new GameVm();
    }
  }

  @GetMapping(value = "/data/{gameId}/status", produces = "application/json")
  @ResponseBody
  public GameStatus getStatus(@PathVariable(name = "gameId") String gameId) {
    User user = userService.getCurrentUser();
    Game game = gameRepository.getGame(gameId);

    if (user != null && game != null) {
      return game.getStatus();
    } else {
      return new GameStatus();
    }
  }

  @MessageMapping("/game/{gameId}/move")
  @SendTo("/game/{gameId}/status")
  public GameStatus doMove(@RequestBody GameFigureMoveVm gameMoveVm,
      Principal principal) {
    User user = userService.getCurrentUser(principal);
    Game game = gameRepository.getGame(gameMoveVm.getGameId());

    if (user != null && game != null) {
      game.doMove(gameMoveVm.getMoveVm(), user);
      return game.getStatus();
    }
    return new GameStatus();
  }

  @MessageMapping("/game/{gameId}/systemMessage")
  @SendTo("/game/{gameId}/systemMessages")
  public SystemMessage sendSystemMessage(@RequestBody SystemMessage message,
      Principal principal) {
    User user = userService.getCurrentUser(principal);
    Game game = gameRepository.getGame(message.getGameId());
    if (user != null && game != null) {
      if (game.processSystemMessage(message, user)) {
        return message;
      }
    }
    return new SystemMessage();
  }

  @MessageMapping("/game/{gameId}/message")
  @SendTo("/game/{gameId}/messages")
  public PlayerMessage sendMessage(@RequestBody PlayerMessage message,
      Principal principal) {
    User user = userService.getCurrentUser(principal);
    Game game = gameRepository.getGame(message.getGameId());
    if (user != null && game != null) {
      if (game.processMessage(message, user)) {
        return message;
      }
    }
    return new PlayerMessage();
  }
}
