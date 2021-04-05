package com.msmir.containers.game.board;

import com.msmir.containers.game.util.FigureMove;
import com.msmir.entity.figures.Figure;
import com.msmir.vm.CellVm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
  private Map<String, Cell> cells;

  public Board() {
  }

  public Map<String, Cell> getCells() {
    return cells;
  }

  public Map<String, CellVm> getCellsVm(){
    Map<String, CellVm> chessCellsVm = new HashMap<>();
    for(var entry : cells.entrySet()){
      chessCellsVm.put(entry.getKey(), new CellVm(entry.getValue()));
    }
    return chessCellsVm;
  }

  public void setCells(Map<String, Cell> cells) {
    this.cells = cells;
  }

  public Figure getKingFigure(String player){
    for(Cell cell : cells.values()){
      Figure figure = cell.getFigure();
      if(figure != null && figure.getType().equals("king") && figure.getPlayer().equals(player)){
        return figure;
      }
    }
    return null;
  }

  public List<Figure> getFigures(){
    List<Figure> figures = new ArrayList<>();
    for(Cell cell : cells.values()){
      if(cell.getFigure() != null){
        figures.add(cell.getFigure());
      }
    }
    return figures;
  }

  public void setFigures(List<Figure> figures){
    for(Cell cell : cells.values()){
      cell.setFigure(null);
    }
    for(Figure figure : figures){
      Cell cell = cells.get(figure.getCell());
      if(cell != null){
        cell.setFigure(figure);
      }
    }
  }

  public Cell getCell(String cell){
    return cells.get(cell);
  }

  public boolean isKingInSafeAfterMove(String player, FigureMove move){
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
    boolean answer = isKingInSafe(player);
    if(move != null){
      move.getTo().setFigure(replacedFigure);
      if(movedFigure != null){
        movedFigure.setCell(move.getFrom().getName());
        move.getFrom().setFigure(movedFigure);
      }
    }
    return answer;
  }

  public boolean isKingInSafe(String player){
    Figure king = getKingFigure(player);
    if(king == null){
      return true;
    }

    Cell kingCell = getCell(king.getCell());
    List<Figure> figures = getFigures();
    for(Figure figure : figures){
      if(!figure.getPlayer().equals(player)){
        if(figure.getPossibleCells(this).contains(kingCell)){
          return false;
        }
      }
    }
    return true;
  }
}
