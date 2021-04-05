package com.msmir.containers.replay;

import com.msmir.containers.game.board.BoardGenerator;
import com.msmir.containers.game.util.Arrangement;
import com.msmir.entity.GameReplay;
import com.msmir.entity.figures.Figure;
import com.msmir.vm.CellVm;
import java.util.List;
import java.util.Map;

public class GameReplayFull {
  private GameReplay replay;
  private List<Figure> figures;
  private Map<String, CellVm> cells;

  public GameReplayFull() {
  }

  public GameReplayFull(GameReplay replay){
    this.replay = replay;
    this.figures = new Arrangement().generate(replay.getSettings());
    this.cells = new BoardGenerator(replay.getSettings().getBoardSize()).generate().getCellsVm();
  }

  public GameReplay getReplay() {
    return replay;
  }

  public void setReplay(GameReplay replay) {
    this.replay = replay;
  }

  public List<Figure> getFigures() {
    return figures;
  }

  public void setFigures(List<Figure> figures) {
    this.figures = figures;
  }

  public Map<String, CellVm> getCells() {
    return cells;
  }

  public void setCells(Map<String, CellVm> cells) {
    this.cells = cells;
  }
}
