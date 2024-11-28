package fr.uge.environment;

import java.util.Arrays;
import java.util.Objects;

public final class HabitatTile implements Tile {

  /*
   * configTilesWithTwoAnimals.txt
   * 45 Tiles : with two animals
   * River, Prairie : Bear, Salmon
   * 
   * configTilesWithThreeAnimals.txt
   * 15 Tiles : with three animals
   * Mountain, Wetland : Elk, Hawk, Fox
   * 
   * clockwise = dans le sens des aiguilles d'une montre
   * counterclockwise = dans le sens inverse des aiguilles d'une montre
   * */


  private final TileType[] habitats;       /* 1-2 habitats  depends from game version */
  private final WildlifeType[] animals;    /* 2-3 animals   depends from game version */
  private boolean occupiedByAnimal = false;  /* idle */
  private WildlifeToken placedAnimal = null;

  
  public HabitatTile(
    TileType[] habitats,       /* 1-2 habitats  depends from game version */
    WildlifeType[] animals     /* 2-3 animals   depends from game version */
  ) {
    this.habitats = Objects.requireNonNull(habitats, "Habitats can't be null");
    this.animals = Objects.requireNonNull(animals, "Animals can't be null");
  }

  @Override
  public final WildlifeToken getAnimal() {
    return placedAnimal;
  }

  public final boolean isOccupied() {
    return occupiedByAnimal;
  }

  /**
   * @param Animal token
   * @return true - it's possible, otherwise no
   * */
  @Override
  public final boolean canBePlaced(WildlifeToken token) {
    Objects.requireNonNull(token, "token must not be null in HabitatTile.canBePlaced()");
    if (isOccupied()) {
      return false;
    }
    for (var authorisedAnimal : animals) {
      if (authorisedAnimal.equals(token.animal())) {
        return true;
      }
    }
    return false;
  }


  public final boolean placeAnimal(WildlifeToken token) {
    Objects.requireNonNull(token, "animal must not be null in placeToken()");
    if (isOccupied()) {
      return false;     /* we don't place animal, and return false (it wasn't placed) */
    }
    if (!canBePlaced(token)) {
      return false;
    }
    this.placedAnimal = token;
    this.occupiedByAnimal = true;
    return occupiedByAnimal;    /* we placed animal */
  }


  /* to improve later */
  private String habitatsAndAnimalsAsString() {
    var builder = new StringBuilder();
    var separator = "";
    for (var habitat : habitats) {
      builder.append(separator).append(habitat);
      separator = " ";
    }
    builder.append(": ");
    separator = "(";

    for (var animal : animals) {  /* two or three animals */
      builder.append(separator).append(animal.toString());
      separator = ", ";
    }
    builder.append(")");
    if (placedAnimal != null) {
      builder.append(" ").append(placedAnimal);
    }
    return builder.toString();
  }

  
  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append(habitatsAndAnimalsAsString());
    return builder.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(habitats, animals);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof HabitatTile other
          && Arrays.equals(habitats, other.habitats)
          && Arrays.equals(animals, other.animals);
  }
}


