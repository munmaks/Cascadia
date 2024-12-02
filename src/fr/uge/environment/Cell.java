package fr.uge.environment;

public sealed interface Cell permits CellSquare, CellHexagonal {
  boolean isOccupied();
  boolean placeTile(Tile tileToPlace);
  Tile getTile();
  Coordinates getCoordinates();
  int getNumberOfNeighbors();
}
