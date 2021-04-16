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
public class QueenTest {

  private Board board;

  @BeforeAll
  void init(){
    this.board = new BoardGenerator(6).generate();
  }

  @Test
  void centerMoves(){
    Figure queen = new FigureFactory().makeFigure("WHITE", "queen", "A00");
    this.board.setFigures(List.of(queen));
    List<String> cells = queen.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C10","C20","C30","C40","C50","E11","E22","E33",
        "E44","E55","A10","A20","A30","A40","A50","A11","A22","A33","A44","A55","A01","A02","A03",
        "A04","A05","C11","C22","C33","C44","C55","E12","E24","E21","E42","A21","A42","A12","A24",
        "C12","C24","C21","C42")));
    Assertions.assertEquals(42, cells.size());
  }

  @Test
  void borderMoves(){
    Figure queen = new FigureFactory().makeFigure("WHITE", "queen", "C55");
    this.board.setFigures(List.of(queen));
    List<String> cells = queen.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C44","C33","C22","C11","A00","A10","A20","A30",
        "A40","A50","C54","C53","C52","C51","C50","C45","C35","C25","C15","A05","C34","C13","A13",
        "A34","A55","C43","C31","E13","E34","E55")));
    Assertions.assertEquals(30, cells.size());
  }

  @Test
  void enemyAndFriends(){
    FigureFactory figureFactory = new FigureFactory();
    Figure queen = figureFactory.makeFigure("WHITE", "queen", "A00");
    Figure whiteFigure = figureFactory.makeFigure("WHITE", "pawn", "A02");
    Figure blackFigure = figureFactory.makeFigure("BLACK", "pawn", "C20");
    this.board.setFigures(List.of(queen, whiteFigure, blackFigure));
    List<String> cells = queen.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertFalse(cells.contains("C30"));
    Assertions.assertFalse(cells.contains("A02"));
    Assertions.assertTrue(cells.containsAll(List.of("C10","C20","A01")));
  }
}
