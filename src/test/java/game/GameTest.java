package game;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameSettings;
import com.msmir.entity.figures.FigureFactory;
import com.msmir.entity.user.User;
import com.msmir.vm.FigureMoveVm;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {

  @Test
  void move(){
    GameSettings settings = new GameSettings();
    settings.setFirstPlayer("WHITE");
    User whitePlayer = new User();
    whitePlayer.setUsername("user1");
    User blackPlayer = new User();
    blackPlayer.setUsername("user2");
    Game game = new Game(UUID.randomUUID(), settings, whitePlayer, blackPlayer, g -> {});
    FigureFactory figureFactory = new FigureFactory();
    game.getBoard().setFigures(List.of(
        figureFactory.makeFigure("WHITE", "king", "C22"),
        figureFactory.makeFigure("BLACK", "king", "C55")
    ));

    game.doMove(new FigureMoveVm("C22", "C33"), blackPlayer);
    Assertions.assertEquals("WHITE", game.getActivePlayer());
    game.doMove(new FigureMoveVm("C55", "C44"), whitePlayer);
    Assertions.assertEquals("WHITE", game.getActivePlayer());
    game.doMove(new FigureMoveVm("A00", "A10"), whitePlayer);
    Assertions.assertEquals("WHITE", game.getActivePlayer());
    game.doMove(new FigureMoveVm("C22", "C44"), whitePlayer);
    Assertions.assertEquals("WHITE", game.getActivePlayer());
    game.doMove(new FigureMoveVm("C22", "C33"), whitePlayer);
    Assertions.assertEquals("BLACK", game.getActivePlayer());
    Assertions.assertEquals("WHITE", game.getStatus().getWinner());
    Assertions.assertFalse(game.isActive());
  }
}
