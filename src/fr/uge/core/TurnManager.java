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
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.playersLength = listOfPlayers.size();
    if (Constants.isInvalidSquareNbPlayers(this.playersLength, version)) {
      throw new IllegalArgumentException(Constants.IllegalSquareNbPlayers);
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
      needToTurn = false;
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

     // still need to test this class
//   public static void main(String[] args) {
//     // Test the TurnManager class
//     var version = Constants.VERSION_HEXAGONAL;
//     var player1 = new Player("Alice", version);
//     var player2 = new Player("Bob", version);
//     var player3 = new Player("Charlie", version);
//     var player4 = new Player("David", version);
//
//     var players = new ArrayList<Player>();
//     players.add(player1);
////     players.add(player2);
////     players.add(player3);
////     players.add(player4);
//     var turnManager = new TurnManager(players, version);
//
//     for (var i = 1; i < players.size() * Constants.TILE_PER_PLAYER + 1; ++i) {
//       System.out.println(i + ": " + turnManager.getCurrentPlayer());
//       turnManager.changePlayer();
//
//       if (i % players.size() == 0 && i != 0) {
//         turnManager.nextTurn();
//         System.out.println("turns : " + turnManager.getTotalTurns());
//       }
//
//       turnManager.nextTurn();
//       System.out.println(isGameEnd());
//     }
//     System.out.println(isGameEnd());
//   }

}
