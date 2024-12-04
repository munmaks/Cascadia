package fr.uge.environment;

import java.util.List;
import java.util.Set;

public sealed interface Environment permits EnvironmentSquare, EnvironmentHexagonal {
    Cell getCellOrCreate(Coordinates coordinates);
    boolean placeTile(Cell cell, Tile tile);
    boolean placeAnimal(Cell cell, WildlifeType token);
    List<Cell> getCells();
    Set<Coordinates> getPossibleCells();
    Cell getOneNeighbor(Cell cell, int direction);
    List<Cell> getNeighbors(Cell cell);
    boolean canBePlacedWildlifeToken(WildlifeType token);
    void printAllNeighbors(Coordinates coordinates);    // for terminal version, to think if we can do better
}
