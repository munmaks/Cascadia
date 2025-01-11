package fr.uge.environment;


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

  // private static final int SQUARE_NUMBER_OF_NEIGHBORS = 4;

  public SquareEnvironment() {
    /*
     * class for all check and valid parameters to stop checking everytime THE SAME
     * THING
     */
    // initializeGrid(version);
    // this.cellsMap = new HashMap<>();
    // this.cellsSet = new HashSet<>();
  }

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
    return this.cellsMap.computeIfAbsent(currCoordinates, /* neighbor coordinates */
        SquareCell::new /* create new cell if not exists */
    );
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
        // .filter(Objects::nonNull) // no need to filter, because we create new cell if
        // not exists
        .collect(Collectors.toUnmodifiableList());
  }

  private void addNeighborsInSet(Cell cell) {
    // don't need to check again if cell is null, because we did it in placeTile()
    // Objects.requireNonNull(cell, "cell can't be null in addNeighborsInSet()");
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
   */
  @Override
  public final boolean canBePlacedWildlifeToken(WildlifeType token) {
    Objects.requireNonNull(token);
    for (var cell : this.cellsMap.values()) {
      if (cell.canBePlaced(token)) { /* found, no need to continue */
        return true;
      }
    }
    return false;
  }

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
   * Map.computeIfAbsent() - if the specified key is not already associated with a
   * value (or is mapped to null), attempts to compute its value using the given
   * mapping function and enters it into this map unless null.
   */
  @Override
  public final Cell getCell(Coordinates coordinates) {
    Objects.requireNonNull(coordinates, "Coordinates can't be null in getCell()");
    return this.cellsMap.computeIfAbsent(coordinates, SquareCell::new);
  }

  /* gets all tiles in the environment */
  @Override
  public final List<Cell> getCells() {
    // return List.copyOf(this.cellsMap.values());
    return this.cellsMap.values().stream().collect(Collectors.toUnmodifiableList());
  }

  @Override
  public final Set<Coordinates> getPossibleCells() {
    // return Set.copyOf(this.cellsSet);
    return Collections.unmodifiableSet(this.cellsSet);
  }

  @Override
  public void printAllNeighbors(Coordinates coordinates) {
    Objects.requireNonNull(coordinates, "Coordinates can't be null in printAllNeighbors()");

    getNeighbors(this.cellsMap.get(coordinates)).stream()
        .map(neighbor -> this.cellsMap.get(neighbor.getCoordinates())) // Map to the cell
        .filter(Objects::nonNull) // Filter out null cells
        .filter(cell -> cell.getTile() != null) // Filter cells with null tiles
        .map(cell -> String.format("%8s  %8s  %6s", coordinates, cell.getTile(),
            (cell.getAnimal() == null) ? "" : cell.getAnimal())) // Format the output string
        .forEach(System.out::println); // Print each formatted string
  }

  /*
   * We look at all cells in set and then we show them
   */
  // @Override
  // public void printAllNeighbors(Coordinates coordinates) {
  // Objects.requireNonNull(coordinates, "Coordinates can't be null in
  // printAllNeighbors()");
  // var neighbors = getNeighbors(this.cellsMap.get(coordinates));
  // for (var neighbor : neighbors) {
  // var builder = new StringBuilder();
  // var cell = this.cellsMap.getOrDefault(neighbor.getCoordinates(), null);
  // if (cell != null) {
  // var tile = cell.getTile();
  // if (tile == null) { continue; }
  // builder.append(coordinates).append(" - ").append(tile.toString()).append(" -
  // ").append(cell.getAnimal());
  // System.out.println(builder);
  // }
  // }
  // }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    var allCells = this.cellsMap.values();
    for (var cell : allCells) {
      builder.append(cell.toString()).append("\n");
    }
    return builder.toString();
  }

  /*
   * private int dfs(TileType tileType, Cell cell, Set<Cell> visited) {
   * Objects.requireNonNull(tileType, "TileType can't be null in dfs()");
   * Objects.requireNonNull(cell, "Cell can't be null in dfs()"); if
   * (cell.getTile() == null || visited.contains(cell)) { return 0; }
   * visited.add(cell); if (!tileType.equals(cell.getTile().leftHabitat())) {
   * return 0; } var score = 1; var neighbors = getNeighbors(cell); for (var
   * neighbor : neighbors) { score += dfs(tileType, neighbor, visited); } return
   * score; }
   */

  private int dfs(TileType tileType, Cell cell, Set<Cell> visited) {
    /*
     * we don't need Objects.requireNonNull(...) because it's internal method and we
     * know that tileType, cell and visited are not null
     */
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

  private int calculateScoreTileType(TileType tileType) {
    return this.cellsMap.values().stream().mapToInt(cell -> dfs(tileType, cell, new HashSet<>()))
        .max().orElse(0);
  }

  @Override
  public final Map<TileType, Integer> calculateTileScore() {
    return Collections.unmodifiableMap(Arrays.stream(TileType.values())
        .collect(Collectors.toMap(tileType -> tileType, tileType -> calculateScoreTileType(tileType) // this::calculateScoreTileType
        )));
  }

  /*
   * @Override public final Map<TileType, Integer> calculateTileScore(){ return
   * Arrays.stream(TileType.values()) .collect(Collectors.toMap( tileType ->
   * tileType, tileType -> calculateScoreTileType(tileType) //
   * this::calculateScoreTileType )); }
   */

  /*
   * @Override public final Map<TileType, Integer> calculateTileScore() { var
   * tileTypes = TileType.values(); var allTiles = this.cellsMap.values(); var map
   * = new HashMap<TileType, Integer>(); for (var tileType : tileTypes) { var
   * score = 0; var visited = new HashSet<Cell>(); for (var cell : allTiles) {
   * score = Math.max(score, dfs(tileType, cell, visited)); } map.put(tileType,
   * score); } return map; }
   */

}
