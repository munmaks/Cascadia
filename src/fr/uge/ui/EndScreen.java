package fr.uge.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.github.forax.zen.Application;
import com.github.forax.zen.Event;
import com.github.forax.zen.PointerEvent;

public class EndScreen {

  private final List<String> playerNames;
  private final List<Integer> scores;
  public EndScreen(List<String> playerNames, List<Integer> scores) {
    this.playerNames = playerNames;
    this.scores = scores;
  }

  public void displayAllPlayers(Graphics2D graphics, int screenWidth, int screenHeight) {
    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, 0, screenWidth, screenHeight);
    graphics.setColor(new Color(50, 50, 50));
    graphics.fillRoundRect(screenWidth / 8, screenHeight / 8, screenWidth * 3 / 4, screenHeight * 3 / 4, 20, 20);
    graphics.setColor(Color.WHITE);
    graphics.setFont(graphics.getFont().deriveFont(36f));
    int yPosition = screenHeight / 4;
    for (int i = 0; i < playerNames.size(); i++) {
      String playerName = playerNames.get(i);
      int score = scores.get(i);
      graphics.setFont(graphics.getFont().deriveFont(36f));
      graphics.drawString("Joueur " + (i + 1) + ": " + playerName, screenWidth / 2 - screenWidth / 10, yPosition);
      graphics.drawString("Score : " + score, screenWidth / 2 - screenWidth / 10, yPosition + 40);
      yPosition += 100;
    }
    graphics.setFont(graphics.getFont().deriveFont(24f));
    graphics.drawString("Cliquez ici pour quitter", (int) (screenWidth / 2.5), screenHeight * 3 / 4 + 50);
    graphics.setColor(Color.RED);
    graphics.fillRoundRect(screenWidth / 2 - screenWidth / 8, screenHeight * 3 / 4 + 70, screenWidth / 4, screenHeight / 12, 10, 10);
    graphics.setColor(Color.WHITE);
    graphics.drawString("Quitter", screenWidth / 2 - screenWidth / 16, screenHeight * 3 / 4 + 105);
  }

  public void checkClickQuit(PointerEvent.Action action, PointerEvent.Location location, int screenWidth, int screenHeight) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= screenWidth / 2 - screenWidth / 8 && location.x() <= screenWidth / 2 - screenWidth / 8 + screenWidth / 4 &&
            location.y() >= screenHeight * 3 / 4 + 70 && location.y() <= screenHeight * 3 / 4 + 70 + screenHeight / 12) {
      System.out.println("Fin du jeu. Merci d'avoir joué !");
      System.exit(0);
    }
  }

  public void runEndMenu() {
    Application.run(Color.BLACK, context -> {
      int screenWidth = context.getScreenInfo().width();
      int screenHeight = context.getScreenInfo().height();
      System.out.println("Screen width: " + screenWidth);
      System.out.println("Screen height: " + screenHeight);
      context.renderFrame(graphics -> displayAllPlayers(graphics, screenWidth, screenHeight));
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          checkClickQuit(pointerEvent.action(), pointerEvent.location(), screenWidth, screenHeight);
        }
      }
    });
  }

  public static void main(String[] args) {
    EndScreen menu = new EndScreen(List.of("Alice", "Bob", "Charlie", "David"), List.of(1500, 1200, 1300, 1100));
    menu.runEndMenu();
  }
}
