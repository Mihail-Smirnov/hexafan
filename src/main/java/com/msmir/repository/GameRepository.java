package com.msmir.repository;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameRoom;
import com.msmir.containers.game.GameSettings;
import com.msmir.entity.GameReplay;
import com.msmir.entity.user.User;
import com.msmir.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameRepository {
  @Autowired
  private UserService userService;
  @Autowired
  private GameReplayRepository gameReplayRepository;
  private final Map<String, Game> games;
  private final Timer endGameTimer;

  public GameRepository(){
    this.games = new HashMap<>();
    this.endGameTimer = new Timer();
  }

  public Game newGame(User user1, User user2){
    Game game = new Game(UUID.randomUUID(), new GameSettings(), user1, user2, this::onGameClose);
    games.put(game.getId().toString(), game);
    return game;
  }

  public Game newGame(GameRoom room){
    if(room != null){
      if(room.getUser1() != null && room.getUser2() != null && room.getSettings() != null) {
        Game game = new Game(UUID.randomUUID(), room.getSettings(), room.getUser1(),
            room.getUser2(), this::onGameClose);
        games.put(game.getId().toString(), game);
        return game;
      }
    }
    return null;
  }

  public Game getGame(String gameId){
    if(gameId != null){
      return games.get(gameId);
    }else{
      return null;
    }
  }

  private void onGameClose(Game game){
    userService.leaveGame(game.getWhitePlayer(), game);
    userService.leaveGame(game.getBlackPlayer(), game);
    gameReplayRepository.save(new GameReplay(game));
    endGameTimer.schedule(new TimerTask() {
      @Override
      public void run() { games.remove(game.getId().toString()); }
    }, 60000);
  }
}
