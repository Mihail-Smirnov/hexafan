package com.msmir.entity.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Figure {

  public Rook() {
  }

  public Rook(String player, String cell) {
    super(player, "rook", cell);
  }

  @Override
  public List<List<Cell>> getPossibleCellLines(Board board) {
    List<List<Cell>> possibleCellLines = new ArrayList<>();
    Cell cell = board.getCell(getCell());
    if(cell != null){
      for(int i = 0; i < 6; i++){
        int finalI = i;
        possibleCellLines.add(untilEnemyOrObstacle(cell, (curCell) -> curCell.getSides().get(finalI)));
      }
    }
    return possibleCellLines;
  }
}
