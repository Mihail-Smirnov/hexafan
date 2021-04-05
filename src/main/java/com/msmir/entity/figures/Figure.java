package com.msmir.entity.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import com.msmir.containers.game.util.FigureMove;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Figure {
  private String player;
  private String type;
  private String cell;

  public Figure() {
  }

  public Figure(String player, String type, String cell) {
    this.player = player;
    this.type = type;
    this.cell = cell;
  }

  public abstract List<List<Cell>> getPossibleCellLines(Board board);

  public List<List<Cell>> getAttackCellLines(Board board){
    return getPossibleCellLines(board);
  }

  public List<List<Cell>> getMoveCellLines(Board board){
    return getPossibleCellLines(board);
  }

  public List<Cell> getPossibleCells(Board board){
    return getPossibleCellLines(board).stream().flatMap(Collection::stream).collect(Collectors.toList());
  }

  public boolean doMove(Board board, FigureMove move){
    if(canMove(board, move)){
      Cell cell = board.getCell(getCell());
      if(cell == null){
        return false;
      }
      cell.setFigure(null);
      move.getTo().setFigure(this);
      setCell(move.getTo().getName());
      return true;
    }else{
      return false;
    }
  }

  public boolean canMove(Board board, FigureMove move){
    Cell cell = board.getCell(getCell());
    if(cell == null || cell != move.getFrom()){
      return false;
    }
    return getPossibleCells(board).contains(move.getTo());
  }

  public String getPlayer() {
    return player;
  }

  public void setPlayer(String player) {
    this.player = player;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCell() {
    return cell;
  }

  public void setCell(String cell) {
    this.cell = cell;
  }

  protected boolean isObstacle(Cell cell){
    return cell == null || cell.getFigure() != null;
  }

  protected boolean isEnemy(Cell cell){
    return cell != null && cell.getFigure() != null && !cell.getFigure().getPlayer().equals(getPlayer());
  }

  protected boolean isMovable(Cell cell){
    return cell != null && (cell.getFigure() == null || !cell.getFigure().getPlayer().equals(getPlayer()));
  }

  protected List<Cell> untilEnemyOrObstacle(Cell beginCell,
      Function<Cell, Cell> nextCell){
    List<Cell> line = new ArrayList<>();
    if(beginCell == null){
      return line;
    }

    Cell curCell = nextCell.apply(beginCell);
    while(!isObstacle(curCell)){
      line.add(curCell);
      curCell = nextCell.apply(curCell);
    }
    if(isEnemy(curCell)) {
      line.add(curCell);
    }
    return line;
  }

  protected Cell getCellByPath(Cell beginCell, List<Integer> path){
    Cell curCell = beginCell;
    for (int direction : path){
      if(direction >= 0 && direction < 6){
        curCell = curCell.getSides().get(direction);
      }
      if(curCell == null) {
        return null;
      }
    }
    return curCell;
  }
}
