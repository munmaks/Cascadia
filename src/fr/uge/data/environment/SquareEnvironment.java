package fr.uge.data.environment;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Player's environment of all tiles and placed wildlife tokens
 */
public final class SquareEnvironment implements Environment {

  /* environment of all placed tiles by one player */
  private final HashSet<Coordinates> cellsSet = new HashSet<>();

  /* environment of all placed tiles by one player */
  private final HashMap<Coordinates, Cell> cellsMap = new HashMap<>();

  /**
   * <b>(x, y)</b><br>
   * {-1, 0} left <br>
   * {0, -1} down <br>
   * {1, 0} right <br>
   * {0, 1} up <br>
   */
  private static final int[][] SQUARE_DIRECTION_DIFFERENCES = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};

  /**
   * Create a new environment for a player.
   */
  public SquareEnvironment() {}

  /**
   * Get the coordinates of a neighbor cell.
   * 
   * @param cell      The cell for which to retrieve a neighbor.
   * @param direction The direction of the neighbor.
   * @return The coordinates of the neighbor cell.
   */
  private Coordinates getNeighborCoordinates(Cell cell, int direction) {
    var diff = SquareEnvironment.SQUARE_DIRECTION_DIFFERENCES[direction];
    return new Coordinates(cell.getCoordinates().y() + diff[1], /* neighborRow */
        cell.getCoordinates().x() + diff[0] /* neighborCol */
    );
  }

  /**
   * Map.computeIfAbsent() - if the specified key is not already associated with a
   * value (or is mapped to null), attempts to compute its value using the given
   * mapping function and enters it into this map unless null.
   * 
   * @param cell      The cell for which to retrieve a neighbor.
   * @param direction The direction of the neighbor.
   * @return The neighbor cell.
   */
  @Override
  public Cell getOneNeighbor(Cell cell, int direction) {
    Objects.requireNonNull(cell, "Cell can't be null in getOneNeighbor()");
    var currCoordinates = getNeighborCoordinates(cell, direction);
    if (!cell.isOccupiedByTile()) {
      return new SquareCell(currCoordinates); // without adding into hashmap
    }
    /* neighbor coordinates, create new cell if not exists */
    return this.cellsMap.computeIfAbsent(currCoordinates, SquareCell::new);
  }

  /**
   * <b>gets a copy of list of all valid neighbors for a given hex cell.</b>
   * 
   * @param cell The hex cell for which to retrieve neighbors.
   * @return An immutable list of neighboring cells.
   */
  @Override
  public List<Cell> getNeighbors(Cell cell) {
    Objects.requireNonNull(cell, "Cell can't be null in getNeighbors()");
    return IntStream.range(0, SquareEnvironment.SQUARE_DIRECTION_DIFFERENCES.length)
        .mapToObj(direction -> getOneNeighbor(cell, direction))
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Add all neighbors of a cell to the set of possible cells.
   * 
   * @param cell The cell for which to add neighbors to the set.
   */
  private void addNeighborsInSet(Cell cell) {
    var neighbors = getNeighbors(cell);
    for (var neighbor : neighbors) {
      var currCoordinates = neighbor.getCoordinates();
      if (!this.cellsMap.get(currCoordinates).isOccupiedByTile()) {
        this.cellsSet.add(currCoordinates);
      }
    }
  }

  /**
   * places a wildlife token on a tile within the environment
   * 
   * @param cell The cell on which to place the tile.
   * @param tile The tile to place.
   * @return true if the tile was placed, false otherwise.
   */
  @Override
  public final boolean placeTile(Cell cell, Tile tile) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(tile);
    if (cell.placeTile(tile)) {
      addNeighborsInSet(cell); /* placed and added to all player's occupied cells */
      var currCoordinates = cell.getCoordinates();
      if (this.cellsSet.contains(currCoordinates)) { /* remove from set if exists */
        this.cellsSet.remove(currCoordinates);
      }
      return true;
    }
    System.err.println("Can't place tile on " + cell.toString());
    return false;
  }

  /**
   * Determine possibility to placed a wildlife token on player's environment.
   * 
   * @param token The wildlife token to place.
   * @return true if the token was placed, false otherwise.
   */
  @Override
  public final boolean couldBePlacedWildlifeToken(WildlifeType token) {
    Objects.requireNonNull(token);
    for (var cell : this.cellsMap.values()) {
      if (cell.couldBePlaced(token)) { /* found, no need to continue */
        return true;
      }
    }
    return false;
  }

  /**
   * places a wildlife token on a tile within the environment
   * 
   * @param cell  The cell on which to place the wildlife token.
   * @param token The wildlife token to place.
   * @return true if the token was placed, false otherwise.
   */
  @Override
  public final boolean placeAnimal(Cell cell, WildlifeType token) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(token);
    if (!cell.isOccupiedByTile()) {
      return false;
    }
    // return cell.placeAnimal(token);
    return cell.getTile() != null && cell.placeAnimal(token);
  }

  /**
   * Determine possibility to placed a wildlife token on player's environment.
   * 
   * @param token The wildlife token to place.
   * @return true if the token was placed, false otherwise.
   */
  @Override
  public final Cell getCell(Coordinates coordinates) {
    /*
     * Map.computeIfAbsent() - if the specified key is not already associated with a
     * value (or is mapped to null), attempts to compute its value using the given
     * mapping function and enters it into this map unless null.
     */
    Objects.requireNonNull(coordinates, "Coordinates can't be null in getCell()");
    return this.cellsMap.computeIfAbsent(coordinates, SquareCell::new);
  }

  /**
   * Get all cells of the environment.
   * 
   * @return An immutable list of all cells.
   */
  @Override
  public final List<Cell> getCells() {
    return this.cellsMap.values().stream().collect(Collectors.toList());
  }

  @Override
  public final Set<Coordinates> getPossibleCells() {
    return Collections.unmodifiableSet(this.cellsSet);
  }

  /**
   * Print all neighbors of a cell.
   * 
   * @param coordinates The coordinates of the cell for which to print neighbors.
   */
  @Override
  public void printAllNeighbors(Coordinates coordinates) {
    Objects.requireNonNull(coordinates, "Coordinates can't be null in printAllNeighbors()");

    getNeighbors(this.cellsMap.get(coordinates)).stream()
        .map(neighbor -> this.cellsMap.get(neighbor.getCoordinates())).filter(Objects::nonNull)
        .filter(cell -> cell.getTile() != null).map(cell -> String.format("%8s  %8s  %6s",
            coordinates, cell.getTile(), (cell.getAnimal() == null) ? "" : cell.getAnimal()))
        .forEach(System.out::println);
  }

  /**
   * Get the string representation of the environment.
   * 
   * @return The string representation of the environment.
   */
  @Override
  public String toString() {
    var builder = new StringBuilder();
    var allCells = this.cellsMap.values();
    for (var cell : allCells) {
      builder.append(cell.toString()).append("\n");
    }
    return builder.toString();
  }

  /**
   * Depth-first search to calculate the score of a tile type in the environment.
   * 
   * @param tileType
   * @param cell
   * @param visited
   * @return
   */
  private int dfs(TileType tileType, Cell cell, Set<Cell> visited) {
    if (cell.getTile() == null || visited.contains(cell)) {
      return 0;
    } /* no tile or already visited */
    visited.add(cell);
    if (!tileType.equals(cell.getTile().firstHabitat())) {
      return 0;
    } /* not the same tile */
    return 1 + getNeighbors(cell).stream().mapToInt(neighbor -> dfs(tileType, neighbor, visited))
        .max().orElse(0);
  }

  /**
   * Calculate the score of a tile type in the environment.
   * 
   * @param tileType The tile type for which to calculate the score.
   * @return The score of the tile type.
   */
  private int calculateScoreTileType(TileType tileType) {
    return this.cellsMap.values().stream().mapToInt(cell -> dfs(tileType, cell, new HashSet<>()))
        .max().orElse(0);
  }

  /**
   * Calculate the score of each tile type in the environment.
   * 
   * @return An immutable map of tile types and their scores.
   */
  @Override
  public final Map<TileType, Integer> calculateTileScore() {
    return Collections.unmodifiableMap(Arrays.stream(TileType.values())
        .collect(Collectors.toMap(tileType -> tileType, tileType -> calculateScoreTileType(tileType) // this::calculateScoreTileType
        )));
  }

}
