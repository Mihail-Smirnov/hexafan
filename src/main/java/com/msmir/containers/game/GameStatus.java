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
    this.whiteCheck = !game.getBoard().isKingInSafe("WHITE");
    this.blackCheck = !game.getBoard().isKingInSafe("BLACK");

    if(!this.isFinished){
      setWinner(calcWinner(game.getBoard()));
    }
    if(this.isFinished){
      game.close(this.winner);
    }
  }

  private String calcWinner(Board board){
    if(this.whiteCheck){
      if(isMate(board, "WHITE")){
        return "BLACK";
      }
    }else if(this.blackCheck){
      if(isMate(board, "BLACK")){
        return "WHITE";
      }
    }
    return "UNKNOWN";
  }

  private boolean isMate(Board board, String player){
    Figure king = board.getKingFigure(player);
    if(king == null){
      return false;
    }

    Cell kingCell = board.getCell(king.getCell());
    List<Figure> figures = board.getFigures();
    List<Set<Cell>> dangerLines = new ArrayList<>();
    Cell enemyCell = null;
    for(Figure figure : figures){
      if(!figure.getPlayer().equals(player)){
        List<List<Cell>> cellLines = figure.getPossibleCellLines(board);
        for(List<Cell> cellLine : cellLines){
          if(cellLine.contains(kingCell)){
            dangerLines.add(new HashSet<>(cellLine));
            enemyCell = board.getCell(figure.getCell());
          }
        }
      }
    }

    if(dangerLines.isEmpty()){
      return false;
    }

    Set<Cell> safeCell = dangerLines.get(0);
    for(int i = 1; i < dangerLines.size(); i++){
      safeCell.retainAll(dangerLines.get(i));
    }

    for(Figure figure : figures) {
      if (figure.getPlayer().equals(player) && figure != king) {
        List<Cell> cells = figure.getPossibleCells(board);
        for(Cell cell : cells){
          if(safeCell.contains(cell) || (safeCell.size() == 1 && safeCell.contains(enemyCell))){
            return false;
          }
        }
      }
    }
    List<Cell> possibleCells = king.getPossibleCells(board);
    for(Cell cell : possibleCells){
      if(board.isKingInSafeAfterMove(player, new FigureMove(kingCell, cell))){
        return false;
      }
    }

    return true;
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
