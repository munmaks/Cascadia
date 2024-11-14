package fr.uge.core;

import java.util.ArrayList;

/** to improve later */
public final class Game {
  private ArrayList<Player> players;    // player(-s)
  private GameBoard board;              // available: tiles and/or tokens 
  private TurnManager turnManager;      // 20 turns for entire game

  private Game() {
//    this.turnManager = new TurnManager();
//    this.board = new GameBoard();
//    this.players = new ArrayList<Player>();
  }


  public void startGame() {
    /* Implementation */ 
  }

  
  public void endGame() {
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
}
