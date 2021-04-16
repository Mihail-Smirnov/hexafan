package com.msmir.containers.game;

import com.msmir.containers.game.board.Board;
import com.msmir.containers.game.board.BoardGenerator;
import com.msmir.containers.game.board.Cell;
import com.msmir.containers.game.messages.PlayerMessage;
import com.msmir.containers.game.messages.SystemMessage;
import com.msmir.containers.game.util.Arrangement;
import com.msmir.containers.game.util.FigureMove;
import com.msmir.entity.figures.Figure;
import com.msmir.entity.user.User;
import com.msmir.vm.CellVm;
import com.msmir.vm.FigureMoveVm;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.function.Consumer;

public class Game {
  private final UUID id;
  private final GameSettings settings;
  private final Board board;
  private final GameStatus status;
  private final Timer moveTimer;
  private final Consumer<Game> onClose;

  private final User whitePlayer;
  private final User blackPlayer;
  private final List<SystemMessage> systemMessages;
  private final List<PlayerMessage> messages;
  private final List<FigureMoveVm> moves;

  public Game(UUID id, GameSettings settings, User whitePlayer,
      User blackPlayer, Consumer<Game> onClose) {
    this.id = id;
    this.settings = settings;
    this.board = new BoardGenerator(settings.getBoardSize()).generate();
    this.board.setFigures(new Arrangement().generate(settings));
    this.whitePlayer = whitePlayer;
    this.blackPlayer = blackPlayer;
    this.status = new GameStatus();
    this.status.setActivePlayer(settings.getFirstPlayer());
    this.status.setFigures(board.getFigures());
    this.systemMessages = new ArrayList<>();
    this.messages = new ArrayList<>();
    this.moves = new ArrayList<>();
    this.moveTimer = new Timer();
    this.onClose = onClose;
    start();
  }

  public void start(){
    status.setStarted(true);
    status.setStartTime(LocalDateTime.now());
    status.setLastMoveTime(LocalDateTime.now());

    moveTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        if(getStatus().isFinished()){
          moveTimer.cancel();
        }else{
          if(status.getLastMoveTime().plus(settings.getMoveDurationParsed()).isBefore(LocalDateTime.now())){
            if(status.getActivePlayer().equals("WHITE")){
              close("BLACK");
            }else{
              close("WHITE");
            }
          }
        }
      }
    }, 1000, 1000);
  }

  public void close(String winner){
    if(winner != null && !winner.equals("UNKNOWN")){
      status.setWinner(winner);
      status.setFinishTime(LocalDateTime.now());
      onClose.accept(this);
    }
  }

  public void doMove(FigureMoveVm moveVm, User user) {
    if (isActive() && isCorrectUser(status.getActivePlayer(), user)) {
      Cell cell = board.getCell(moveVm.getFrom());
      if (cell != null && cell.getFigure() != null) {
        FigureMove move = new FigureMove(moveVm, board);
        if (status.isKingInSafeAfterMove(board, status.getActivePlayer(), move)) {
          Figure figure = cell.getFigure();
          if (figure != null &&
              figure.getPlayer().equals(getActivePlayer()) &&
              cell.getFigure().doMove(board, move)) {
            switchPlayer();
            status.setLastMoveTime(LocalDateTime.now());
            status.update(this);
            moves.add(moveVm);
          }
        }
      }
    }
  }

  public boolean processMessage(PlayerMessage message, User user) {
    if (message != null && user != null) {
      message.setSender(user.getUsername());
      messages.add(message);
      return true;
    }
    return false;
  }

  public boolean processSystemMessage(SystemMessage message, User user){
    if (status.isStarted() && !status.isFinished()) {
      if (message != null && message.getSender() != null && user != null) {
        if(isCorrectUser(message.getSender(), user)) {
          systemMessages.add(message);
          switch (message.getType()) {
            case "LOSE":
              return processMessageLose(message);
            case "DRAW_OK":
              return processMessageDrawOK(message);
            case "DRAW":
            case "DRAW_FAIL":
              return true;
          }
        }
      }
    }
    return false;
  }

  private boolean processMessageDrawOK(SystemMessage message){
    SystemMessage offerMessage = null;
    if(message.getSender().equals("WHITE")){
      offerMessage = new SystemMessage("DRAW","BLACK",message.getGameId());
    }else if(message.getSender().equals("BLACK")){
      offerMessage = new SystemMessage("DRAW","WHITE",message.getGameId());
    }
    if(offerMessage != null){
      if(systemMessages.contains(offerMessage)){
        systemMessages.remove(offerMessage);
        close("DRAW");
        return true;
      }
    }
    return false;
  }

  private boolean processMessageLose(SystemMessage message){
    if (message.getSender().equals("WHITE")) {
      close("BLACK");
      return true;
    } else if (message.getSender().equals("BLACK")) {
      close("WHITE");
      return true;
    }else{
      return false;
    }
  }

  private boolean isCorrectUser(String userColor, User user){
    return (userColor.equals("WHITE") && user.getUsername().equals(whitePlayer.getUsername())) ||
        (userColor.equals("BLACK") && user.getUsername().equals(blackPlayer.getUsername()));
  }

  public GameStatus getStatus(){
    return status;
  }

  public Map<String, CellVm> getBoardCells(){
    return board.getCellsVm();
  }

  private void switchPlayer(){
    if(status.getActivePlayer().equals("WHITE")){
      status.setActivePlayer("BLACK");
    }else{
      status.setActivePlayer("WHITE");
    }
  }

  public boolean isActive(){
    return status.isStarted() && !status.isFinished();
  }

  public Board getBoard() {
    return board;
  }

  public String getActivePlayer() {
    return status.getActivePlayer();
  }

  public UUID getId() {
    return id;
  }

  public User getWhitePlayer() {
    return whitePlayer;
  }

  public User getBlackPlayer() {
    return blackPlayer;
  }

  public List<PlayerMessage> getMessages() {
    return messages;
  }

  public GameSettings getSettings() {
    return settings;
  }

  public List<FigureMoveVm> getMoves() {
    return moves;
  }
}
