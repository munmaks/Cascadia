package fr.uge.environment;

import java.util.List;
import java.util.Objects;
// import java.lang.StringBuilder;

public record HabitatTile(List<Tile> habitats, List<WildlifeToken> tokens, int x, int y) implements Tile {

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
    Objects.requireNonNull(habitats, "Habitats can't be null");
    Objects.requireNonNull(tokens, "Tokens can't be null");
    occupied = false;
  }


  
  
  private final String habitatsAsString() {
    var builder = new StringBuilder();
    var separator = "";
    builder.append("Habitats: ");
    for (var habitat : habitats) {
      builder.append(separator).append(habitat.toString());
      separator = ", ";
    }
    builder.append("\n");
    return builder.toString();
  }
  
 
  private final String tokensAsString() {
    var builder = new StringBuilder();
    var separator = "";

    builder.append("\nTokens: ");
    for (var token : tokens) {
      builder.append(separator).append(token.toString());
      separator = ", ";
    }
    builder.append("\n");
    return builder.toString();
  }

  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append(habitatsAsString());
    builder.append(tokensAsString());
    return builder.toString();
  }
}
















