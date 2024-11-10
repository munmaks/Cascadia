package fr.uge.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;

import fr.uge.deck.Bag;
import fr.uge.deck.Deck;

public record Environment(int version) {

  // private static int nature_tokens = 0;    // for player

  /* environment of all placed tiles by one player */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();

  private static final int MAX_SIZE = 12; /* 10 - 14 */

  /* 100 - 196 total tiles to show in player's environment */
  private static final Cell[][] grid = new Cell[MAX_SIZE][MAX_SIZE];


  public Environment {

    /* class for all check and valid parameters to stop checking everytime THE SAME THING */
    if (version <= 0 || version > 2) {
      throw new IllegalArgumentException("Game Version must be 1, 2");
    }
    for (var i = 0; i < MAX_SIZE; ++i) {
      for (var j = 0; j < MAX_SIZE; ++j) {
        grid[i][j] = new Cell(new Coordinates(i, j), version);
      }
    }
  }


  /* adds a new tile to Player's environment */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);

    /* update all neighbors */

    tiles.add(tile);
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
    return grid[coordinates.x()][coordinates.y()];
  }
  

  /* gets all tiles in the environment */
  public final List<Tile> getTiles() {
    return List.copyOf(tiles);
  }

  /**
   * Gives max size for every instance of coordiantes 
   * @return max_size
   * */
  public final int getMaxSize() {
    return MAX_SIZE;
  }
  


  // for tests
//  public static void main(String[] main) {
//    var version = 2;
//    var env = new Environment(version);
//    var bag = new Bag(2, version);
//    var deck = new Deck(version);
//    var coord = new Coordinates(0, 0);
//    var tile = bag.getRandomTile();
//    var token = deck.getRandomToken();
//    
//    env.placeTile(tile, coord);
//    env.placeWildlifeToken(token, coord);
//    var cell = env.getCell(coord);
//
//    System.out.println("tile: " + tile.toString() + " token: " + token.toString());
//    System.out.println(cell);
//    
//  }
  
}
