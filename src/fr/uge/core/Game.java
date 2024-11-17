package fr.uge.core;

import java.util.ArrayList;


/** to improve later */
public final class Game {
  private ArrayList<Player> players;    // player(-s)
  private GameBoard board;              // available: tiles and/or tokens 
  private TurnManager turnManager;      // 20 turns for entire game

  public Game() {
    this.players = List.of(new Player("Player 1"), new Player("Player 2"));
    this.turnManager = new TurnManager();
    this.board = new GameBoard();
  }


  /* we need to read first of all their names as String
  * we have only two players, so for each player we need to
  * ask if they want to change their name from Player 1 and Player 2 to
  * their own name.
  */
  public void setPlayers() {
    players = new ArrayList<>();
    for (int i = 1; i <= 2; i++) {
      System.out.println("Enter name for Player " + i + " (or press Enter to keep default): ");
      String name = new java.util.Scanner(System.in).nextLine();
      if (name.isEmpty()) {
        name = "Player " + i;
      }
      players.add(new Player(name));
    }
  }

  public void startGame() {
    // Initialize game components and start the game loop
  }

  public void endGame() {
    // Ends the game and performs final scoring
  }
    /* ends the game and performs final scoring */ 
  }

  public int performCalculations() {
    /* for every player we calculate their score
     * based on WildlifeScoringCard for every animal
     * 
     * After, we must add additional score
     * based on every tile: (Forest, Mountain etc ...)
     * if player has the most habitat tile type than other players
     * so he gets +1 points on each habitat type.
     * 
     * */
    return 0;
  }

  public static void main(String[] args) {
      
  }

}
