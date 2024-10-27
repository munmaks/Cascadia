package fr.uge.core;

import java.util.ArrayList;


public final class Game {
  private final ArrayList<Player> players;  // player(-s)
  private final GameBoard board;            // available: tiles and/or tokens 
  private final TurnManager turnManager;    // 20 turns for entire game

  public Game() {
    this.turnManager = new TurnManager();
    this.board = new GameBoard();
    this.players = new ArrayList<Player>();
  }
  
  
}
