package fr.uge.environment;

import java.util.Objects;

public record StarterHabitatTile(
      KeystoneTile topTile,
      HabitatTile leftTile,
      HabitatTile rightTile
    ) implements Tile {

  /*
   * configStarterHabitatTile.txt
   * 5 starter habitat tiles:
   * Version 1 and 2 they look like:
   * 
   * X
   * Y Z
   *
   * Version - 3 looks like:
   *
   *   X
   * Y   Z
   *
   * X: Keystone tile
   * Y: Habitat tile with three animals.
   * Z: Habitat tile with two animals.
   * 
   * Source: game rules
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
  
  @Override
  public WildlifeToken getAnimal() {
    return null;
  }
}
