package game;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameSettings;
import com.msmir.containers.game.GameStatus;
import com.msmir.entity.figures.Figure;
import com.msmir.entity.figures.FigureFactory;
import com.msmir.entity.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameStatusTest {

  @Test
  void check() {
    Game game = getGame();
    game.getBoard().setFigures(getFigures(List.of("WHITE", "king", "A00",
        "BLACK", "rook", "C55")));
    GameStatus status = new GameStatus();
    status.update(game);
    Assertions.assertTrue(status.isWhiteCheck());
  }

  @Test
  void mateWithoutCheck(){
    Game game = getGame();
    game.getBoard().setFigures(getFigures(List.of("WHITE", "king", "C55",
        "BLACK", "king", "C33")));
    GameStatus status = new GameStatus();
    status.update(game);
    Assertions.assertFalse(status.isWhiteCheck());
    Assertions.assertEquals("BLACK", status.getWinner());
  }

  @Test
  void figuresCanMove(){
    Game game = getGame();
    game.getBoard().setFigures(getFigures(List.of("WHITE", "king", "C55",
        "BLACK", "king", "C33", "WHITE", "pawn", "A50")));
    GameStatus status = new GameStatus();
    status.update(game);
    Assertions.assertFalse(status.isWhiteCheck());
    Assertions.assertEquals("UNKNOWN", status.getWinner());
  }

  @Test
  void blockingMate(){
    Game game = getGame();
    game.getBoard().setFigures(getFigures(List.of("WHITE", "king", "A05",
        "BLACK", "queen", "A03", "WHITE", "knight", "A35")));
    GameStatus status = new GameStatus();
    status.update(game);
    Assertions.assertTrue(status.isWhiteCheck());
    Assertions.assertEquals("UNKNOWN", status.getWinner());
  }

  @Test
  void whiteMate() {
    Game game = getGame();
    game.getBoard().setFigures(getFigures(List.of("WHITE", "king", "A05",
        "BLACK", "queen", "A03")));
    GameStatus status = new GameStatus();
    status.update(game);
    Assertions.assertTrue(status.isWhiteCheck());
    Assertions.assertEquals("BLACK", status.getWinner());
  }

  @Test
  void blackMate(){
    Game game = getGame();
    game.getBoard().setFigures(getFigures(List.of("BLACK", "king", "A05",
        "WHITE", "queen", "A03")));
    GameStatus status = new GameStatus();
    status.update(game);
    Assertions.assertTrue(status.isBlackCheck());
    Assertions.assertEquals("WHITE", status.getWinner());
  }

  private Game getGame() {
    return new Game(UUID.randomUUID(), new GameSettings(), new User(), new User(), g -> {});
  }

  private List<Figure> getFigures(List<String> figureStrings) {
    List<Figure> figures = new ArrayList<>();
    FigureFactory figureFactory = new FigureFactory();
    for (int i = 0; i < figureStrings.size(); i += 3) {
      figures.add(figureFactory.makeFigure(figureStrings.get(i),
          figureStrings.get(i + 1), figureStrings.get(i + 2)));
    }
    return figures;
  }
}
