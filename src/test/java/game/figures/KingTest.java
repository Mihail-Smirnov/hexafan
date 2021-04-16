package game.figures;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.BoardGenerator;
import com.msmir.containers.game.board.Cell;
import com.msmir.entity.figures.Figure;
import com.msmir.entity.figures.FigureFactory;
import com.msmir.entity.figures.King;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.mapping.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class KingTest {

  private Board board;

  @BeforeAll
  void init(){
    this.board = new BoardGenerator(6).generate();
  }

  @Test
  void centerMoves(){
    Figure king = new FigureFactory().makeFigure("WHITE", "king", "A00");
    this.board.setFigures(List.of(king));
    List<String> cells = king.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C11","C10","E11","A10","A11","A01","C21",
        "E12","E21","A21","A12","C12")));
    Assertions.assertEquals(12, cells.size());
  }

  @Test
  void borderMoves(){
    Figure king = new FigureFactory().makeFigure("WHITE", "king", "C55");
    this.board.setFigures(List.of(king));
    List<String> cells = king.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertTrue(cells.containsAll(List.of("C45","C44","C54","C43","C34")));
    Assertions.assertEquals(5, cells.size());
  }

  @Test
  void enemyAndFriends(){
    FigureFactory figureFactory = new FigureFactory();
    Figure king = figureFactory.makeFigure("WHITE", "king", "A00");
    Figure whiteFigure = figureFactory.makeFigure("WHITE", "pawn", "C10");
    Figure blackFigure = figureFactory.makeFigure("BLACK", "pawn", "A01");
    this.board.setFigures(List.of(king, whiteFigure, blackFigure));
    List<String> cells = king.getPossibleCells(board).stream().map(Cell::getName).collect(
        Collectors.toList());
    Assertions.assertFalse(cells.contains("C10"));
    Assertions.assertTrue(cells.contains("A01"));
  }
}
