package fr.uge.environment;

import java.util.Objects;

public record KeystoneTile(
      TileType tile,
      WildlifeType animal,
      Tile[] neighbors,
      int q,
      int r
    ) implements Tile {

  public KeystoneTile {
    Objects.requireNonNull(tile);
    Objects.requireNonNull(animal);
    Objects.requireNonNull(neighbors);
  }

  @Override
  public String toString() {
    return "\n" + tile + ": " + animal;
  }
}
