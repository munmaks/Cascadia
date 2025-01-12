package fr.uge.data.core;


import fr.uge.data.util.Constants;

/**
 * Only one turn manager is created for the entire game.<br>
 * That's why we use static fields.
 */
public final class TurnManager {
  // private final List<Player> players;
  private int totalTurns = 1;
  private int currentPlayerIndex = 0;
  private int numberOfPlayers = 0;
  private boolean needToTurn = false;

  public TurnManager(int numberOfPlayers) {
    if (numberOfPlayers < 1 || numberOfPlayers > 4) {
      throw new IllegalArgumentException("Invalid number of players");
    }
    this.numberOfPlayers = numberOfPlayers;
  }

  /**
   * @return the number of players in the game
   */
  public final int getCurrentPlayerIndex() { return this.currentPlayerIndex; }

  /**
   * changes the current player to the next player in the list
   */
  public final void changePlayer() { this.currentPlayerIndex++; }

  /**
   * handles the end of a player's turn
   */
  public final void nextTurn() {
    if (this.currentPlayerIndex == this.numberOfPlayers) {
      this.currentPlayerIndex %= this.numberOfPlayers;
      this.totalTurns++;
      this.needToTurn = false;
    }
  }

  /**
   * if the player can turn
   * 
   * @return true if the player needs to turn, false otherwise
   */
  public final boolean getNeedToTurn() { return this.needToTurn; }

  public final int getTotalTurns() { return this.totalTurns; }

  /**
   * Checks if the game has reached the end based on a set number of turns.
   * 
   * @param maxTurns Maximum turns allowed in the game.
   * @return true if the game should end, false otherwise
   */
  public final boolean isGameEnd() { return this.totalTurns >= Constants.MAX_GAME_TURNS; }

}
