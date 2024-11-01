package fr.uge.environment;

import java.util.Objects;

public record StarterHabitatTile(
      KeystoneTile topTile,
      HabitatTile leftTile,
      HabitatTile rightTile
    ) implements Tile {

  /*
   * configStarterHabitatTile.txt
   * 5 starter habitat tiles looking like:
   *
   *   X
   * Y   Z
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
