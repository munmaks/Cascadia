package fr.uge.environment;

public record EmptyTile(
      int x,
      int y,
      int version   /* 1, 2 or 3 */
    ) implements Tile {

  public EmptyTile {
    /* don't have any habitats and any animals */
  }

}
