package fr.uge.deck;

import java.util.ArrayList;
import fr.uge.environment.Tile;

public record Deck() {
  private static final ArrayList<Tile> habitatTiles = new ArrayList<Tile>();
  private static int index = 0;
  public Deck {

  }


  /* draws a habitat tile from the deck */
  public Tile drawTile() {
    if (habitatTiles.size() >= 1 && index <= 85) {
      // 
    }

    Tile tile = habitatTiles.get(index);
    index++;
    /* implementation */
    return tile;
  }


  /* shuffles the deck of habitat tiles */
  public void shuffleDeck() {
    /* TO DO */
  }


}
