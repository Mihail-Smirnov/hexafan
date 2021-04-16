package game.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.BoardGenerator;
import com.msmir.containers.game.board.Cell;
import com.msmir.containers.game.util.FigureMove;
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
public class PawnTest {

  private Board board;

  @BeforeAll
  void init(){
    this.board = new BoardGenerator(6).generate();
  }

  @Test
  void centerMovesWhite(){
    Figure pawn = new FigureFactory().makeFigure("WHITE", "pawn", "A00");
    this.board.setFigures(List.of(pawn));
    List<String> cells = pawn.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C11", "C22")));
    Assertions.assertEquals(2, cells.size());
  }

  @Test
  void borderMovesWhite(){
    Figure pawn = new FigureFactory().makeFigure("WHITE", "pawn", "C55");
    this.board.setFigures(List.of(pawn));
    List<String> cells = pawn.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertEquals(0, cells.size());
  }

  @Test
  void centerMovesBlack(){
    Figure pawn = new FigureFactory().makeFigure("BLACK", "pawn", "A00");
    this.board.setFigures(List.of(pawn));
    List<String> cells = pawn.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("A10", "A20")));
    Assertions.assertEquals(2, cells.size());
  }

  @Test
  void borderMovesBlack(){
    Figure pawn = new FigureFactory().makeFigure("BLACK", "pawn", "C55");
    this.board.setFigures(List.of(pawn));
    List<String> cells = pawn.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C44", "C33")));
    Assertions.assertEquals(2, cells.size());
  }

  @Test
  void firstMove(){
    Figure pawn = new FigureFactory().makeFigure("WHITE", "pawn", "A00");
    this.board.setFigures(List.of(pawn));
    pawn.doMove(board, new FigureMove(board.getCell("A00"), board.getCell("C11")));
    List<String> cells = pawn.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.contains("C22"));
    Assertions.assertEquals(1, cells.size());
  }

  @Test
  void enemyAndFriends(){
    FigureFactory figureFactory = new FigureFactory();
    Figure pawn = figureFactory.makeFigure("WHITE", "pawn", "A00");
    Figure whiteFigure = figureFactory.makeFigure("WHITE", "pawn", "C11");
    Figure whiteFigure2 = figureFactory.makeFigure("WHITE", "pawn", "C21");
    Figure blackFigure = figureFactory.makeFigure("BLACK", "pawn", "C12");
    this.board.setFigures(List.of(pawn, whiteFigure, whiteFigure2, blackFigure));
    List<String> cells = pawn.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertFalse(cells.contains("C11"));
    Assertions.assertFalse(cells.contains("C21"));
    Assertions.assertTrue(cells.contains("C12"));
  }
}
