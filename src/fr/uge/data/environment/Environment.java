package fr.uge.data.environment;


import java.util.List;
import java.util.Map;
import java.util.Set;

public sealed interface Environment permits SquareEnvironment, HexagonalEnvironment {
  Cell getCell(Coordinates coordinates);

  List<Cell> getCells();

  boolean placeTile(Cell cell, Tile tile);

  boolean placeAnimal(Cell cell, WildlifeType token);

  Set<Coordinates> getPossibleCells();

  Cell getOneNeighbor(Cell cell, int direction);

  List<Cell> getNeighbors(Cell cell);

  boolean couldBePlacedWildlifeToken(WildlifeType token);

  Map<TileType, Integer> calculateTileScore();

  void printAllNeighbors(Coordinates coordinates);
}
