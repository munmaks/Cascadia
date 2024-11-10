package fr.uge.environment;

public record EmptyTile(
      Coordinates coordinates,
      int version   /* 1, 2 or 3 */
    ) implements Tile {

  public EmptyTile {
    /* don't have any habitats and any animals */
  }

  
  @Override
  public final String toString() {
    // TODO Auto-generated method stub
    return "Empty Tile";
  }
  
}
