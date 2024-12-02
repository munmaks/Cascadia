package fr.uge.core;

import fr.uge.environment.Coordinates;
import fr.uge.util.Constants;
import java.util.Objects;

/**
 * Game class represents the main game object that contains the game board and the turn manager.<br>
 * It is responsible for initializing the game components and starting the game loop.
 */
public final class Game {
  private final GameBoard board;          // available: tiles and/or tokens 
  private final TurnManager turnManager;  // 20 turns for entire game
  
  private final int version;
  private final int playerCount;     // number of players


  public Game(
      GameBoard board,          /* 1 game board */
      TurnManager turnManager,  /* 20 turns for entire game */
      int playerCount,          /* number of players */
      int version
    ){
    this.board = Objects.requireNonNull(board);
    this.turnManager = Objects.requireNonNull(turnManager);
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    this.version = version;
    if (playerCount < 1 || playerCount > 4) {
      throw new IllegalArgumentException("Invalid number of players");
    }
    this.playerCount = playerCount;
    initializeGame();
  }

  /**
   * @return number of players in the game.
   */
  public final int getPlayerCount() {
    return this.playerCount;
  }

  /**
   * @return the game board.
   */
  public final GameBoard board() {
    return this.board;
  }

  /**
   * @return the turn manager.
   */
  public final TurnManager turnManager() {
    return this.turnManager;
  }

  

  public void startGame() {
    /* Initialize game components and start the game loop */
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

  /**
   * Initializes the game by placing the starter tiles for each player.
   * The starter tiles are placed in the center of the board.
   * The placement of starter tiles depends on the version of the game.
   */
  private void initializeGame() {
    Coordinates centerCoordinates = new Coordinates(0, 0);
    for (var i = 0; i < this.playerCount; ++i) {
      placeStarterTiles(i, centerCoordinates);
    }
  }

  /**
   * Places the starter tiles for a player at the center of the board.
   * @param playerIndex the index of the player.
   * @param centerCoordinates the center coordinates of the board.
   */
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
    if (this.version == Constants.VERSION_HEXAGONAL) {
      placeStarterTilesHexagonal(playerIndex, centerCoordinates, 2, 3);
    } else {
      placeStarterTilesSquare(playerIndex, centerCoordinates, 1, 2);
    }

    // cells.add(new Cell(centerCoordinates, topTile));
    // var cell = getCell(tile.coordinates());
    // placeTile(cell, tile);
  }


  private void placeStarterTilesSquare(
      int playerIndex,
      Coordinates centerCoordinates,
      int leftNeighborNumber,
      int rightNeighborNumber
    ) {
    var starter = board.getBag().getStarter();
    var playerEnvironment = turnManager.getPlayerByIndex(playerIndex).environment();

    var cell = playerEnvironment.getCellOrCreate(centerCoordinates); /* main cell */
    playerEnvironment.placeTile(cell, starter[0]);

    var neighborCell = playerEnvironment.getOneNeighbor(cell, leftNeighborNumber);     /* on down from current cell - 1 */
    playerEnvironment.placeTile(neighborCell, starter[1]);

    neighborCell = playerEnvironment.getOneNeighbor(neighborCell, rightNeighborNumber); /* on right from neighbor cell - 3*/
    playerEnvironment.placeTile(neighborCell, starter[2]);
  }


  private void placeStarterTilesHexagonal(
      int playerIndex,
      Coordinates centerCoordinates,
      int leftNeighborNumber,
      int rightNeighborNumber
    ) {
    var starter = board.getBag().getStarter();
    var playerEnvironment = turnManager.getPlayerByIndex(playerIndex).environment();

    var cell = playerEnvironment.getCellOrCreate(centerCoordinates); /* main cell */
    playerEnvironment.placeTile(cell, starter[0]);

    var neighborCell = playerEnvironment.getOneNeighbor(cell, leftNeighborNumber);    /* left down cell - 2*/
    playerEnvironment.placeTile(neighborCell, starter[1]);

    neighborCell = playerEnvironment.getOneNeighbor(cell, rightNeighborNumber);
    playerEnvironment.placeTile(neighborCell, starter[2]);     /* right down cell - 3*/
  }




  

  
  


}
