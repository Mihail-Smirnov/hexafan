package game.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.BoardGenerator;
import com.msmir.containers.game.board.Cell;
import com.msmir.entity.figures.Figure;
import com.msmir.entity.figures.FigureFactory;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class BishopTest {

  private Board board;

  @BeforeAll
  void init(){
    this.board = new BoardGenerator(6).generate();
  }

  @Test
  void centerMoves(){
    Figure bishop = new FigureFactory().makeFigure("WHITE", "bishop", "A00");
    this.board.setFigures(List.of(bishop));
    List<String> cells = bishop.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("E12","E24","E21","E42","A21","A42","A12","A24",
        "C12","C24","C21","C42")));
    Assertions.assertEquals(12, cells.size());
  }

  @Test
  void borderMoves(){
    Figure bishop = new FigureFactory().makeFigure("WHITE", "bishop", "C55");
    this.board.setFigures(List.of(bishop));
    List<String> cells = bishop.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C34","C13","A13","A34","A55","C43","C31","E13",
        "E34","E55")));
    Assertions.assertEquals(10, cells.size());
  }

  @Test
  void enemyAndFriends(){
    FigureFactory figureFactory = new FigureFactory();
    Figure bishop = figureFactory.makeFigure("WHITE", "bishop", "A00");
    Figure whiteFigure = figureFactory.makeFigure("WHITE", "pawn", "A12");
    Figure blackFigure = figureFactory.makeFigure("BLACK", "pawn", "E12");
    this.board.setFigures(List.of(bishop, whiteFigure, blackFigure));
    List<String> cells = bishop.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertFalse(cells.contains("A12"));
    Assertions.assertFalse(cells.contains("E24"));
    Assertions.assertTrue(cells.contains("E12"));
  }
}
