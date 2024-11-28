package fr.uge.environment;

public final class EmptyTile implements Tile {

  public EmptyTile(){
    /* don't have any habitats and any animals */
  }

  @Override
  public WildlifeToken getAnimal() {
    return null;
  }
  
  @Override
  public boolean canBePlaced(WildlifeToken token) {
    return false;
  }

  @Override
  public final String toString() {
    return "Empty Tile";
  }
}
