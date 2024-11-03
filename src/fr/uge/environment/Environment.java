package fr.uge.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public record Environment() {

  private final static int nature_tokens = 0;    // for player

  /* environment of all placed tiles by one player */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();


  private static final int [][] grid;


  /* we need 20 lines and 16 columns for best experience later */
  public Environment {
    grid = new int [10][10];  /* 100 total tiles to show in player's environment */
    
  }


  /* Adds a new tile to the environment */
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);
  }

  
  /* Places a wildlife token on a tile within the environment */
  public void placeWildlifeToken(Tile tile, WildlifeToken token) {
    /* TO DO */
  }

  /* Gets all tiles in the environment */
  public List<Tile> getTiles() {

    return null;
  }


  
}
