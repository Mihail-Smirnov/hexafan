package com.msmir.vm;

public class GameFigureMoveVm {
  private FigureMoveVm moveVm;
  private String gameId;

  public GameFigureMoveVm() {
  }

  public FigureMoveVm getMoveVm() {
    return moveVm;
  }

  public void setMoveVm(FigureMoveVm moveVm) {
    this.moveVm = moveVm;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}
