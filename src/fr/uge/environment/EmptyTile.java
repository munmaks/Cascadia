package fr.uge.environment;

public record EmptyTile() implements Tile {

  public EmptyTile {
    /* don't have any habitats and any animals */
  }

  
  @Override
  public final String toString() {
    // TODO Auto-generated method stub
    return "Empty Tile";
  }
  
  @Override
  public WildlifeToken getAnimal() {
    return null;
  }
}
