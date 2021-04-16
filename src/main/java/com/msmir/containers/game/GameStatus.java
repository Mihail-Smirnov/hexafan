package com.msmir.containers.game;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import com.msmir.containers.game.util.FigureMove;
import com.msmir.entity.figures.Figure;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameStatus {
  private boolean isStarted;
  private boolean isFinished;
  private String winner;
  private LocalDateTime startTime;
  private LocalDateTime finishTime;
  private LocalDateTime lastMoveTime;
  private LocalDateTime currentTime;
  private boolean whiteCheck;
  private boolean blackCheck;
  private String activePlayer;
  private List<Figure> figures;

  public GameStatus() {
    this.isStarted = false;
    this.activePlayer = "UNKNOWN";
    this.figures = null;
    this.whiteCheck = false;
    this.blackCheck = false;
    this.winner = "UNKNOWN";
    this.isFinished = false;
    this.startTime = null;
    this.finishTime = null;
    this.lastMoveTime = null;
    this.currentTime = LocalDateTime.now();
  }

  public void update(Game game) {
    this.figures = game.getBoard().getFigures();
    updatePlayerStatus(game.getBoard(), "WHITE");
    updatePlayerStatus(game.getBoard(), "BLACK");

    if(this.isFinished){
      game.close(this.winner);
    }
  }

  private void updatePlayerStatus(Board board, String player){
    boolean kingInSafe = isKingInSafe(board, player);
    if(player.equals("WHITE")){
      this.whiteCheck = !kingInSafe;
    }else{
      this.blackCheck = !kingInSafe;
    }

    List<Figure> figures = board.getFigures();
    for(Figure figure : figures) {
      if (figure.getPlayer().equals(player)) {
        List<Cell> cells = figure.getPossibleCells(board);
        for(Cell cell : cells){
          FigureMove move = new FigureMove(board.getCell(figure.getCell()), cell);
          if(isKingInSafeAfterMove(board, player, move)){
            return;
          }
        }
      }
    }

    setLooser(player);
  }

  public boolean isKingInSafeAfterMove(Board board, String player, FigureMove move){
    Figure movedFigure = null;
    Figure replacedFigure = null;
    if(move != null){
      movedFigure = move.getFrom().getFigure();
      replacedFigure = move.getTo().getFigure();
      if(movedFigure != null){
        movedFigure.setCell(move.getTo().getName());
        move.getFrom().setFigure(null);
        move.getTo().setFigure(movedFigure);
      }
    }
    boolean answer = isKingInSafe(board, player);
    if(move != null){
      move.getTo().setFigure(replacedFigure);
      if(movedFigure != null){
        movedFigure.setCell(move.getFrom().getName());
        move.getFrom().setFigure(movedFigure);
      }
    }
    return answer;
  }

  public boolean isKingInSafe(Board board, String player){
    Figure king = board.getKingFigure(player);
    if(king == null){
      return true;
    }

    Cell kingCell = board.getCell(king.getCell());
    List<Figure> figures = board.getFigures();
    for(Figure figure : figures){
      if(!figure.getPlayer().equals(player)){
        if(figure.getPossibleCells(board).contains(kingCell)){
          return false;
        }
      }
    }
    return true;
  }

  private void setLooser(String looser){
    if(looser.equals("WHITE")){
      setWinner("BLACK");
    }else if(looser.equals("BLACK")){
      setWinner("WHITE");
    }else{
      setWinner("UNKNOWN");
    }
  }

  public boolean isStarted() {
    return isStarted;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public String getWinner() {
    return winner;
  }

  public boolean isWhiteCheck() {
    return whiteCheck;
  }

  public boolean isBlackCheck() {
    return blackCheck;
  }

  public String getActivePlayer() {
    return activePlayer;
  }

  public List<Figure> getFigures() {
    return figures;
  }

  public void setStarted(boolean started) {
    isStarted = started;
  }

  public void setWinner(String winner) {
    this.winner = winner;
    this.isFinished = !this.winner.equals("UNKNOWN");
  }

  public void setWhiteCheck(boolean whiteCheck) {
    this.whiteCheck = whiteCheck;
  }

  public void setBlackCheck(boolean blackCheck) {
    this.blackCheck = blackCheck;
  }

  public void setActivePlayer(String activePlayer) {
    this.activePlayer = activePlayer;
  }

  public void setFigures(List<Figure> figures) {
    this.figures = figures;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(LocalDateTime finishTime) {
    this.finishTime = finishTime;
  }

  public LocalDateTime getLastMoveTime() {
    return lastMoveTime;
  }

  public void setLastMoveTime(LocalDateTime lastMoveTime) {
    this.lastMoveTime = lastMoveTime;
  }

  public LocalDateTime getCurrentTime() {
    this.currentTime = LocalDateTime.now();
    return currentTime;
  }
}
