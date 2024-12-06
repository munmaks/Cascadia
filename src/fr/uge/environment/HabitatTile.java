package fr.uge.environment;

import java.util.Arrays;
import java.util.Objects;

public final class HabitatTile implements Tile {
  /*
   * configTilesWithTwoAnimals.txt
   * 45 Tiles : with two animals
   * RIVER, PRAIRIE : BEAR, SALMON
   * 
   * configTilesWithThreeAnimals.txt
   * 15 Tiles : with three animals
   * MOUNTAIN, WETLAND : ELK, HAWK, FOX
   **/
  private final TileType[] habitats;
  private final WildlifeType[] animals;
  private boolean occupiedByAnimal = false;  /* idle */
  private WildlifeType placedAnimal = null;

  /* depends from game version: 1-2 habitats         2-3 animals */
  public HabitatTile(TileType[] habitats, WildlifeType[] animals) {
    this.habitats = Objects.requireNonNull(habitats, "Habitats can't be null");
    this.animals = Objects.requireNonNull(animals, "Animals can't be null");
  }

  @Override
  public final WildlifeType getAnimal() {
    return this.placedAnimal;
  }

  private boolean isOccupied() {
    return this.occupiedByAnimal;
  }

  /**
   * @param Animal token
   * @return true - it's possible, otherwise no
   * */
  @Override
  public final boolean canBePlaced(WildlifeType token) {
    Objects.requireNonNull(token, "token must not be null in HabitatTile.canBePlaced()");
    if (isOccupied()) {
      return false;
    }
    for (var authorisedAnimal : this.animals) {
      if (authorisedAnimal.equals(token)) {
        return true;
      }
    }
    return false;
  }


  public final boolean placeAnimal(WildlifeType token) {
    Objects.requireNonNull(token, "animal must not be null in placeToken()");
    if (isOccupied()) {
      return false;     /* we don't place animal, and return false (it wasn't placed) */
    }
    if (!canBePlaced(token)) {
      return false;
    }
    this.placedAnimal = token;
    this.occupiedByAnimal = true;
    return this.occupiedByAnimal;    /* we placed animal */
  }


  /* to improve later */
  private String habitatsAndAnimalsAsString() {
    var builder = new StringBuilder();
    var separator = "";
    for (var habitat : this.habitats) {
      builder.append(separator).append(habitat);
      separator = " ";
    }
    builder.append(": ");
    separator = "(";
    for (var animal : this.animals) {  /* two or three animals */
      builder.append(separator).append(animal);
      separator = ", ";
    }
    builder.append(")");
    if (this.placedAnimal != null) {
      builder.append(" - ").append(this.placedAnimal);
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
    return Objects.hash(this.habitats, this.animals);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof HabitatTile other
          && Arrays.equals(this.habitats, other.habitats)
          && Arrays.equals(this.animals, other.animals);
  }
}


