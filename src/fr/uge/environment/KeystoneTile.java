package fr.uge.environment;

import java.util.Objects;

public record KeystoneTile(TileType tile, WildlifeToken animal, int x, int y) implements Tile {

  public KeystoneTile {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(animal);
  }

  @Override
  public String toString() {
    return tile + " -->" + animal;
  }
}
