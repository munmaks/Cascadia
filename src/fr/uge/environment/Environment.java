package fr.uge.environment;

import java.util.ArrayList;
import java.util.HashMap;
// import java.util.Objects;
import java.util.Objects;

public record Environment() {
  // private final static int nature_tokens = 0;    // for player

  
  /* we need coordiantes for every tile, */
  // TO DO
 
  private final static ArrayList<Tile> tiles = new ArrayList<Tile>();   // environment of all placed tiles by one player
  
  // using hashmap?
  // private final static HashMap<Tile, WildlifeToken> tileToWildlifeMapping = new HashMap<Tile, WildlifeToken>();

  public Environment {
    
  }


  // to improve later
  public void addTile(Tile tile) {
    Objects.requireNonNull(tile);
    tiles.add(tile);
  }

  
  public void placeWildlifeToken(Tile tile, WildlifeToken token) {
    /* TO DO */
  }

  

  
}
