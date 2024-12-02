package fr.uge.environment;

public sealed interface Tile permits 
  KeystoneTile, 
  HabitatTile, 
  EmptyTile {
  /* abstract methods */
  WildlifeToken getAnimal();
  boolean canBePlaced(WildlifeToken token);
}
