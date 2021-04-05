package com.msmir.entity.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Figure {

  public Knight() {
  }

  public Knight(String player, String cell) {
    super(player, "knight", cell);
  }

  @Override
  public List<List<Cell>> getPossibleCellLines(Board board) {
    List<List<Cell>> possibleCellLines = new ArrayList<>();

    Cell cell = board.getCell(getCell());
    if(cell != null){
      for(int i = 0; i < 6; i++){
        Cell endCell1 = getCellByPath(cell, List.of(i, i, (i+6-1)%6));
        if(isMovable(endCell1)){
          possibleCellLines.add(List.of(endCell1));
        }
        Cell endCell2 = getCellByPath(cell, List.of(i, i, (i+6+1)%6));
        if(isMovable(endCell2)){
          possibleCellLines.add(List.of(endCell2));
        }
      }
    }
    return possibleCellLines;
  }
}
