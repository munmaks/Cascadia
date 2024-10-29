package fr.uge.environment;

import java.util.Objects;

public record StarterHabitatTile(
      KeystoneTile topTile,
      HabitatTile leftTile,
      HabitatTile rightTile
    ) implements Tile {

  /*
   * configStarterHabitatTile.txt
   * 15 Tiles : with three animals
   * Mountain, Wetland : Elk, Hawk, Fox
   * */
  // five entities
  public StarterHabitatTile {
    Objects.requireNonNull(topTile);
    Objects.requireNonNull(leftTile);
    Objects.requireNonNull(rightTile);
  }

  
  @Override
  public String toString() {
    return topTile.toString() + "\n" +
           leftTile.toString() + "\n" +
           rightTile.toString() + "\n";
  }
}
