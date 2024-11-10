package fr.uge.environment;

import java.util.Objects;

public record KeystoneTile(
      TileType tile,          /* habitat type */
      WildlifeType animal     /* animal type  */
    ) implements Tile {

  private static boolean occupied = false;

  private static WildlifeToken placedAnimal = null; 

  public KeystoneTile {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(animal);
  }


  public final boolean isOccupied() {
    return occupied;
  }

  /**
   * Place an animal on tile if it's possible 
   * @param token - New Animal
   * @return boolean */
  public final boolean canBePlaced(WildlifeToken token){
    if (isOccupied()) {
      return false;
    }
    return animal.equals(token.animal());
  }


  public final boolean placeAnimal(WildlifeToken token) {
    if (canBePlaced(token)) {
      occupied = true;
      placedAnimal = token;            
    }
    return false;
  }
  
  
  public final WildlifeToken getAnimal() { 
    return placedAnimal;
  }
  

  @Override
  public String toString() {
    return tile + ": " + animal;
  }
  
}
