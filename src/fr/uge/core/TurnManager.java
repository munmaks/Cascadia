package fr.uge.core;

import fr.uge.util.Constants;
import java.util.List;

public final class TurnManager {
  private static int totalTurns = 0;
  private static int currentPlayerIndex = 0;
  private static List<Player> players;

  public TurnManager(List<Player> players, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    if (players == null || players.isEmpty()) {
      throw new IllegalArgumentException("Players list cannot be null or empty");
    }
    // copy the list of players to avoid any modification
    this.players = List.copyOf(players);
  }

  public static Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public static void nextTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    totalTurns++;
  }
  
  public static int getTotalTurns() {
    return totalTurns;
  }

  /**
   * Checks if the game has reached the end based on a set number of turns.
   * 
   * @param maxTurns Maximum turns allowed in the game.
   * @return true if the game should end, false otherwise
   */
  public static boolean isGameEnd(int maxTurns) {
    return totalTurns >= Constants.MAX_GAME_TURNS;
  }

  // // still need to test this class
  // public static void main(String[] args) {
  //   // Test the TurnManager class
  //   var player1 = new Player("Alice", 1);
  //   var player2 = new Player("Bob", 1);
  //   var player3 = new Player("Charlie", 1);
  //   var player4 = new Player("David", 1);

  //   var players = new ArrayList<Player>();
  //   players.add(player1);
  //   players.add(player2);
  //   players.add(player3);
  //   players.add(player4);    
  //   var turnManager = new TurnManager(players, 1);
  //   System.out.println(getCurrentPlayer());
  //   nextTurn();
  //   System.out.println(getCurrentPlayer());
  //   System.out.println(getTotalTurns());
  //   System.out.println(getCurrentPlayer());
  //   System.out.println(players.get(1));
  // }

}
