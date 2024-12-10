package fr.uge.core;

import fr.uge.util.Constants;
import java.util.List;
import java.util.Objects;

/**
 * Only one turn manager is created for the entire game.<br>
 * That's why we use static fields.
*/
public final class TurnManager {
  private List<Player> players;
  private int totalTurns = 0;
  private int currentPlayerIndex = 0;
  private int playersLength = 0;
  private boolean needToTurn = false;

  public TurnManager(List<Player> listOfPlayers, int version) {
    Objects.requireNonNull(listOfPlayers);
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    this.playersLength = listOfPlayers.size();
    if (Constants.isInvalidSquareNbPlayers(this.playersLength, version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_SQUARE_NUMBER_OF_PLAYERS);
    }
    /* listOfPlayers is already immutable, and we don't need to change */
    this.players = listOfPlayers;
  }

  public final Player getCurrentPlayer() {
    return this.players.get(this.currentPlayerIndex);
  }

  public final Player getPlayerByIndex(int index) {
    if (index < 0 || index >= this.playersLength) {
      throw new IllegalArgumentException("Invalid index");  /* to improve later */
    }
    return this.players.get(index);
  }
  
  
  public final List<Player> getAllPlayers(){
    return this.players;
  }


  public final void changePlayer() {
    this.currentPlayerIndex++;
//    if (this.currentPlayerIndex == this.playersLength - 1) {
//      this.currentPlayerIndex = 0;
//      needToTurn = true;
//    }
  }

  public final void nextTurn() {
    if (this.currentPlayerIndex == this.playersLength) {
      this.currentPlayerIndex %= this.playersLength;
      this.totalTurns++;
      this.needToTurn = false;
    }
  }
  
  public final boolean getNeedToTurn(){
    return this.needToTurn;
  }

  public final int getTotalTurns() {
    return this.totalTurns;
  }
  
  /**
   * Checks if the game has reached the end based on a set number of turns.
   * 
   * @param maxTurns Maximum turns allowed in the game.
   * @return true if the game should end, false otherwise
   */
  public final boolean isGameEnd() {
    return this.totalTurns >= Constants.MAX_GAME_TURNS;
  }

}
