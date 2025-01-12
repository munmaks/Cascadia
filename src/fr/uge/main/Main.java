package fr.uge.main;


import fr.uge.controler.CascadiaControler;
import com.github.forax.zen.Application;
import java.awt.Color;

public class Main {
  public static void main(String[] args) {
    // var _ = new MainMenu(Constants.VERSION_SQUARE); // square terminal version
    Application.run(Color.WHITE, CascadiaControler::cascadiaSquareGame);
  }

}
