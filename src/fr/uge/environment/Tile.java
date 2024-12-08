package fr.uge.environment;

import java.util.Objects;
import java.util.Set;

public record Tile (
    TileType leftHabitat,
    TileType rightHabitat,
    Set<WildlifeType> animals
  ) {
  /*
   * configTilesWithTwoAnimals.txt
   * 45 Tiles : with two animals
   * RIVER, PRAIRIE : BEAR, SALMON
   * 
   * configTilesWithThreeAnimals.txt
   * 15 Tiles : with three animals
   * MOUNTAIN, WETLAND : ELK, HAWK, FOX
   **/
  // private boolean occupiedByAnimal = false;  /* idle */
  // private WildlifeType placedAnimal = null;

  /* depends from game version: 1-2 habitats         2-3 animals */
  public Tile {
    Objects.requireNonNull(leftHabitat, "leftHabitat can't be null");
    Objects.requireNonNull(rightHabitat, "rightHabitat can't be null");
    Objects.requireNonNull(animals, "Animals can't be null");
  }

  public final boolean isKeystone(){
    return leftHabitat.equals(rightHabitat);
  }



  /**
   * @param Animal token
   * @return true - it's possible, otherwise no
   * */
  // @Override
  // public final boolean canBePlaced(WildlifeType token) {
  //   Objects.requireNonNull(token, "token must not be null in HabitatTile.canBePlaced()");
  //   if (isOccupied()) {
  //     return false;
  //   }
  //   for (var authorisedAnimal : this.animals) {
  //     if (authorisedAnimal.equals(token)) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }


  // public final boolean placeAnimal(WildlifeType token) {
  //   Objects.requireNonNull(token, "animal must not be null in placeToken()");
  //   if (isOccupied()) {
  //     return false;     /* we don't place animal, and return false (it wasn't placed) */
  //   }
  //   if (!canBePlaced(token)) {
  //     return false;
  //   }
  //   this.placedAnimal = token;
  //   this.occupiedByAnimal = true;
  //   return this.occupiedByAnimal;    /* we placed animal */
  // }


  /* to improve later */
  private String habitatsAndAnimalsAsString() {
    var builder = new StringBuilder();
    var separator = ": (";
    builder.append(leftHabitat).append(" ");
    if (!this.isKeystone()) {
      builder.append(rightHabitat);
    }
    for (var animal : this.animals) {  /* two or three animals */
      builder.append(separator).append(animal);
      separator = ", ";
    }
    builder.append(")");
    // if (this.placedAnimal != null) {
    //   builder.append(" - ").append(this.placedAnimal);
    // }
    return builder.toString();
  }

  
  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append(habitatsAndAnimalsAsString());
    return builder.toString();
  }
}


