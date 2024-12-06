package fr.uge.environment;

public sealed interface Tile permits KeystoneTile, HabitatTile, EmptyTile {
  /* abstract methods */
  WildlifeType getAnimal();
  boolean canBePlaced(WildlifeType token);
}
