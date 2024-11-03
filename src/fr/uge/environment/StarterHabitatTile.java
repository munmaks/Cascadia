package fr.uge.environment;

import java.util.Objects;

public record StarterHabitatTile(
      KeystoneTile topTile,
      HabitatTile leftTile,
      HabitatTile rightTile,
      int version
    ) implements Tile {

  /*
   * configStarterHabitatTile.txt
   * 5 starter habitat tiles in version - 3 look like:
   *
   *   X
   * Y   Z
   *
   * And in version 1 and 2 they look like:
   * 
   * X
   * Y Z
   *
   * X: Keystone tile
   * Y: Habitat tile with three animals.
   * Z: Habitat tile with two animals.
   * 
   * Source: from game rules
   * */

  public StarterHabitatTile {
    Objects.requireNonNull(topTile);
    Objects.requireNonNull(leftTile);
    Objects.requireNonNull(rightTile);
  }


  @Override
  public String toString() {
    return topTile.toString() +
           leftTile.toString() +
           rightTile.toString();
  }
}
