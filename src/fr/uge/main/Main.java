package fr.uge.main;


import java.awt.Color;

import fr.uge.ui.CascadiaController;
// import fr.uge.ui.GraphicSquare;
// import fr.uge.ui.MainMenu;
// import fr.uge.util.Constants;

import com.github.forax.zen.Application;

public class Main {
  public static void main(String[] args) {
    // var _ = new MainMenu(Constants.VERSION_SQUARE);
    // Application.run(Color.WHITE, GraphicSquare::cascadiaSquareGame);
    Application.run(Color.WHITE, CascadiaController::cascadiaSquareGame);
  }

}
