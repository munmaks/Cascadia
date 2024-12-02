package fr.uge.environment;

import java.util.Objects;

public final class KeystoneTile implements Tile {

  private final TileType habitat;          /* habitat type */
  private final WildlifeType animal;       /* animal type  */
  private boolean occupied = false;
  private WildlifeToken placedAnimal = null; 

  public KeystoneTile(TileType habitat, WildlifeType animal) {
    this.habitat = Objects.requireNonNull(habitat);
    this.animal = Objects.requireNonNull(animal);
  }


  public final boolean isOccupied() {
    return occupied;
  }

  /**
   * Place an animal on tile if it's possible 
   * @param token - New Animal
   * @return boolean */
  @Override
  public final boolean canBePlaced(WildlifeToken token){
    Objects.requireNonNull(token);
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
  
  @Override
  public final WildlifeToken getAnimal() { 
    return placedAnimal;
  }
  

  @Override
  public String toString() {
    return habitat + ": " + animal;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(habitat, animal);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof KeystoneTile other
          && animal.equals(other.animal)
          && habitat.equals(other.habitat);
  }

  

}
