package fr.uge.data.core;


import fr.uge.data.environment.Coordinates;
import fr.uge.data.util.Constants;

import java.util.List;
import java.util.Objects;

/**
 * Game class represents the main game object that contains the game board and
 * the turn manager.<br>
 * It is responsible for initializing the game components and starting the game
 * loop.
 */
public final class Game {
  private final GameBoard board; // available: tiles and/or tokens
  private final TurnManager turnManager; // 20 turns for entire game
  private final List<Player> players; // list of players

  private final int version;
  private final int playerCount; // number of players

  public Game(GameBoard board, /* 1 game board */
      TurnManager turnManager, /* 20 turns for entire game */
      List<Player> players, /* list of players */
      int version) {
    this.board = Objects.requireNonNull(board);
    this.turnManager = Objects.requireNonNull(turnManager);
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    this.version = version;

    this.playerCount = players.size();
    this.players = Objects.requireNonNull(players);
    initializeGame();
  }

  /**
   * @return the version of the game.
   */
  public final int getPlayerCount() { return this.playerCount; }

  /**
   * @return the game board.
   */
  public final GameBoard board() { return this.board; }

  /**
   * @return the list of players.
   */
  public final List<Player> getPlayers() { return List.copyOf(this.players); }

  /**
   * Returns the player at the specified index.
   * 
   * @param index the index of the player.
   * @return the player at the specified index.
   */
  public final Player getPlayerByIndex(int index) {
    if (index < 0 || index >= this.playerCount) {
      throw new IllegalArgumentException("Invalid index");
    }
    return this.players.get(index);
  }

  /**
   * @return the turn manager.
   */
  public final TurnManager turnManager() { return this.turnManager; }

  /**
   * initializes the game by placing the starter tiles for each player.
   * 
   * @return the winner of the game.
   */
  private void initializeGame() {
    Coordinates centerCoordinates = new Coordinates(0, 0);
    for (var i = 0; i < this.playerCount; ++i) {
      placeStarterTiles(i, centerCoordinates);
    }
  }

  /**
   * Places the starter tiles for a player at the center of the board.
   * 
   * @param playerIndex       the index of the player.
   * @param centerCoordinates the center coordinates of the board.
   */
  private void placeStarterTiles(int playerIndex, Coordinates centerCoordinates) {
    /**
     * Hexagonal version: X Y Z Square version: X Y Z
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

  /**
   * Places the starter tiles for a player in a square pattern.
   * 
   * @param playerIndex         the index of the player.
   * @param centerCoordinates   the center coordinates of the board.
   * @param leftNeighborNumber  left neighbor number.
   * @param rightNeighborNumber right neighbor number.
   */
  private void placeStarterTilesSquare(int playerIndex, Coordinates centerCoordinates,
      int leftNeighborNumber, int rightNeighborNumber) {
    var starter = board.getBag().getStarter(); /* 3 tiles */
    var playerEnvironment = players.get(playerIndex).getEnvironment();

    var cell = playerEnvironment.getCell(centerCoordinates); /* main cell */
    playerEnvironment.placeTile(cell, starter[0]);

    var neighborCell = playerEnvironment.getOneNeighbor(cell,
        leftNeighborNumber); /* on down from current cell - 1 */
    playerEnvironment.placeTile(neighborCell, starter[1]);

    /* on right from neighbor cell - 3 */
    neighborCell = playerEnvironment.getOneNeighbor(neighborCell, rightNeighborNumber);
    playerEnvironment.placeTile(neighborCell, starter[2]);
  }

  /**
   * Places the starter tiles for a player in a hexagonal pattern.
   * 
   * @param playerIndex         player index
   * @param centerCoordinates   center coordinates
   * @param leftNeighborNumber  left neighbor number
   * @param rightNeighborNumber right neighbor number
   */
  private void placeStarterTilesHexagonal(int playerIndex, Coordinates centerCoordinates,
      int leftNeighborNumber, int rightNeighborNumber) {
    var starter = board.getBag().getStarter(); /* 3 tiles */
    var playerEnvironment = players.get(playerIndex).getEnvironment();

    var cell = playerEnvironment.getCell(centerCoordinates); /* main cell */
    playerEnvironment.placeTile(cell, starter[0]);

    var neighborCell = playerEnvironment.getOneNeighbor(cell,
        leftNeighborNumber); /* left down cell - 2 */
    playerEnvironment.placeTile(neighborCell, starter[1]);

    neighborCell = playerEnvironment.getOneNeighbor(cell, rightNeighborNumber);
    playerEnvironment.placeTile(neighborCell, starter[2]); /* right down cell - 3 */
  }

}
