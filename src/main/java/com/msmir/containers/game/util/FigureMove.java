package com.msmir.containers.game.util;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.Cell;
import com.msmir.vm.FigureMoveVm;

public class FigureMove {
  private Cell from;
  private Cell to;

  public FigureMove() {
  }

  public FigureMove(Cell from, Cell to) {
    this.from = from;
    this.to = to;
  }

  public FigureMove(FigureMoveVm moveVm, Board board){
    this.from = board.getCell(moveVm.getFrom());
    this.to = board.getCell(moveVm.getTo());
  }

  public Cell getFrom() {
    return from;
  }

  public Cell getTo() {
    return to;
  }
}
