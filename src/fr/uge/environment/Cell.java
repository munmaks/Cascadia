package fr.uge.environment;

public sealed interface Cell permits SquareCell, HexagonalCell {
  Coordinates getCoordinates();

  Tile getTile();

  WildlifeType getAnimal();

  boolean placeTile(Tile tileToPlace);

  boolean canBePlaced(WildlifeType token);

  boolean placeAnimal(WildlifeType token);

  boolean isOccupiedByTile();

  int getNumberOfNeighbors();
  // boolean isOccupiedByAnimal(); // not needed
}
