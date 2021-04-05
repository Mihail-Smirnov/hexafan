package com.msmir.entity.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import java.util.ArrayList;
import java.util.List;

public class King extends Figure {
  public King() {
  }

  public King(String player, String cell) {
    super(player, "king", cell);
  }

  @Override
  public List<List<Cell>> getPossibleCellLines(Board board) {
    List<List<Cell>> possibleCellLines = new ArrayList<>();
    Cell cell = board.getCell(getCell());
    if(cell != null){
      for(int i = 0; i < 6; i++){
        Cell side = cell.getSides().get(i);
        if(isMovable(side)){
          possibleCellLines.add(List.of(side));
        }
        Cell diag = cell.getDiags().get(i);
        if(isMovable(diag)){
          possibleCellLines.add(List.of(diag));
        }
      }
    }
    return possibleCellLines;
  }
}
