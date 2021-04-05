package com.msmir.vm;

import com.msmir.containers.game.board.BoardPosition;
import com.msmir.containers.game.board.Cell;
import java.util.List;
import java.util.stream.Collectors;

public class CellVm {
  private final String name;
  private final List<String> sides;
  private final List<String> diags;
  private final BoardPosition pos;
  
  public CellVm(Cell cell) {
    this.name = cell.getName();
    this.sides = cell.getSides().stream().map(this::takeName).collect(Collectors.toList());
    this.diags = cell.getDiags().stream().map(this::takeName).collect(Collectors.toList());
    this.pos = cell.getPos();
  }

  public String takeName(Cell cell){
    if(cell == null){
      return "";
    }else{
      return cell.getName();
    }
  }

  public String getName() {
    return name;
  }

  public List<String> getSides() {
    return sides;
  }

  public List<String> getDiags() {
    return diags;
  }

  public BoardPosition getPos() {
    return pos;
  }
}
