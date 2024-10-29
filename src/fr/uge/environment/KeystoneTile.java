package fr.uge.environment;

import java.util.Objects;

public record KeystoneTile(
      Tile tile,
      WildlifeToken animal,
      int x,
      int y
    ) implements Tile {

  public KeystoneTile {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(animal);
  }

  
  @Override
  public String toString() {
    return "Keystone Tile: tile - " + tile + " animal: " + animal;
  }
}
