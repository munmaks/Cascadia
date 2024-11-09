package fr.uge.environment;

import java.util.Objects;


public record HabitatTile(
      TileType[] habitats,       /* 1-2 habitats  depends from game version */
      WildlifeType[] animals,    /* 2-3 animals   depends from game version */
      int x,
      int y,
      int version   /* 1, 2 or 3 */
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
  
  private static boolean occupiedByAnimal;
  private static WildlifeToken placedAnimal;      /* it is initialised from occupyTile() */ 


  public HabitatTile {
    Objects.requireNonNull(habitats, "Habitats can't be null");
    Objects.requireNonNull(animals, "Animals can't be null");
    occupiedByAnimal = false;   /* idle */
  }

  public final WildlifeToken getPlacedAnimal() {
    return placedAnimal;
  }


  public final boolean isOccupied() {
    return occupiedByAnimal;
  }

  /** to improve later, need to check if there's nearby we have other tiles
   * because we can't place a tile in isolated place.
   * @return token 
   * */
  public final boolean canBePlaced(WildlifeToken token) {
    Objects.requireNonNull(token, "token must not be null in canBePlaced()");
    if (isOccupied()) {
      return false;
    }

    for (var authorisedAnimal : animals) {
      if (token.animal().equals(authorisedAnimal)) {
        return true;
      }
    }
    return false;
  }


  public final boolean placeAnimal(WildlifeToken animal) {
    Objects.requireNonNull(animal, "animal must not be null in placeToken()");
    if (isOccupied()) {
      return false;     /* we don't place animal, and return false (it wasn't placed) */
    }
    placedAnimal = animal;
    occupiedByAnimal = true;
    return occupiedByAnimal;    /* we placed animal */
  }

  /* to improve later */
  private final String habitatsAndAnimalsAsString() {

    var builder = new StringBuilder();
    builder.append("\n" + habitats[0] + " " + habitats[1] + ": ");  /* everytime only two habitats */

    var separator = "(";
    /* two or three animals */
    for (var animal : animals) {
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
  


  // for tests
//  public static void main(String[] args) {
//
//    var habitats = new TileType[] { TileType.MOUNTAIN, TileType.RIVER };
//    var animals = new WildlifeType[] { WildlifeType.BEAR, WildlifeType.ELK, WildlifeType.FOX };
//    var habitat = new HabitatTile(habitats, animals, -1, -1, 1);
//
//  }
}


