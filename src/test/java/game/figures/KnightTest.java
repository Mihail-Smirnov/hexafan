package game.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.BoardGenerator;
import com.msmir.containers.game.board.Cell;
import com.msmir.entity.figures.Figure;
import com.msmir.entity.figures.FigureFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class KnightTest {

  private Board board;

  @BeforeAll
  void init(){
    this.board = new BoardGenerator(6).generate();
  }

  @Test
  void centerMoves(){
    Figure knight = new FigureFactory().makeFigure("WHITE", "knight", "A00");
    this.board.setFigures(List.of(knight));
    List<String> cells = knight.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C32","C31","E13","E23","E32","E31","A31","A32",
        "A23","A13","C13","C23")));
    Assertions.assertEquals(12, cells.size());
  }

  @Test
  void borderMoves(){
    Figure knight = new FigureFactory().makeFigure("WHITE", "knight", "C55");
    this.board.setFigures(List.of(knight));
    List<String> cells = knight.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C42","C32","C23","C24")));
    Assertions.assertEquals(4, cells.size());
  }

  @Test
  void enemyAndFriends(){
    FigureFactory figureFactory = new FigureFactory();
    Figure knight = figureFactory.makeFigure("WHITE", "knight", "A00");
    Figure whiteFigure = figureFactory.makeFigure("WHITE", "pawn", "C22");
    Figure whiteFigure2 = figureFactory.makeFigure("WHITE", "pawn", "C12");
    Figure blackFigure = figureFactory.makeFigure("BLACK", "pawn", "C32");
    this.board.setFigures(List.of(knight, whiteFigure, whiteFigure2, blackFigure));
    List<String> cells = knight.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertFalse(cells.contains("C12"));
    Assertions.assertTrue(cells.contains("C32"));
  }
}
