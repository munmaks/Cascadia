package fr.uge.environment;

public sealed interface Tile permits 
  KeystoneTile,
  HabitatTile,
  StarterHabitatTile,
  EmptyTile {
WildlifeToken getAnimal();
}
