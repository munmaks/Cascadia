package fr.uge.environment;

import java.util.Objects;


public record HabitatTile(
      TileType[] habitats,       // 2 habitats
      WildlifeType[] animals,    // 2 or 3 animals
      Tile[] neighbors,          // 6 neighbors
      int q,
      int r
    ) implements Tile {

  /*
   * configTilesWithTwoAnimals.txt
   * 45 Tiles : with two animals
   * River, Prairie : Bear, Salmon
   * 
   * 
   * configTilesWithThreeAnimals.txt
   * 15 Tiles : with three animals
   * Mountain, Wetland : Elk, Hawk, Fox
   * 
   */

  private static boolean occupied;
  private static WildlifeToken placedAnimal;      /* it is initialised from occupyTile() */

  public HabitatTile {
    Objects.requireNonNull(habitats, "Habitats can't be null");
    Objects.requireNonNull(animals, "Animals can't be null");
    Objects.requireNonNull(neighbors, "Neighbors can't be null");
    occupied = false;
  }


  public final boolean isOccupied() {
    return occupied;
  }

  public final boolean occupyTile(WildlifeToken animal) {
    if (isOccupied()) {
      return false;     /* we don't place animal, and return false (it wasn't placed) */
    }
    placedAnimal = animal;
    occupied = true;
    return occupied;    /* we placed animal */
  }

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
  
//  public static void main(String[] args) {
//    ;
//  }
}
















