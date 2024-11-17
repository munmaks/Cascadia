package fr.uge.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
// import java.util.HashMap;

import javax.sound.midi.SysexMessage;

import fr.uge.bag.Bag;
import fr.uge.bag.Deck;
import fr.uge.util.Constants;

/**
 * Player's environment of all tiles and placed wildlife tokens */
public record Environment(int version) {

  // private static int nature_tokens = 0;    // for player
  
  /* 100 - 196 total tiles to show in player's environment */
  private static final Cell[][] grid = new Cell[Constants.MAX_ROW][Constants.MAX_COL];

  /* environment of all placed tiles by one player */
  private static final ArrayList<Cell> cells = new ArrayList<>();
  

  private static int nbNeighbors = 0;


  public Environment {
    /* class for all check and valid parameters to stop checking everytime THE SAME THING */
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException("Game Version must be 1, 2");
    }
    initializeGrid(version);

    nbNeighbors = defineNbNeighbors(version); 
  }

  
  private static void initializeGrid(int version) {
    for (var row = 0; row < Constants.MAX_ROW; ++row) {  /* row is `y` */
      for (var col = 0; col < Constants.MAX_COL; ++col) { /* col is `x` */
        grid[row][col] = new Cell(new Coordinates(row, col), version);
      }
    }
  }

  private static int defineNbNeighbors(int version) {
    return (version == Constants.VERSION_HEXAGONAL) ?
            Constants.NB_NEIGHBORS_HEXAGONAL:
            Constants.NB_NEIGHBORS_SQUARE; 
  }
  
  
  /* adds a new tile to Player's environment */
//  private static void addTile(Tile tile) {
//    Objects.requireNonNull(tile);
//
//    /* update all neighbors */
//    
//    tiles.add(tile);
//  }

  
  /**
   * @param from this cell we determine a neighbor
   * @param direction - 0, 1, 2, 3
   * */
  private static Cell getNeighborSquare(Cell cell, int direction) {
    var diff = Constants.SQUARE_DIRECTION_DIFFERENCES[direction];
    var neighborRow = cell.coordinates().y() + diff[1];
    var neighborCol = cell.coordinates().x() + diff[0];
    /* validate neighbor coordinates */
    if (Constants.isValidCoordinates(neighborRow, neighborCol)) {
      return grid[neighborRow][neighborCol];
    }
    return null;
  }
  

  public final List<Cell> getNeighborsSquare(Cell cell){
    Objects.requireNonNull(cell);
    var neighbors = new ArrayList<Cell>();
    /* SQUARE_DIRECTION_DIFFERENCES: (x, y) */
    for (var direction = 0; direction < Constants.NB_NEIGHBORS_SQUARE; ++direction) {
      var neighbor = getNeighborSquare(cell, direction);
      if (null != neighbor) {
          neighbors.add(neighbor);
      }
    }
    return List.copyOf(neighbors);
  }  
  
  
  /**
   * Gets the neighboring hex cell in a specified direction for a given hex cell.
   * 
   * @param cell      The cell for which to find a neighbor.
   * @param direction An integer representing one of the six directions (0-5).
   * @return          The neighboring cell if within bounds, otherwise null.
   */
  private static Cell oddrOffsetNeighbor(Cell cell, int direction) {
    var parity = cell.coordinates().y() & 1;  /* parity: 0 - even rows, 1 - odd rows */

    var diff = Constants.HEXAGONE_DIRECTION_DIFFERENCES[parity][direction];

    var neighborRow = cell.coordinates().y() + diff[1];
    var neighborCol = cell.coordinates().x() + diff[0];

    /* validate neighbor coordinates */
    if (neighborRow < 0 || neighborRow >= Constants.MAX_ROW ||
        neighborCol < 0 || neighborCol >= Constants.MAX_COL) {
      return null;
    }
    return grid[neighborRow][neighborCol];
  }
  
  
  /**
   * <b>gets a copy of list of all valid neighbors for a given hex cell.</b>
   * 
   * @param cell The hex cell for which to retrieve neighbors.
   * @return     An immutable list of neighboring cells.
   */
  public final List<Cell> getOffsetNeighbors(Cell cell) {
      var neighbors = new ArrayList<Cell>();
      for (var direction = 0; direction < Constants.MAX_ROTATIONS; ++direction) {
          var neighbor = oddrOffsetNeighbor(cell, direction);
          if (null != neighbor) {
              neighbors.add(neighbor);
          }
      }
      return List.copyOf(neighbors);
  }
  
  
  
  /**
   * To determine neighbors when calculating final score
   * */
  public final List<Cell> getNeighborsCells(Cell cell){
    Objects.requireNonNull(cell);
    if (version == Constants.VERSION_HEXAGONAL) {
      return getOffsetNeighbors(cell);
    }
    return getNeighborsSquare(cell);
  }
  



  /* places a wildlife token on a tile within the environment */
  public final boolean placeTile(Cell cell, Tile tile) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(tile);
    // System.err.println("in function placeTile()");
    if (cell.placeTile(tile)) {
      // System.err.println("Tile is placed inside function placeTile()");
      cells.add(cell);  /* placed and added to all player's occupied cells */
      return true;
    }
    return false;
  }

  /**
   * Determine possibility to placed a wildlife token on player's environment. 
   * */
  public final boolean canBePlacedWildlifeToken(WildlifeToken token) {
    Objects.requireNonNull(token);
    boolean flag = false;
    for (var cell : cells) {
      switch (cell.getTile()) {
        case HabitatTile habitat -> { flag = habitat.canBePlaced(token);}
        case KeystoneTile keystone -> { flag = keystone.canBePlaced(token); }
        /* normally shouldn't happen */
        case EmptyTile empty -> { System.err.println("Can't be placed wildlife token on empty tile"); }
        case StarterHabitatTile s -> { System.err.println("Can't be placed wildlife token on starter tile"); }
      }
      if (flag) { /* found */
        return flag;
      }
    }
    return flag;
  }


  public final boolean placeWildlifeToken(Cell cell, WildlifeToken token) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(token);
    if (!cell.isOccupied()) {
      return false;
    }
    var currentTile = cell.getTile();
    boolean flag = false;
    switch (currentTile) {
      case HabitatTile habitat -> { flag = habitat.placeAnimal(token);}
      case KeystoneTile keystone -> { flag = keystone.placeAnimal(token); }
      /* normally shouldn't happen */
      case EmptyTile empty -> { System.err.println("Can't place wildlife token on empty tile"); }
      case StarterHabitatTile starter -> { System.err.println("Can't place wildlife token on starter tile"); }
    }
    return flag;
  }



  public final Cell getCell(Coordinates coordinates) {
    Objects.requireNonNull(coordinates);
    return grid[coordinates.y()][coordinates.x()];
  }

  
  
//  public final List<Cell> getCellsWithToken(WildlifeToken token){
//    Objects.requireNonNull(token);
//    var list = new ArrayList<Cell>();
//    for (var tile : cells) {
//      if (tile.)
//    }
//    return List.copyOf(list);
//  }
  


  /* gets all tiles in the environment */
  public final List<Cell> getCells() {
    return List.copyOf(cells);
  }
  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    for (var row = 0; row < Constants.MAX_ROW; ++row) {
      for (var col = 0; col < Constants.MAX_COL; ++col) {
        builder.append(grid[row][col].toString());
      }
      builder.append("\n");
    }
    return builder.toString();
  }
  
  
  // for tests
  // public static void main(String[] main) {
  //   var version = 3;
  //   var env = new Environment(version);
  //   var bag = new Bag(3, version);
  //   var deck = new Deck(version);
  //   var coord = new Coordinates(0, 0);
  //   var tile = bag.getRandomTile();
  //   var token = deck.getRandomToken();

  //   var cell = env.getCell(coord);
  //   env.placeTile(cell, tile);
  //   env.placeWildlifeToken(cell, token);

  //   // cell.turnСounterСlockwise();
  //   // System.out.println("rotation: " + cell.getRotation());
  //   System.out.println("tile: " + tile.toString() + " token: " + token.toString());
  //   System.out.println(cell);
  //   System.out.println(env.getCells());
  // }
  
  
}

