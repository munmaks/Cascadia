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

  /**
   * initialize a TurnManager instance with the list of players and the version of the game
   *
   * @param listOfPlayers the list of the players in the game
   * @param version the version where the player play
   */
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

  /**
   * accessor for the current player of the game
   *
   * @return the current player
   */
  public final Player getCurrentPlayer() {
    return this.players.get(this.currentPlayerIndex);
  }

  /**
   * get a specific player in the game by his index
   *
   * @param index the index of the player that we want to have
   * @return the player of the given index
   */
  public final Player getPlayerByIndex(int index) {
    if (index < 0 || index >= this.playersLength) {
      throw new IllegalArgumentException("Invalid index");  /* to improve later */
    }
    return this.players.get(index);
  }

  /**
   * accessor for all the player of the game
   *
   * @return a list with all the player
   */
  public final List<Player> getAllPlayers(){
    return this.players;
  }

  /**
   * make the currentPlayerIndex change by the next player in the list
   */
  public final void changePlayer() {
    this.currentPlayerIndex++;
//    if (this.currentPlayerIndex == this.playersLength - 1) {
//      this.currentPlayerIndex = 0;
//      needToTurn = true;
//    }
  }

  /**
   * count the turn of the round, a turn is incremented when all the players has played once
   */
  public final void nextTurn() {
    if (this.currentPlayerIndex == this.playersLength) {
      this.currentPlayerIndex %= this.playersLength;
      this.totalTurns++;
      this.needToTurn = false;
    }
  }

  /**
   * accessor for needToTurn
   *
   * @return true if we need to turn, false otherwise
   */
  public final boolean getNeedToTurn(){
    return this.needToTurn;
  }

  /**
   * accessor for the getTotalTurns
   *
   * @return the total turns in the game for now
   */
  public final int getTotalTurns() {
    return this.totalTurns;
  }
  
  /**
   * Checks if the game has reached the end based on a set number of turns.
   *
   * @return true if the game should end, false otherwise
   */
  public final boolean isGameEnd() {
    return this.totalTurns >= Constants.MAX_GAME_TURNS;
  }

}
