package com.msmir.containers.game.util;

import com.msmir.containers.game.GameSettings;
import com.msmir.entity.figures.Figure;
import com.msmir.entity.figures.FigureFactory;
import java.util.ArrayList;
import java.util.List;

public class Arrangement {

  public List<Figure> generate(GameSettings settings){
    if(settings.getArrangement().equals("NORMAL")){
      if(settings.getBoardSize() == 4){
        return generateNormal4();
      }else if(settings.getBoardSize() == 6){
        return generateNormal6();
      }else if(settings.getBoardSize() == 8){
        return generateNormal8();
      }
    }else if(settings.getArrangement().equals("WIDE")){
      if(settings.getBoardSize() == 4){
        return generateWide4();
      }else if(settings.getBoardSize() == 6){
        return generateWide6();
      }else if(settings.getBoardSize() == 8){
        return generateWide8();
      }
    }
    return null;
  }

  private List<Figure> generateNormal4(){
    String[][] whiteFigures = {
        {"king", "A30"}, {"rook", "E31"}, {"knight", "A31"},
        {"pawn", "A33"}, {"pawn", "A32"}, {"pawn", "A21"}, {"pawn", "A20"},
        {"pawn", "E21"}, {"pawn", "E32"}, {"pawn", "E33"}
    };
    String[][] blackFigures = {
        {"king", "C33"}, {"rook", "C23"}, {"knight", "C32"},
        {"pawn", "A03"}, {"pawn", "C13"}, {"pawn", "C12"}, {"pawn", "C22"},
        {"pawn", "C21"}, {"pawn", "C31"}, {"pawn", "C30"}
    };
    return generateFromString(whiteFigures, blackFigures);
  }

  private List<Figure> generateNormal6(){
    String[][] whiteFigures = {
        {"king", "A50"}, {"queen", "E51"}, {"rook", "A52"}, {"rook", "E52"}, {"bishop", "A51"},
        {"bishop", "A40"}, {"bishop", "E53"}, {"knight", "A53"}, {"knight", "A41"},
        {"knight", "E41"}, {"pawn", "A55"}, {"pawn", "A54"}, {"pawn", "A43"},
        {"pawn", "A42"}, {"pawn", "A31"}, {"pawn", "A30"}, {"pawn", "E31"},
        {"pawn", "E42"}, {"pawn", "E43"}, {"pawn", "E54"}, {"pawn", "E55"}
    };
    String[][] blackFigures = {
        {"king", "C55"}, {"queen", "C45"}, {"rook", "C35"}, {"rook", "C53"}, {"bishop", "C54"},
        {"bishop", "C44"}, {"bishop", "C25"}, {"knight", "C34"}, {"knight", "C43"},
        {"knight", "C52"}, {"pawn", "A05"}, {"pawn", "C15"}, {"pawn", "C14"},
        {"pawn", "C24"}, {"pawn", "C23"}, {"pawn", "C33"}, {"pawn", "C32"},
        {"pawn", "C42"}, {"pawn", "C41"}, {"pawn", "C51"}, {"pawn", "C50"}
    };
    return generateFromString(whiteFigures, blackFigures);
  }

  private List<Figure> generateNormal8(){
    String[][] whiteFigures = {
        {"king", "A70"}, {"queen", "A71"}, {"queen", "E71"}, {"rook", "A72"},
        {"rook", "A73"}, {"rook", "A74"}, {"rook", "E72"}, {"rook", "E73"},
        {"rook", "E74"}, {"bishop", "A61"}, {"bishop", "A60"}, {"bishop", "E61"},
        {"bishop", "A50"}, {"bishop", "E75"}, {"bishop", "E62"}, {"knight", "A62"},
        {"knight", "A75"}, {"knight", "A63"}, {"knight", "A51"}, {"knight", "E51"},
        {"knight", "E63"}, {"pawn", "A77"}, {"pawn", "A76"}, {"pawn", "A65"},
        {"pawn", "A64"}, {"pawn", "A53"}, {"pawn", "A52"}, {"pawn", "A41"},
        {"pawn", "A40"}, {"pawn", "E41"}, {"pawn", "E52"}, {"pawn", "E53"},
        {"pawn", "E64"}, {"pawn", "E65"}, {"pawn", "E76"}, {"pawn", "E77"}
    };
    String[][] blackFigures = {
        {"king", "C77"}, {"queen", "C67"}, {"queen", "C76"}, {"rook", "C57"},
        {"rook", "C47"}, {"rook", "C37"}, {"rook", "C75"}, {"rook", "C74"},
        {"rook", "C73"}, {"bishop", "C56"}, {"bishop", "C66"}, {"bishop", "C65"},
        {"bishop", "C55"}, {"bishop", "C27"}, {"bishop", "C46"}, {"knight", "C36"},
        {"knight", "C45"}, {"knight", "C54"}, {"knight", "C64"}, {"knight", "C63"},
        {"knight", "C72"}, {"pawn", "A07"}, {"pawn", "C17"}, {"pawn", "C16"},
        {"pawn", "C26"}, {"pawn", "C25"}, {"pawn", "C35"}, {"pawn", "C34"},
        {"pawn", "C44"}, {"pawn", "C43"}, {"pawn", "C53"}, {"pawn", "C52"},
        {"pawn", "C62"}, {"pawn", "C61"}, {"pawn", "C71"}, {"pawn", "C70"}
    };
    return generateFromString(whiteFigures, blackFigures);
  }

  private List<Figure> generateWide4(){
    String[][] whiteFigures = {
        {"king", "E31"}, {"rook", "A31"}, {"knight", "A30"},
        {"pawn", "A32"}, {"pawn", "A21"}, {"pawn", "A10"}, {"pawn", "E21"},
        {"pawn", "E32"}
    };
    String[][] blackFigures = {
        {"king", "C23"}, {"rook", "C32"}, {"knight", "C33"},
        {"pawn", "C13"}, {"pawn", "C12"}, {"pawn", "C11"}, {"pawn", "C21"},
        {"pawn", "C31"}
    };
    return generateFromString(whiteFigures, blackFigures);
  }

  private List<Figure> generateWide6(){
    String[][] whiteFigures = {
        {"king", "E51"}, {"queen", "A51"}, {"rook", "A53"}, {"rook", "E53"}, {"bishop", "A50"},
        {"bishop", "A40"}, {"bishop", "A30"}, {"knight", "A52"}, {"knight", "E52"},
        {"pawn", "A54"}, {"pawn", "A43"}, {"pawn", "A32"}, {"pawn", "A21"},
        {"pawn", "A10"}, {"pawn", "E21"}, {"pawn", "E32"}, {"pawn", "E43"},
        {"pawn", "E54"}
    };
    String[][] blackFigures = {
        {"king", "C45"}, {"queen", "C54"}, {"rook", "C25"}, {"rook", "C52"}, {"bishop", "C55"},
        {"bishop", "C44"}, {"bishop", "C33"}, {"knight", "C35"}, {"knight", "C53"},
        {"pawn", "C15"}, {"pawn", "C14"}, {"pawn", "C13"}, {"pawn", "C12"},
        {"pawn", "C11"}, {"pawn", "C21"}, {"pawn", "C31"}, {"pawn", "C41"},
        {"pawn", "C51"}
    };
    return generateFromString(whiteFigures, blackFigures);
  }

  private List<Figure> generateWide8(){
    String[][] whiteFigures = {
        {"king", "E71"}, {"queen", "A71"}, {"rook", "A74"}, {"rook", "A75"},
        {"rook", "E74"}, {"rook", "E75"}, {"bishop", "A70"}, {"bishop", "A60"},
        {"bishop", "A50"}, {"bishop", "A40"}, {"bishop", "A30"}, {"bishop", "A20"},
        {"knight", "A72"}, {"knight", "A73"}, {"knight", "E72"}, {"knight", "E73"},
        {"pawn", "A76"}, {"pawn", "A65"}, {"pawn", "A54"}, {"pawn", "A43"},
        {"pawn", "A32"}, {"pawn", "A21"}, {"pawn", "A10"}, {"pawn", "E21"},
        {"pawn", "E32"}, {"pawn", "E43"}, {"pawn", "E54"}, {"pawn", "E65"},
        {"pawn", "E76"}
    };
    String[][] blackFigures = {
        {"king", "C67"}, {"queen", "C76"}, {"rook", "C27"}, {"rook", "C37"},
        {"rook", "C72"}, {"rook", "C73"}, {"bishop", "C77"}, {"bishop", "C66"},
        {"bishop", "C55"}, {"bishop", "C44"}, {"bishop", "C33"}, {"bishop", "C22"},
        {"knight", "C47"}, {"knight", "C57"}, {"knight", "C74"}, {"knight", "C75"},
        {"pawn", "C17"}, {"pawn", "C16"}, {"pawn", "C15"}, {"pawn", "C14"},
        {"pawn", "C13"}, {"pawn", "C12"}, {"pawn", "C11"}, {"pawn", "C21"},
        {"pawn", "C31"}, {"pawn", "C41"}, {"pawn", "C51"}, {"pawn", "C61"},
        {"pawn", "C71"}
    };
    return generateFromString(whiteFigures, blackFigures);
  }

  private List<Figure> generateFromString(String[][] whiteFigures, String[][] blackFigures){
    List<Figure> figures = new ArrayList<>();
    FigureFactory figureFactory = new FigureFactory();
    for (String[] whiteFigure : whiteFigures) {
      figures.add(figureFactory.makeFigure("WHITE", whiteFigure[0], whiteFigure[1]));
    }
    for (String[] blackFigure : blackFigures) {
      figures.add(figureFactory.makeFigure("BLACK", blackFigure[0], blackFigure[1]));
    }
    return figures;
  }
}
