package com.msmir.vm;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameSettings;
import com.msmir.containers.game.GameStatus;
import com.msmir.containers.game.messages.PlayerMessage;
import com.msmir.entity.user.User;
import java.util.List;
import java.util.Map;

public class GameVm {
  private GameStatus status;
  private GameSettings settings;
  private Map<String, CellVm> cells;
  private List<PlayerMessage> messages;
  private String myColor;

  public GameVm() {
  }

  public GameVm(Game game, User user){
    if(game == null) {
      return;
    }

    this.status = game.getStatus();
    this.settings = game.getSettings();
    this.cells = game.getBoardCells();
    this.messages = game.getMessages();
    if(game.getWhitePlayer().getUsername().equals(user.getUsername())){
      this.myColor = "WHITE";
    }else if(game.getBlackPlayer().getUsername().equals(user.getUsername())){
      this.myColor = "BLACK";
    }
  }

  public GameStatus getStatus() {
    return status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public GameSettings getSettings() {
    return settings;
  }

  public void setSettings(GameSettings settings) {
    this.settings = settings;
  }

  public Map<String, CellVm> getCells() {
    return cells;
  }

  public void setCells(Map<String, CellVm> cells) {
    this.cells = cells;
  }

  public String getMyColor() {
    return myColor;
  }

  public void setMyColor(String myColor) {
    this.myColor = myColor;
  }

  public List<PlayerMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<PlayerMessage> messages) {
    this.messages = messages;
  }
}
