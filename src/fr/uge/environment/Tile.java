package fr.uge.environment;

public sealed interface Tile permits 
  KeystoneTile, 
  HabitatTile, 
  EmptyTile, 
  StarterHabitatTile {
  /* abstract methods */
  WildlifeToken getAnimal();
}
