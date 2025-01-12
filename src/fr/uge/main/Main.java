package fr.uge.main;


import fr.uge.ui.MainMenu;
import fr.uge.ui.GraphicSquare;
import fr.uge.util.Constants;
import java.awt.Color;

import com.github.forax.zen.Application;

public class Main {
  public static void main(String[] args) {
    // var _ = new MainMenu(Constants.VERSION_SQUARE);
    Application.run(Color.WHITE, GraphicSquare::cascadiaSquareGame);
  }

}
