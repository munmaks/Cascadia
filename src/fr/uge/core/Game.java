package fr.uge.core;

import fr.uge.environment.Coordinates;
import fr.uge.util.Constants;
import java.util.Objects;

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
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.version = version;
    if (playerCount < 1 || playerCount > 4) {
      throw new IllegalArgumentException("Invalid number of players");
    }
    this.playerCount = playerCount;
    initializeGame();
  }


  public final int getPlayerCount() {
    return this.playerCount;
  }

  public final GameBoard board() {
    return this.board;
  }

  public final TurnManager turnManager() {
    return this.turnManager;
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
    for (var i = 0; i < this.playerCount; ++i) {
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
    if (this.version == Constants.VERSION_HEXAGONAL) {
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
    var playerEnvironment = turnManager.getPlayerByIndex(playerIndex).environment();

    var cell = playerEnvironment.getCell(centerCoordinates.y(), centerCoordinates.x()); /* main cell */
    playerEnvironment.placeTile(cell, topTile);

    var neighborCell = playerEnvironment.getNeighborSquare(cell, 1);     /* on down from current cell */
    playerEnvironment.placeTile(neighborCell, leftTile);

    neighborCell = playerEnvironment.getNeighborSquare(neighborCell, 2); /* on right from neighbor cell*/
    playerEnvironment.placeTile(neighborCell, rightTile);
  }

  


  private void placeStarterTilesHexagonal(int playerIndex, Coordinates centerCoordinates) {
    var starter = board.getBag().getStarterHabitatTile();
    var playerEnvironment = turnManager.getPlayerByIndex(playerIndex).environment();

    var cell = playerEnvironment.getCell(centerCoordinates.y(), centerCoordinates.x()); /* main cell */
    playerEnvironment.placeTile(cell, starter.topTile());

    var neighborCell = playerEnvironment.getNeighborSquare(cell, 2);    /* left down cell */
    playerEnvironment.placeTile(neighborCell, starter.leftTile());

    neighborCell = playerEnvironment.getNeighborSquare(cell, 3);
    playerEnvironment.placeTile(neighborCell, starter.rightTile());     /* right down cell */

  }




  

  
  


}
