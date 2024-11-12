package fr.uge.environment;

import java.util.Objects;


public record HabitatTile(
      TileType[] habitats,       /* 1-2 habitats  depends from game version */
      WildlifeType[] animals     /* 2-3 animals   depends from game version */
    ) implements Tile {

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


  private static boolean occupiedByAnimal = false;  /* idle */
  private static WildlifeToken placedAnimal = null;

  
  public HabitatTile {
    Objects.requireNonNull(habitats, "Habitats can't be null");
    Objects.requireNonNull(animals, "Animals can't be null");
  }

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
  public final boolean canBePlaced(WildlifeToken token) {
    Objects.requireNonNull(token, "token must not be null in canBePlaced()");
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
    placedAnimal = token;
    occupiedByAnimal = true;
    return occupiedByAnimal;    /* we placed animal */
  }


  /* to improve later */
  private final String habitatsAndAnimalsAsString() {
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
    return builder.toString();
  }

  
  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append(habitatsAndAnimalsAsString());
    return builder.toString();
  }

}


