package fr.uge.core;

public final class TurnManager {
  private static int turns = 20;

  public void nextTurn() {
    if (turns == 0) {
      ; // Game Ends here, EndScreen is shown and score calculates for every player.
    }
    turns--;
  }
  
}
