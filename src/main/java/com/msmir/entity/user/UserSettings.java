package com.msmir.entity.user;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Embeddable;

@Embeddable
public class UserSettings implements Serializable {
  private static final Set<String> BOARD_STYLES = Set.of("COLORFUL", "BEIGE");
  private static final Set<String> FIGURE_STYLES = Set.of("GEOMETRY", "CLASSIC");

  private String boardStyle;
  private String figureStyle;

  public UserSettings() {
    this.boardStyle = "COLORFUL";
    this.figureStyle = "GEOMETRY";
  }

  public boolean isValid(){
    return BOARD_STYLES.contains(this.boardStyle) &&
        FIGURE_STYLES.contains(this.figureStyle);
  }

  public String getBoardStyle() {
    return boardStyle;
  }

  public void setBoardStyle(String boardStyle) {
    this.boardStyle = boardStyle;
  }

  public String getFigureStyle() {
    return figureStyle;
  }

  public void setFigureStyle(String figureStyle) {
    this.figureStyle = figureStyle;
  }
}
