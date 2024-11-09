package fr.uge.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public record Environment() {

  private static int nature_tokens = 0;    // for player

  /* environment of all placed tiles by one player */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();

  private static final int MAX_SIZE = 15; /* 10 - 15 */

  /* 100-225 total tiles to show in player's environment */
  private static final Cell[][] grid = new Cell[MAX_SIZE][MAX_SIZE];


  
  public Environment {
    
  }


  /* adds a new tile to Player's environment */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);
    /* update all neighbors */
    
    tiles.add(tile);
  }

  
  /* places a wildlife token on a tile within the environment */
  public void placeWildlifeToken(Tile tile, WildlifeToken token) {
    /* TO DO */
  }


  /* gets all tiles in the environment */
  public List<Tile> getTiles() {

    return null;  /*List.copyOf(list); */
  }

  /**
   * Gives max size for every instance of coordiantes 
   * @return max_size
   * */
  public final int getMaxSize() {
    return MAX_SIZE;
  }
  
}
