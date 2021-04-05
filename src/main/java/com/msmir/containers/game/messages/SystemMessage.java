package com.msmir.containers.game.messages;

import java.util.Objects;

public class SystemMessage {
  private String type;
  private String sender;
  private String gameId;
  private boolean statusUpdated;

  public SystemMessage() {
  }

  public SystemMessage(String type, String sender, String gameId) {
    this.type = type;
    this.sender = sender;
    this.gameId = gameId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMessage that = (SystemMessage) o;
    return Objects.equals(type, that.type) &&
        Objects.equals(sender, that.sender) &&
        Objects.equals(gameId, that.gameId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, sender, gameId);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public boolean isStatusUpdated() {
    return statusUpdated;
  }

  public void setStatusUpdated(boolean statusUpdated) {
    this.statusUpdated = statusUpdated;
  }
}
