package com.msmir.containers.game;

import java.time.Duration;
import java.util.Set;
import javax.persistence.Embeddable;

@Embeddable
public class GameSettings {

  private static final Set<Integer> MOVE_DURATIONS = Set.of(30, 60, 120, 300);
  private static final Set<String> PLAYERS = Set.of("WHITE", "BLACK");
  private static final Set<Integer> BOARD_SIZES = Set.of(4, 6, 8);
  private static final Set<String> ARRANGEMENTS = Set.of("NORMAL", "WIDE");

  private int moveDuration = 120;
  private String firstPlayer = "WHITE";
  private int boardSize = 6;
  private String arrangement = "NORMAL";

  public GameSettings() {
  }

  public boolean isValid(){
    return MOVE_DURATIONS.contains(this.moveDuration) &&
        PLAYERS.contains(this.firstPlayer) &&
        BOARD_SIZES.contains(this.boardSize) &&
        ARRANGEMENTS.contains(this.arrangement);
  }

  public int getMoveDuration() {
    return moveDuration;
  }

  public Duration getMoveDurationParsed() {
    return Duration.ofSeconds(moveDuration);
  }

  public void setMoveDuration(int moveDuration) {
    this.moveDuration = moveDuration;
  }

  public String getFirstPlayer() {
    return firstPlayer;
  }

  public void setFirstPlayer(String firstPlayer) {
    this.firstPlayer = firstPlayer;
  }

  public int getBoardSize() {
    return boardSize;
  }

  public void setBoardSize(int boardSize) {
    this.boardSize = boardSize;
  }

  public String getArrangement() {
    return arrangement;
  }

  public void setArrangement(String arrangement) {
    this.arrangement = arrangement;
  }
}
