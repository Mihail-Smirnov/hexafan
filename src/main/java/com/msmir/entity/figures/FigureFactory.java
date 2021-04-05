package com.msmir.entity.figures;

public class FigureFactory {
  public Figure makeFigure(String player, String type, String cell){
    switch (type) {
      case "pawn":
        return new Pawn(player, cell);
      case "king":
        return new King(player, cell);
      case "rook":
        return new Rook(player, cell);
      case "bishop":
        return new Bishop(player, cell);
      case "knight":
        return new Knight(player, cell);
      case "queen":
        return new Queen(player, cell);
      default:
        return null;
    }
  }
}
