package fr.uge.environment;

import java.util.Objects;

public final class KeystoneTile implements Tile {

  private final TileType habitat;          /* habitat type */
  private final WildlifeType animal;       /* animal type  */
  private boolean occupied = false;
  private WildlifeType placedAnimal = null; 

  public KeystoneTile(TileType habitat, WildlifeType animal) {
    this.habitat = Objects.requireNonNull(habitat);
    this.animal = Objects.requireNonNull(animal);
  }


  public final boolean isOccupied() {
    return this.occupied;
  }

  /**
   * Place an animal on tile if it's possible 
   * @param token - New Animal
   * @return boolean */
  @Override
  public final boolean canBePlaced(WildlifeType token){
    // Objects.requireNonNull(token);
    if (isOccupied()) {
      return false;
    }
    return animal.equals(token);
  }


  public final boolean placeAnimal(WildlifeType token) {
    if (canBePlaced(token)) {
      this.occupied = true;
      this.placedAnimal = token;            
    }
    return false;
  }
  
  @Override
  public final WildlifeType getAnimal() { 
    return this.placedAnimal;
  }
  

  @Override
  public String toString() {
    return this.habitat + ": " + this.animal;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(this.habitat, this.animal);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof KeystoneTile other
          && animal.equals(other.animal)
          && habitat.equals(other.habitat);
  }

  

}
