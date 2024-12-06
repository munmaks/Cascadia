package fr.uge.environment;

public final class EmptyTile implements Tile {

  public EmptyTile(){
    /* don't have any habitats and any animals */
  }

  @Override
  public WildlifeType getAnimal() {
    return null;
  }
  
  @Override
  public boolean canBePlaced(WildlifeType token) {
    return false;
  }

  @Override
  public final String toString() {
    return "empty tile";
  }
}
