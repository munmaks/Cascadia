package fr.uge.environment;

import java.util.Objects;
import java.util.Set;

public record Tile (
    TileType firstHabitat,
    TileType secondHabitat,
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
  public Tile {
    Objects.requireNonNull(firstHabitat, "firstHabitat can't be null");
    Objects.requireNonNull(secondHabitat, "secondHabitat can't be null");
    Objects.requireNonNull(animals, "Animals can't be null");
  }

  public final boolean isKeystone(){
    return firstHabitat.equals(secondHabitat);
  }


  /* to improve later */
  private String habitatsAndAnimalsAsString() {
    var builder = new StringBuilder();
    var separator = ": (";
    builder.append(firstHabitat).append(" ");
    if (!this.isKeystone()) {
      builder.append(secondHabitat);
    }
    for (var animal : this.animals) {  /* two or three animals */
      builder.append(separator).append(animal);
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


