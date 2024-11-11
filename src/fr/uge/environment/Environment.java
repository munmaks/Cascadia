package fr.uge.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
// import java.util.HashMap;

import fr.uge.bag.Bag;
import fr.uge.bag.Deck;
import fr.uge.util.Constants;

/**
 * Player's environment of all tiles and placed wildlife tokens */
public record Environment(
      int version
    ) {

  // private static int nature_tokens = 0;    // for player

  /* environment of all placed tiles by one player */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();

  /* 100 - 196 total tiles to show in player's environment */
  private static final Cell[][] grid = new Cell[Constants.MAX_ROW][Constants.MAX_COL];
  
//  private static final Coordinates[] NEIGHBORS_SQUARE = new Coordinates[Constants.NB_NEIGHBORS_SQUARE];
//  private static final Coordinates[] NEIGHBORS_HEXAGONAL = new Coordinates[Constants.NB_NEIGHBORS_HEXAGONAL];

  private static int nbNeighbors = 0;


  public Environment {
    /* class for all check and valid parameters to stop checking everytime THE SAME THING */
    if (version <= 0 || version > 4) {
      throw new IllegalArgumentException("Game Version must be 1, 2");
    }
    initializeGrid(version);

    nbNeighbors = (version == Constants.VERSION_HEXAGONAL) ?
                  Constants.NB_NEIGHBORS_HEXAGONAL:
                  Constants.NB_NEIGHBORS_SQUARE; 
  }

  
  private final void initializeGrid(int version) {
    for (var row = 0; row < Constants.MAX_ROW; ++row) {  /* row is `y` */
      for (var col = 0; col < Constants.MAX_COL; ++col) { /* col is `x` */
        grid[row][col] = new Cell(new Coordinates(row, col), version);
      }
    }
  }


  
  /* adds a new tile to Player's environment */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);

    /* update all neighbors */

    tiles.add(tile);
  }
  
  
  private static boolean validCoordinates(int y, int x) {
    return (y >= 0 && y < Constants.MAX_ROW &&
            x >= 0 && x < Constants.MAX_COL);
    }
  

  public final List<Cell> getNeighborsCoordinatesSquare(Cell cell){
    Objects.requireNonNull(cell);
    var neighbors = new ArrayList<Cell>();

    for (var coord : Constants.SQUARE_DIRECTION_DIFFERENCES) {
      if (validCoordinates(coord[1], coord[0])) {
        neighbors.add(new Cell(new Coordinates(coord[1], coord[0]), version));
      }
    }
    return List.copyOf(neighbors);
  }
  
  
//  public final List<Cell> getNeighborsCoordinatesHexagone(Cell cell){
//    Objects.requireNonNull(cell);
//    var neighbors = new ArrayList<Cell>();
//    var coords = new int[][] {
//      { cell.coordinates().y(), cell.coordinates().x() - 1 },     // (0, -1)   - left
//      { cell.coordinates().y(), cell.coordinates().x() },     // (-1, 0)   - 
//      { cell.coordinates().y(), cell.coordinates().x() }, // (-1, +1)  - 
//      { cell.coordinates().y(), cell.coordinates().x() + 1 },     // (0, +1)   - right
//      { cell.coordinates().y(), cell.coordinates().x() },     // (+1, 0)   - 
//      { cell.coordinates().y(), cell.coordinates().x() }  // (+1, -1)  - 
//
//    };
//
//    for (var coord : coords) {
//      if (validCoordinates(coord[0], coord[1])) {
//        neighbors.add(new Cell(new Coordinates(coord[0], coord[1]), version));
//      }
//    }
//    return List.copyOf(neighbors);
//  }

  
  
  
  /**
   * Gets the neighboring hex cell in a specified direction for a given hex cell.
   * 
   * @param cell      The cell for which to find a neighbor.
   * @param direction An integer representing one of the six directions (0-5).
   * @return          The neighboring cell if within bounds, otherwise null.
   */
  private Cell oddrOffsetNeighbor(Cell cell, int direction) {
    // parity: 0 - even rows, 1 - odd rows
    var parity = cell.coordinates().y() & 1; 
    var diff = Constants.HEXAGONE_DIRECTION_DIFFERENCES[parity][direction];

    var neighborRow = cell.coordinates().y() + diff[1];
    var neighborCol = cell.coordinates().x() + diff[0];

    // validate neighbor coordinates
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
  public List<Cell> getOffsetNeighbors(Cell cell) {
      var neighbors = new ArrayList<Cell>();

      for (var direction = 0; direction < Constants.MAX_ROTATIONS; ++direction) {
          var neighbor = oddrOffsetNeighbor(cell, direction);
          if (null != neighbor) {
              neighbors.add(neighbor);
          }
      }

      return List.copyOf(neighbors);
  }
  
  
  
  
  public final List<Tile> getNeighborsTiles(Cell cell){
    Objects.requireNonNull(cell);
    var list = new ArrayList<Tile>();
    
//    var z = cell.getThirdParameter();   /* third parameter */
//    if (version == MAX_VERSION) {
//      ;
//    } else {
//      ;
//    }

    return List.copyOf(list);
  }
  
  
  public final boolean canBePlaced(Tile tile, Cell cell) {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(cell);
    
    
    
    return true;
  }


  /* places a wildlife token on a tile within the environment */
  public final boolean placeTile(Cell cell, Tile tile) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(tile);
    if (cell.placeTile(tile)) {
      tiles.add(tile);  /* placed and added to all player's tiles */
      return true;
    }
    return false;
  }

  
  public final boolean canBePlacedWildlifeToken(WildlifeToken token) {
    Objects.requireNonNull(token);
    boolean flag = false;
    for (var tile : tiles) {
      switch (tile) {
        case HabitatTile habitat -> { flag = habitat.canBePlaced(token);}
        case KeystoneTile keystone -> { flag = keystone.canBePlaced(token); }
        /* normally shouldn't happen */
        case EmptyTile empty -> { System.err.println("Can't be placed wildlife token on empty tile"); }
        case StarterHabitatTile s -> { System.err.println("Can't be placed wildlife token on starter tile"); }
      }
      if (flag) {
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



  public static Cell getCell(Coordinates coordinates) {
    Objects.requireNonNull(coordinates);
    return grid[coordinates.y()][coordinates.x()];
  }



  /* gets all tiles in the environment */
  public final List<Tile> getTiles() {
    return List.copyOf(tiles);
  }
  
  
  
  // for tests
  public static void main(String[] main) {
    var version = 3;
    var env = new Environment(version);
    var bag = new Bag(3, version);
    var deck = new Deck(version);
    var coord = new Coordinates(0, 0);
    var tile = bag.getRandomTile();
    var token = deck.getRandomToken();

    var cell = env.getCell(coord);
    env.placeTile(cell, tile);
    env.placeWildlifeToken(cell, token);

    System.out.println("tile: " + tile.toString() + " token: " + token.toString());
    System.out.println(cell);
  }
  
  
}

