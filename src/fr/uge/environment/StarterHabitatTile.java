package fr.uge.environment;

import java.util.Objects;

public final class StarterHabitatTile implements Tile {

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
  private final KeystoneTile topTile;           /* we don't know if it's habitat tile or keystone, depends from game version */
  private final HabitatTile leftTile;
  private final HabitatTile rightTile;

  public StarterHabitatTile(
    KeystoneTile topTile,
    HabitatTile leftTile,
    HabitatTile rightTile
  ) {
    this.topTile = Objects.requireNonNull(topTile);
    this.leftTile = Objects.requireNonNull(leftTile);
    this.rightTile = Objects.requireNonNull(rightTile);
  }


  public KeystoneTile topTile() {
    return topTile;
  }

  public HabitatTile leftTile() {
    return leftTile;
  }

  public HabitatTile rightTile() {
    return rightTile;
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

  @Override
  public boolean canBePlaced(WildlifeToken token) {
    return false;
  }
}
