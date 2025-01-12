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
public final class HexagonalEnvironment implements Environment {

  /* 100 - 196 total tiles to show in player's environment */
  // private final Cell[][] grid = new Cell[Constants.MAX_ROW][Constants.MAX_COL];

  /* environment of all placed tiles by one player */
  private final HashSet<Coordinates> cellsSet;

  /* environment of all placed tiles by one player */
  private final HashMap<Coordinates, Cell> cellsMap;

  /**
   * <b> Direction offsets based on "odd-r" layout<br>
   * (x, y)</b><br>
   * <br>
   * even rows<br>
   * {<br>
   * (-1, -1), (-1, 0), (-1, 1), left habitat: (left up, left, left down)<br>
   * ( 0, 1), ( 1, 0), ( 0, -1) right habitat: (right down, right, right up)<br>
   * }<br>
   * <br>
   * odd rows<br>
   * {<br>
   * ( 0, -1), (-1, 0), ( 0, 1), left habitat: (left up, left, left down)<br>
   * ( 1, 1), ( 1, 0), ( 1, -1) right habitat: (right down, right, right up)<br>
   * }<br>
   * <br>
   * Source: https://www.redblobgames.com/grids/hexagons/#neighbors-offset </b>
   */
  public static final int[][][] HEXAGONE_DIRECTION_DIFFERENCES = {
      {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 0}, {0, -1}},
      {{0, -1}, {-1, 0}, {0, 1}, {1, 1}, {1, 0}, {1, -1}}};

  public HexagonalEnvironment() {
    /*
     * class for all check and valid parameters to stop checking everytime THE SAME
     * THING
     */
    // initializeGrid(version);
    this.cellsMap = new HashMap<>();
    this.cellsSet = new HashSet<>();
  }

  private Coordinates getNeighborCoordinates(Cell cell, int direction) {
    var parity = cell.getCoordinates().y() & 1; /* parity: 0 - even rows, 1 - odd rows */
    var diff = HexagonalEnvironment.HEXAGONE_DIRECTION_DIFFERENCES[parity][direction];

    return new Coordinates(cell.getCoordinates().y() + diff[1], /* neighborRow */
        cell.getCoordinates().x() + diff[0] /* neighborCol */
    );
  }

  @Override
  public Cell getOneNeighbor(Cell cell, int direction) {
    Objects.requireNonNull(cell);
    var currCoordinates = getNeighborCoordinates(cell, direction);
    if (!cell.isOccupiedByTile()) {
      return new HexagonalCell(currCoordinates); // without adding into hashmap
    }
    return this.cellsMap.computeIfAbsent(currCoordinates, /* neighbor coordinates */
        HexagonalCell::new /* create new cell if not exists */
    );

    // var neighbor = new HexagonalCell(currCoordinates);
    // if (!cellsMap.containsKey(currCoordinates)) { /* insert in hashmap `cellsMap`
    // and return new coordinates */
    // cellsMap.put(currCoordinates, neighbor);
    // }
    // return neighbor;
  }

  /**
   * <b>gets a copy of list of all valid neighbors for a given hex cell.</b>
   * 
   * @param cell The hex cell for which to retrieve neighbors.
   * @return An immutable list of neighboring cells.
   */
  // @Override
  // public List<Cell> getNeighbors(Cell cell) {
  // var neighbors = new ArrayList<Cell>();
  // for (var direction = 0; direction < Constants.MAX_ROTATIONS; ++direction) {
  // var neighbor = getOneNeighbor(cell, direction);
  // if (null != neighbor) {
  // neighbors.add(neighbor);
  // }
  // }
  // return List.copyOf(neighbors);
  // }

  /**
   * search in the different direction of the hexagonal tile the neighbor of the given tile and return it in a list
   *
   * @param cell the cell that we want the neighbor
   * @return a list of neighbor of the given cell
   */
  @Override
  public List<Cell> getNeighbors(Cell cell) {
    Objects.requireNonNull(cell, "Cell can't be null in getNeighbors()");
    return IntStream
        .range(0, HexagonalEnvironment.HEXAGONE_DIRECTION_DIFFERENCES[cell.getCoordinates().y() & 1].length)
        .mapToObj(direction -> getOneNeighbor(cell, direction))
        .toList();
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

  /* places a wildlife token on a tile within the environment */
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
    // System.err.println("Can't place tile on " + cell.toString());
    return false;
  }

  /**
   * Determine possibility to placed a wildlife token on player's environment.
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

  @Override
  public final boolean placeAnimal(Cell cell, WildlifeType token) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(token);
    if (!cell.isOccupiedByTile()) {
      return false;
    }
    return cell.getTile() != null && cell.placeAnimal(token);
  }

  /* DEPRECATED, DONT FORGET */
  /**
   * Determine possibility to placed a wildlife token on player's environment.
   * Map.computeIfAbsent() - if the specified key is not already associated with a
   * value (or is mapped to null), attempts to compute its value using the given
   * mapping function and enters it into this map unless null.
   */
  @Override
  public final Cell getCell(Coordinates coordinates) {
    Objects.requireNonNull(coordinates, "Coordinates can't be null in getCell()");
    return this.cellsMap.computeIfAbsent(coordinates, HexagonalCell::new);
  }

  // public final List<Cell> getCellsWithToken(WildlifeToken token){
  // Objects.requireNonNull(token);
  // var list = new ArrayList<Cell>();
  // for (var tile : cells) {
  // if (tile.)
  // }
  // return List.copyOf(list);
  // }

  /* gets all tiles in the environment */
  @Override
  public final List<Cell> getCells() { return List.copyOf(this.cellsMap.values()); }

  @Override
  public final Set<Coordinates> getPossibleCells() {
    // return Set.copyOf(this.cellsSet);
    return Collections.unmodifiableSet(this.cellsSet);
  }

  // public final Set<Coordinates> getPossibleCoordinates() {
  // var set = new HashSet<Coordinates>();

  // for (var cell : this.cellsSet){
  // var neighbors = getNeighborsCells(cell);
  // for (var neighbor : neighbors){
  // if (!neighbor.isOccupied()) {
  // set.add(neighbor.getCoordinates());
  // }
  // }
  // }
  // return Set.copyOf(set);
  // }

  /**
   * To improve and not to use in hexagonal version Be aware, need to update
   * interface and delete this methode from it
   */
  @Override
  public void printAllNeighbors(Coordinates coordinates) {
    Objects.requireNonNull(coordinates);
    System.err.println("Not implemented yet, HexagonalEnvironment.printAllNeighbors()");
    // var neighbors = getNeighbors(cellsMap.get(coordinates));
    // for (var neighbor : neighbors) {
    // var cell = cellsMap.get(neighbor.getCoordinates());
    // var tile = cell.getTile();
    // if (tile != null && !tile.isKeystone()){
    // System.out.println(neighbor + " - " + cell.getTile().toString() + " ");
    // }
    // }
    // System.out.println(cell.coordinates() + " : " + getNeighborsCells(cell));
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    var allCells = this.cellsMap.values();
    for (var cell : allCells) {
      builder.append(cell.toString()).append("\n");
    }
    return builder.toString();
  }

  // private int dfs(TileType tileType, Cell cell, Set<Cell> visited) {
  // Objects.requireNonNull(tileType, "TileType can't be null in dfs()");
  // Objects.requireNonNull(cell, "Cell can't be null in dfs()");
  // if (visited.contains(cell)) {
  // return 0;
  // }
  // visited.add(cell);
  // if (!tileType.equals(cell.getTile().firstHabitat())) { /* not the same tile
  // */
  // return 0;
  // }
  // var score = 1;
  // var neighbors = getNeighbors(cell);
  // for (var neighbor : neighbors) {
  // score += dfs(tileType, neighbor, visited);
  // }
  // return score;
  // }

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

  // @Override
  // public final Map<TileType, Integer> calculateTileScore() {
  // var tileTypes = TileType.values();
  // var score = 0;
  // var allTiles = this.cellsMap.values();
  // var map = new HashMap<TileType, Integer>();
  // for (var tileType : tileTypes) {
  // var visited = new HashSet<Cell>();
  // for (var cell : allTiles) {
  // score = Math.max(score, dfs(tileType, cell, visited));
  // }
  // map.put(tileType, score);
  // }
  // return map;
  // }

}
