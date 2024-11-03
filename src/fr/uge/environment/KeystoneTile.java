package fr.uge.environment;

import java.util.Objects;

public record KeystoneTile(
      TileType tile,
      WildlifeType animal,
      Tile[] neighbors,
      int x,
      int y,
      int version   /* 1, 2 or 3 */
    ) implements Tile {

  public KeystoneTile {
    if (version < 1 || version > 3) {
      throw new IllegalArgumentException();
    }

    Objects.requireNonNull(tile);
    Objects.requireNonNull(animal);
    Objects.requireNonNull(neighbors);
  }


  @Override
  public String toString() {
    return "\n" + tile + ": " + animal;
  }

}
