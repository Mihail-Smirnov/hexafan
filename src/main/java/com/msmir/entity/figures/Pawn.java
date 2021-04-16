package com.msmir.entity.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import com.msmir.containers.game.util.FigureMove;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Figure {
  private boolean firstMove;

  public Pawn() {
    firstMove = true;
  }

  public Pawn(String player, String cell) {
    super(player, "pawn", cell);
    firstMove = true;
  }

  @Override
  public List<List<Cell>> getPossibleCellLines(Board board) {
    List<List<Cell>> possibleCellLines = new ArrayList<>();
    possibleCellLines.addAll(getAttackCellLines(board));
    possibleCellLines.addAll(getMoveCellLines(board));
    return possibleCellLines;
  }

  @Override
  public List<List<Cell>> getAttackCellLines(Board board) {
    List<List<Cell>> attackCellLines = new ArrayList<>();
    Cell cell = board.getCell(getCell());
    if(cell != null) {
      Cell diag1 = cell.getDiags().get(1+getRotation());
      if (isEnemy(diag1)) {
        attackCellLines.add(List.of(diag1));
      }
      Cell diag2 = cell.getDiags().get(2+getRotation());
      if (isEnemy(diag2)) {
        attackCellLines.add(List.of(diag2));
      }
    }
    return attackCellLines;
  }

  @Override
  public List<List<Cell>> getMoveCellLines(Board board) {
    List<List<Cell>> moveCellLines = new ArrayList<>();
    Cell cell = board.getCell(getCell());
    if(cell != null){
      int side = 1+getRotation();
      Cell forwardCell = cell.getSides().get(side);
      if(forwardCell != null && !isObstacle(forwardCell)){
        if(isFirstMove() && !isObstacle(forwardCell.getSides().get(side))){
          moveCellLines.add(List.of(forwardCell, forwardCell.getSides().get(side)));
        }else{
          moveCellLines.add(List.of(forwardCell));
        }
      }
    }
    return moveCellLines;
  }

  @Override
  public boolean doMove(Board board, FigureMove move) {
    boolean didMove = super.doMove(board, move);
    if(didMove) {
      firstMove = false;
    }
    return didMove;
  }

  public boolean isFirstMove() {
    return firstMove;
  }

  public void setFirstMove(boolean firstMove) {
    this.firstMove = firstMove;
  }

  private int getRotation(){
    return getPlayer().equals("WHITE") ? 0 : 3;
  }
}
