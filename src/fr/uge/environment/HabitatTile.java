package fr.uge.environment;

import java.util.List;
import java.util.Objects;
// import java.lang.StringBuilder;

public record HabitatTile(
      TileType firstHabitat,
      TileType secondHabitat,
      WildlifeToken firstAnimal,    
      WildlifeToken secondAnimal,   
      WildlifeToken thirdAnimal,    /* optional for some habitat tiles */
      List<Tile> neighbors,         /* list of neighbors */
      int x,
      int y
    ) implements Tile {

  private static boolean occupied;

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

  public HabitatTile {
    Objects.requireNonNull(firstHabitat, "Habitats can't be null");
    Objects.requireNonNull(secondHabitat, "Habitats can't be null");
    Objects.requireNonNull(firstAnimal, "Animals can't be null");
    Objects.requireNonNull(secondAnimal, "Animals can't be null");
    occupied = false;
  }
  
  
  private final String habitatsAndAnimalsAsString() {
    /* only two animals */
    if (thirdAnimal == null) {
      return "Habitats: " + firstHabitat + "and " + secondHabitat +
           "\nAnimals: " + firstAnimal + ", " + secondAnimal;
    }
    /* three animals */
    return "Habitats: " + firstHabitat + "and " + secondHabitat +
         "\nAnimals: " + firstAnimal + ", " + secondAnimal + ", " + thirdAnimal;
  }

  private final String neighborsAsString() {
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
    builder.append(neighborsAsString());
    return builder.toString();
  }
}
















