package fr.uge.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;

import fr.uge.deck.Bag;
import fr.uge.deck.Deck;
import fr.uge.util.Constants;


public record Environment(
      int version
    ) {

  // private static int nature_tokens = 0;    // for player

  /* environment of all placed tiles by one player */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();

  /* 100 - 196 total tiles to show in player's environment */
  private static final Cell[][] grid = new Cell[Constants.MAX_SIZE][Constants.MAX_SIZE];
  
  private static final Coordinates[] NEIGHBORS_SQUARE = new Coordinates[Constants.NB_NEIGHBORS_SQUARE];
  private static final Coordinates[] NEIGHBORS_HEXAGONAL = new Coordinates[Constants.NB_NEIGHBORS_HEXAGONAL];

  private static int nbNeighbors = 0;


  public Environment {
    /* class for all check and valid parameters to stop checking everytime THE SAME THING */
    if (version <= 0 || version > 4) {
      throw new IllegalArgumentException("Game Version must be 1, 2");
    }
    for (var row = 0; row < Constants.MAX_SIZE; ++row) {  /* row is `y` */
      for (var column = 0; column < Constants.MAX_SIZE; ++column) { /* column is `x` */
        grid[row][column] = new Cell(new Coordinates(row, column), version);
      }
    }
    nbNeighbors = (version == Constants.VERSION_HEXAGONAL) ?
                  Constants.NB_NEIGHBORS_HEXAGONAL:
                  Constants.NB_NEIGHBORS_SQUARE; 
  }



  
  /* adds a new tile to Player's environment */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);

    /* update all neighbors */

    tiles.add(tile);
  }
  
  
  private static boolean validCoordinates(int x, int y) {
    return (y >= 0 && y < Constants.MAX_SIZE &&
            x >= 0 && x < Constants.MAX_SIZE);
    }
  

  public final List<Coordinates> getNeighborsCoordinatesSquare(Coordinates coordinates){
    Objects.requireNonNull(coordinates);
    var list = new ArrayList<Coordinates>();
    var coords = new int[][] {
      {coordinates.x(), coordinates.y() - 1}, // (0, -1)
      {coordinates.x() - 1, coordinates.y()}, // (-1, 0)
      {coordinates.x(), coordinates.y() + 1}, // (0, +1)
      {coordinates.x() + 1, coordinates.y()}  // (+1, 0)
    };

    for (var coord : coords) {
      if (validCoordinates(coord[0], coord[1])) {
        list.add(new Coordinates(coord[0], coord[1]));
      }
    }
    return List.copyOf(list);
  }
  
  
  public final List<Coordinates> getNeighborsCoordinates(Coordinates coordinates){
    Objects.requireNonNull(coordinates);
    var list = new ArrayList<Coordinates>();
    
    
    
    return List.copyOf(list);
  }

  
  public final List<Tile> getNeighborsTiles(Coordinates coordinates){
    Objects.requireNonNull(coordinates);
    var list = new ArrayList<Tile>();
    var z = -(coordinates.x() + coordinates.y());   /* third parameter */
//    if (version == MAX_VERSION) {
//      ;
//    } else {
//      ;
//    }

    return List.copyOf(list);
  }
  
  
  public final boolean canBePlaced(Tile tile, Coordinates coordinates) {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(coordinates);

    return true;
  }


  /* places a wildlife token on a tile within the environment */
  public final boolean placeTile(Tile tile, Coordinates coordinates) {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(coordinates);
    var currentCell = grid[coordinates.x()][coordinates.y()];
    if (currentCell.placeTile(tile)) {
      tiles.add(tile);
      return true;
    }
    return false;
  }


  public final boolean placeWildlifeToken(WildlifeToken token, Coordinates coordinates) {
    Objects.requireNonNull(token);
    Objects.requireNonNull(coordinates);
    var currentCell = grid[coordinates.x()][coordinates.y()];
    if (!currentCell.isOccupied()) {
      return false;
    }
    var currentTile = currentCell.getTile();

    boolean flag = false;
    switch (currentTile) {
      case HabitatTile habitat -> { flag = habitat.placeAnimal(token);}
      case KeystoneTile keystone -> { flag = keystone.placeAnimal(token); }
      /* normally should be here */
      case EmptyTile empty -> { ; }
      case StarterHabitatTile s -> { System.err.println("Can't place wildlife token on starter tile"); }
    }
    return flag;
  }

  

  public Cell getCell(Coordinates coordinates) {
    Objects.requireNonNull(coordinates);
    return grid[coordinates.x()][coordinates.y()];
  }


  
  /* gets all tiles in the environment */
  public final List<Tile> getTiles() {
    return List.copyOf(tiles);
  }
  
//  /**
//   * Gives max size for every instance of coordiantes 
//   * @return max_size
//   * */
//  public final int getMaxSize() {
//    return Constants.MAX_SIZE;
//  }
  
  
  

  // for tests
  public static void main(String[] main) {
    var version = 2;
    var env = new Environment(version);
    var bag = new Bag(2, version);
    var deck = new Deck(version);
    var coord = new Coordinates(0, 0);
    var tile = bag.getRandomTile();
    var token = deck.getRandomToken();

    env.placeTile(tile, coord);
    env.placeWildlifeToken(token, coord);
    var cell = env.getCell(coord);

    System.out.println("tile: " + tile.toString() + " token: " + token.toString());
    System.out.println(cell);
  }
  
}

