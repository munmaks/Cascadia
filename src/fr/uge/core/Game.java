package fr.uge.core;

import java.util.List;
import java.util.Objects;


import fr.uge.environment.Coordinates;
import fr.uge.util.Constants;

public final class Game {
  private final List<Player> players;     // player(-s)
  private final GameBoard board;          // available: tiles and/or tokens 
  private final TurnManager turnManager;  // 20 turns for entire game

  private final int version;



  public Game(
      List<Player> players,     /* 1 - 4 players */
      GameBoard board,          /* 1 game board */
      TurnManager turnManager,  /* 20 turns for entire game */
      int version
    ){
    this.players = Objects.requireNonNull(players);
    this.board = Objects.requireNonNull(board);
    this.turnManager = Objects.requireNonNull(turnManager);
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.version = version;
    initializeGame();
  }


  public final List<Player> players() {
    return players;
  }

  public final GameBoard board() {
    return board;
  }

  public final TurnManager turnManager() {
    return turnManager;
  }

  

  /* we need to read first of all their names as String
  * we have only two players, so for each player we need to
  * ask if they want to change their name from Player 1 and Player 2 to
  * their own name.
  */

  public void startGame() {
    // Initialize game components and start the game loop
  }

  public void endGame() {
    // Ends the game and performs final scoring
  }
    /* ends the game and performs final scoring */ 

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


  private void initializeGame() {
    Coordinates centerCoordinates = new Coordinates( (int)(Constants.MAX_ROW / 2), (int)(Constants.MAX_COL / 2) );
    for (var i = 0; i < players.size(); ++i) {
      placeStarterTiles(i, centerCoordinates);
    }
  }


  private void placeStarterTiles(int playerIndex, Coordinates centerCoordinates) {
    /**
     * Hexagonal version:
     *   X
     * Y   Z
     * 
     * Square version:
     * X
     * Y Z
    */
    if (version == Constants.VERSION_HEXAGONAL) {
      placeStarterTilesHexagonal(playerIndex, centerCoordinates);
    } else {
      placeStarterTilesSquare(playerIndex, centerCoordinates);
    }

    // cells.add(new Cell(centerCoordinates, topTile));
    // var cell = getCell(tile.coordinates());
    // placeTile(cell, tile);
  }

  private void placeStarterTilesSquare(int playerIndex, Coordinates centerCoordinates) {
    var topTile = board.getBag().getRandomTile();
    var leftTile = board.getBag().getRandomTile();
    var rightTile = board.getBag().getRandomTile();
    var cell = players.get(playerIndex).environment().getCell(centerCoordinates.y(), centerCoordinates.x());
    players.get(playerIndex).environment().placeTile(cell, topTile);

    var neighborCell = players.get(playerIndex).environment().getNeighborSquare(cell, 1);     /* on down from current cell */
    players.get(playerIndex).environment().placeTile(neighborCell, leftTile);

    neighborCell = players.get(playerIndex).environment().getNeighborSquare(neighborCell, 2); /* on right from neighbor cell*/
    players.get(playerIndex).environment().placeTile(neighborCell, rightTile);
  }

  


  private void placeStarterTilesHexagonal(int playerIndex, Coordinates centerCoordinates) {
    var starter = board.getBag().getStarterHabitatTile();
    var cell = players.get(playerIndex).environment().getCell(centerCoordinates.y(), centerCoordinates.x());
    players.get(playerIndex).environment().placeTile(cell, starter.topTile());

    var neighborCell = players.get(playerIndex).environment().getNeighborSquare(cell, 1);
    players.get(playerIndex).environment().placeTile(neighborCell, starter.leftTile());

    neighborCell = players.get(playerIndex).environment().getNeighborSquare(cell, 3);
    players.get(playerIndex).environment().placeTile(neighborCell, starter.rightTile());


  }





}
