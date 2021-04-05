package com.msmir.containers.game.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardGenerator {

  private Cell[][] cells;
  private final int n;

  public BoardGenerator(int n){
    this.n = n;
  }

  public Board generate(){
    Board board = new Board();

    cells = new Cell[4*n-3][2*n-1];
    generateE();
    generateC();
    generateA();
    Map<String, Cell> cellsMap = new HashMap<>();
    for(int y = 0; y < cells.length; y++){
      for(int x = 0; x < cells[y].length; x++){
        generateSides(x, y);
        generateDiags(x, y);
        generatePosition(x, y);
        if(cells[y][x] != null){
          cellsMap.put(cells[y][x].getName(), cells[y][x]);
        }
      }
    }

    board.setCells(cellsMap);
    return board;
  }

  private void generateE(){
    for(int i = 0; i < n; i++){
      for(int j = 0; j < n; j++){
        int x = n-1+j;
        int y = 2*n-2-j+i*2;
        cells[y][x] = new Cell("E"+i+j);
      }
    }
  }

  private void generateC(){
    for(int i = 0; i < n; i++){
      for(int j = 0; j < n; j++){
        int x = i+(n-j)-1;
        int y = (n-i-1)+(n-j-1);
        cells[y][x] = new Cell("C"+i+j);
      }
    }
  }

  private void generateA(){
    for(int i = 0; i < n; i++){
      for(int j = 0; j < n; j++){
        int x = n-1-j;
        int y = 2*n-2-j+i*2;
        cells[y][x] = new Cell("A"+i+j);
      }
    }
  }

  private void generateSides(int x, int y){
    if(cells[y][x] == null){
      return;
    }
    List<Cell> sides = new ArrayList<>();
    sides.add(getCell(x+1,y-1));
    sides.add(getCell(x,y-2));
    sides.add(getCell(x-1,y-1));
    sides.add(getCell(x-1,y+1));
    sides.add(getCell(x,y+2));
    sides.add(getCell(x+1,y+1));
    cells[y][x].setSides(sides);
  }

  private void generateDiags(int x, int y){
    if(cells[y][x] == null){
      return;
    }
    List<Cell> diags = new ArrayList<>();
    diags.add(getCell(x+2,y));
    diags.add(getCell(x+1,y-3));
    diags.add(getCell(x-1,y-3));
    diags.add(getCell(x-2,y));
    diags.add(getCell(x-1,y+3));
    diags.add(getCell(x+1,y+3));
    cells[y][x].setDiags(diags);
  }

  private void generatePosition(int x, int y){
    if(cells[y][x] == null){
      return;
    }
    cells[y][x].setPos(new BoardPosition(x, y));
  }

  private Cell getCell(int x, int y){
    if(x < 0 || y < 0 || x >= 2*n-1 || y >= 4*n-3) {
      return null;
    } else {
      return cells[y][x];
    }
  }
}
