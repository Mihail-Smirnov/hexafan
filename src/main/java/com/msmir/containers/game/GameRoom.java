package com.msmir.containers.game;

import com.msmir.entity.user.User;

public class GameRoom {
  private String gameId;
  private GameSettings settings;
  private boolean hidden;
  private User user1;
  private User user2;

  public GameRoom() {
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

  public User getUser1() {
    return user1;
  }

  public void setUser1(User user1) {
    this.user1 = user1;
  }

  public User getUser2() {
    return user2;
  }

  public void setUser2(User user2) {
    this.user2 = user2;
  }
}
