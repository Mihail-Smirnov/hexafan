package com.msmir.containers.game.board;

import com.msmir.entity.figures.Figure;
import java.util.List;
import java.util.Objects;

public class Cell {
  private String name;
  private List<Cell> sides;
  private List<Cell> diags;
  private Figure figure;
  private BoardPosition pos;

  public Cell() {
  }

  public Cell(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Cell> getSides() {
    return sides;
  }

  public void setSides(List<Cell> sides) {
    this.sides = sides;
  }

  public List<Cell> getDiags() {
    return diags;
  }

  public void setDiags(List<Cell> diags) {
    this.diags = diags;
  }

  public Figure getFigure() {
    return figure;
  }

  public void setFigure(Figure figure) {
    this.figure = figure;
  }

  public BoardPosition getPos() {
    return pos;
  }

  public void setPos(BoardPosition pos) {
    this.pos = pos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cell cell = (Cell) o;
    return Objects.equals(name, cell.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
