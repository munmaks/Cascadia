package fr.uge.environment;

import java.util.Objects;


public record HabitatTile(
      TileType[] habitats,       /* 1-2 habitats  depends from game version */
      WildlifeType[] animals,    /* 2-3 animals   depends from game version */
      Tile[] neighbors,          /* 4-6 neighbors depends from game version */
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
  
  private static boolean occupied;
  private static WildlifeToken placedAnimal;      /* it is initialised from occupyTile() */


  /* if it's last version - 3, so there 6 neighbors, otherwise it's only 4 neighbors */
  /* private final int nbRotations = (version == 3) ? (6) : (4); */
  private static int maxRotations;


  /* for version 1, we needn't rotation
   * 
   * for version 2:
   * 1 turn clockwise = 90 degrees, 4 turns clockwise = 360 == 0 degrees
   * 1 turn counter clockwise = 270 degrees, 4 turns clockwise 360 == 0 degrees
   *
   * for version 3:
   * 1 turn clockwise = 60 degrees, 6 turns clockwise = 360 == 0 degrees
   * 1 turn counter clockwise = 300 degrees, 6 turns clockwise 360 == 0 degrees
   * */
  private static int currentRotation;     

  public HabitatTile {
    Objects.requireNonNull(habitats, "Habitats can't be null");
    Objects.requireNonNull(animals, "Animals can't be null");
    Objects.requireNonNull(neighbors, "Neighbors can't be null");
    occupied = false;   /* idle */
    currentRotation = 0;
    maxRotations = (neighbors.length == 6) ? (6) : (4);
  }



  public final WildlifeToken getPlacedAnimal() {
    return placedAnimal;
  }


  public final boolean isOccupied() {
    return occupied;
  }


  public final void turnСlockwise() {
    if (version == 1) {
      return;
    }
    currentRotation = (currentRotation + 1) % maxRotations;
  }


  public final void turnСounterСlockwise() {
    if (version == 1) {
      return;
    }
    currentRotation = (currentRotation - 1 + maxRotations) % maxRotations;
  }


  public final int getRotation() {
    if (version == 1) {
      return 0;
    }
    return currentRotation;
  }


  /* to improve later, need to check if there's nearby we have other tiles
   * because we can't place a tile in isolated place.
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


  /** to improve: update neighbors of tile and `neighbors for neighbors` */
  public final boolean placeAnimal(WildlifeToken animal) {
    Objects.requireNonNull(animal, "animal must not be null in placeToken()");
    if (isOccupied()) {
      return false;     /* we don't place animal, and return false (it wasn't placed) */
    }
    placedAnimal = animal;
    occupied = true;
    return occupied;    /* we placed animal */
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


  private final String neighborsAsString() {
    if (neighbors.length == 0){
      return "\n";
    }
    var builder = new StringBuilder();
    var separator = "";

    builder.append("\nNeighbors: ");
    for (var neighbor : neighbors) {
      builder.append(separator).append(neighbor.toString());
      separator = ", ";
    }
    builder.append("\n");
    return builder.toString();
  }

  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append(habitatsAndAnimalsAsString());
    // builder.append(neighborsAsString());
    return builder.toString();
  }
  


  // for tests
//  public static void main(String[] args) {
//
//    var habitats = new TileType[] { TileType.MOUNTAIN, TileType.RIVER };
//    var animals = new WildlifeType[] { WildlifeType.BEAR, WildlifeType.ELK, WildlifeType.FOX };
//    var habitat = new HabitatTile(habitats, animals, new Tile[6], -1, -1, 1);
//
//  }
}


