package com.msmir.vm;

import com.msmir.containers.game.GameRoom;
import com.msmir.containers.game.GameSettings;

public class GameRoomVm {
  private String gameId;
  private GameSettings settings;
  private boolean hidden;
  private String owner;

  public GameRoomVm() {
  }

  public GameRoomVm(GameRoom room){
    this.gameId = room.getGameId();
    this.settings = room.getSettings();
    this.hidden = room.isHidden();
    if(room.getUser1() != null){
      this.owner = room.getUser1().getUsername();
    }
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public GameSettings getSettings() {
    return settings;
  }

  public void setSettings(GameSettings settings) {
    this.settings = settings;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}

