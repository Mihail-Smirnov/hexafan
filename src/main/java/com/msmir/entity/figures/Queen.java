package com.msmir.entity.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Figure {

  public Queen() {
  }

  public Queen(String player, String cell) {
    super(player, "queen", cell);
  }

  @Override
  public List<List<Cell>> getPossibleCellLines(Board board) {
    List<List<Cell>> possibleCellLines = new ArrayList<>();
    Cell cell = board.getCell(getCell());
    if(cell != null){
      for(int i = 0; i < 6; i++){
        int finalI = i;
        possibleCellLines.add(untilEnemyOrObstacle(cell, (curCell) -> curCell.getSides().get(finalI)));
        possibleCellLines.add(untilEnemyOrObstacle(cell, (curCell) -> curCell.getDiags().get(finalI)));
      }
    }
    return possibleCellLines;
  }
}
