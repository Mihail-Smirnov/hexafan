package com.msmir.vm;

import com.msmir.containers.game.util.FigureMove;

public class FigureMoveVm {
  private String from;
  private String to;

  public FigureMoveVm() {
  }

  public FigureMoveVm(FigureMove move){
    this.from = move.getFrom().getName();
    this.to = move.getTo().getName();
  }

  public FigureMoveVm(String from, String to) {
    this.from = from;
    this.to = to;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
