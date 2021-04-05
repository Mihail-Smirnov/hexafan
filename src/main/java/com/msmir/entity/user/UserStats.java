package com.msmir.entity.user;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class UserStats implements Serializable {
  private int gamesWin;
  private int gamesLose;
  private int gamesDraw;
  private int totalGameTime;

  public UserStats() {
    this.gamesWin = 0;
    this.gamesLose = 0;
    this.gamesDraw = 0;
    this.totalGameTime = 0;
  }

  public int getGamesWin() {
    return gamesWin;
  }

  public void setGamesWin(int gamesWin) {
    this.gamesWin = gamesWin;
  }

  public void addGamesWin(){
    this.gamesWin++;
  }

  public int getGamesLose() {
    return gamesLose;
  }

  public void setGamesLose(int gamesLose) {
    this.gamesLose = gamesLose;
  }

  public void addGamesLose(){
    this.gamesLose++;
  }

  public int getGamesDraw() {
    return gamesDraw;
  }

  public void setGamesDraw(int gamesDraw) {
    this.gamesDraw = gamesDraw;
  }

  public void addGamesDraw(){
    this.gamesDraw++;
  }

  public int getTotalGameTime() {
    return totalGameTime;
  }

  public void setTotalGameTime(int totalGameDuration) {
    this.totalGameTime = totalGameDuration;
  }

  public void addGameTime(int seconds){
    totalGameTime += seconds;
  }
}
