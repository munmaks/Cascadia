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
 * Player's environment of all tiles and placed wildlife tokens */
public final class SquareEnvironment implements Environment {

  /* environment of all placed tiles by one player */
  private final HashSet<Coordinates> cellsSet = new HashSet<>();

  /* environment of all placed tiles by one player */
  private final HashMap<Coordinates, Cell> cellsMap = new HashMap<>();

  /**
   *<b>(x, y)</b><br>
   *   {-1, 0} left  <br>
   *   {0, -1} down  <br>
   *   {1, 0}  right <br>
   *   {0, 1}  up    <br>
   * */
  private static final int[][] SQUARE_DIRECTION_DIFFERENCES = {
    {-1,  0}, { 0, -1}, { 1,  0}, { 0,  1}
  };

  private static final int SQUARE_NUMBER_OF_NEIGHBORS = 4;

  public SquareEnvironment() {
    /* class for all check and valid parameters to stop checking everytime THE SAME THING */
    // initializeGrid(version);
    // this.cellsMap = new HashMap<>();
    // this.cellsSet = new HashSet<>();
  }


  /**
   * calculate the coordinates of a neighbor cell in a specified direction
   *
   * @param coordinates the current coordinates of the cell
   * @param direction the direction in which to find the neighboring cell
   * @return the coordinates of the neighboring cell in the specified direction
   */
  private Coordinates getNeighborCoordinates(Coordinates coordinates, int direction) {
    var diff = SQUARE_DIRECTION_DIFFERENCES[direction];
    return new Coordinates(
      coordinates.y() + diff[1],  /* neighborRow */
      coordinates.x() + diff[0]   /* neighborCol */
    );
  }


  /**
   * Map.computeIfAbsent() - if the specified key is not already associated with a value (or is mapped to null),
   * attempts to compute its value using the given mapping function and enters it into this map unless null.
   * 
   * @param cell The cell for which to retrieve a neighbor.
   * @param direction The direction of the neighbor.
   * 
   * @return The neighbor cell.
   */
  @Override
  public Cell getOneNeighbor(Cell cell, int direction) {
    Objects.requireNonNull(cell, "Cell can't be null in getOneNeighbor()");
    var currCoordinates = getNeighborCoordinates(cell.getCoordinates(), direction);
    if (!cell.isOccupiedByTile()){
      return new SquareCell(currCoordinates); // without adding into hashmap
    }
    return this.cellsMap.computeIfAbsent(
        currCoordinates,                    /* neighbor coordinates */
        SquareCell::new                     /* create new cell if not exists */
    );
  }


  /**
   * <b>gets a copy of list of all valid neighbors for a given hex cell.</b>
   * 
   * @param cell The hex cell for which to retrieve neighbors.
   * 
   * @return     An immutable list of neighboring cells.
   */
  @Override
  public List<Cell> getNeighbors(Cell cell) {
      Objects.requireNonNull(cell, "Cell can't be null in getNeighbors()");
      return IntStream.range(0, SquareEnvironment.SQUARE_NUMBER_OF_NEIGHBORS)
                      .mapToObj(direction -> getOneNeighbor(cell, direction))
                      // .filter(Objects::nonNull)    // no need to filter, because we create new cell if not exists
                      .collect(Collectors.toUnmodifiableList());
  }


  /**
   * added to cellsSet the neighbor of the cell with an animal in the tile
   *
   * @param cell the cell that we want his neighbor to be added in cellsSet
   */
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
   * 
   * @return true if the tile was placed, false otherwise.
   * */
  @Override
  public final boolean placeTile(Cell cell, Tile tile) {
    Objects.requireNonNull(cell, "Cell can't be null in EnvironmentSqaure.placeTile()");
    Objects.requireNonNull(tile, "Tile can't be null in EnvironmentSqaure.placeTile()");
    if (cell.placeTile(tile)) {
      addNeighborsInSet(cell);  /* placed and added to all player's occupied cells */
      var currCoordinates = cell.getCoordinates();
      if (this.cellsSet.contains(currCoordinates)) {  /* remove from set if exists */
        this.cellsSet.remove(currCoordinates);
      }
      return true;
    }
    System.err.println("Can't place tile on " + cell.toString());
    return false;
  }


  /**
   * Determine possibility to placed a wildlife token on player's environment. 
   * */
  @Override
  public final boolean canBePlacedWildlifeToken(WildlifeType token) {
    Objects.requireNonNull(token, "Wildlife token can't be null in canBePlacedWildlifeToken()");
    for (var cell : this.cellsMap.values()) {
      if (cell.canBePlaced(token)) { /* found, no need to continue */
        return true;
      }
    }
    return false;
  }

  /**
   * check if the given cell is occupied and if not place the given token in the given cell
   *
   * @param cell the cell that we want the animal to be place
   * @param token the token that we want to place in the cell
   * @return true if the token has been place in the cell, false otherwise
   */
  @Override
  public final boolean placeAnimal(Cell cell, WildlifeType token) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(token);
    if (!cell.isOccupiedByTile()) {
      return false;
    }
    return cell.placeAnimal(token);
  }


  /**
   * Determine possibility to placed a wildlife token on player's environment.
   * Map.computeIfAbsent() - if the specified key is not already associated with a value (or is mapped to null),
   * attempts to compute its value using the given mapping function and enters it into this map unless null.
   */
  @Override
  public final Cell getCell(Coordinates coordinates) {
    Objects.requireNonNull(coordinates, "Coordinates can't be null in getCell()");
    return this.cellsMap.computeIfAbsent(coordinates, SquareCell::new);
  }

  


  /* gets all tiles in the environment */
  @Override
  public final List<Cell> getCells() {
    return List.copyOf(this.cellsMap.values());
  }

  /**
   * accessor for the cellSet who stock all the placed tiles by one player
   *
   * @return a copy of the cellsSet
   */
  @Override
  public final Set<Coordinates> getPossibleCells() {
    return Set.copyOf(this.cellsSet);
  }

  
  @Override
  public void printAllNeighbors(Coordinates coordinates) {
    Objects.requireNonNull(coordinates, "Coordinates can't be null in printAllNeighbors()");

    getNeighbors(this.cellsMap.get(coordinates)).stream()
        .map(neighbor -> this.cellsMap.get(neighbor.getCoordinates())) // Map to the cell
        .filter(Objects::nonNull) // Filter out null cells
        .filter(cell -> cell.getTile() != null) // Filter cells with null tiles
        .map(cell -> String.format("%8s  %8s  %6s",
                coordinates,
                cell.getTile(),
                (cell.getAnimal() == null) ? "" : cell.getAnimal())) // Format the output string
        .forEach(System.out::println); // Print each formatted string
  }


  /*
   * We look at all cells in set and then we show them
   */
  // @Override
  // public void printAllNeighbors(Coordinates coordinates) {
  //   Objects.requireNonNull(coordinates, "Coordinates can't be null in printAllNeighbors()");
  //   var neighbors = getNeighbors(this.cellsMap.get(coordinates));
  //   for (var neighbor : neighbors) {
  //     var builder = new StringBuilder();
  //     var cell = this.cellsMap.getOrDefault(neighbor.getCoordinates(), null);
  //     if (cell != null) {
  //       var tile = cell.getTile();
  //       if (tile == null) { continue; }
  //       builder.append(coordinates).append(" - ").append(tile.toString()).append(" - ").append(cell.getAnimal());
  //       System.out.println(builder);
  //     }
  //   }
  // }




  @Override
  public String toString() {
    var builder = new StringBuilder();
    // for (var row = 0; row < Constants.MAX_ROW; ++row) {
    //   for (var col = 0; col < Constants.MAX_COL; ++col) {
    //     builder.append(this.cellsMap.get(new Coordinates(row, col)).toString());
    //   }
    //   builder.append("\n");
    // }
    return builder.toString();
  }
  




  /*
  private int dfs(TileType tileType, Cell cell, Set<Cell> visited) {
    Objects.requireNonNull(tileType, "TileType can't be null in dfs()");
    Objects.requireNonNull(cell, "Cell can't be null in dfs()");
    if (cell.getTile() == null || visited.contains(cell)) { return 0; }
    visited.add(cell);
    if (!tileType.equals(cell.getTile().leftHabitat())) { return 0; }
    var score = 1;
    var neighbors = getNeighbors(cell);
    for (var neighbor : neighbors) {
      score += dfs(tileType, neighbor, visited);
    }
    return score;
  }
  */


  /**
   * calculate the size of a connected group of cells with the same tileType
   *
   * @param tileType the tileType that we want to find the size of the group
   * @param cell the starting cell for the research
   * @param visited a set of already visited cells
   * @return the size of the connected group of cells sharing the specified tile type
   */
  private int dfs(TileType tileType, Cell cell, Set<Cell> visited) {
    Objects.requireNonNull(tileType, "TileType can't be null in dfs()");
    Objects.requireNonNull(cell, "Cell can't be null in dfs()");
    if (cell.getTile() == null || visited.contains(cell)) { return 0; } /* no tile or already visited */
    visited.add(cell);
    if (!tileType.equals(cell.getTile().firstHabitat())) { return 0; }   /* not the same tile */
    return 1 + getNeighbors(cell).stream()
                                 .mapToInt(neighbor -> dfs(tileType, neighbor, visited))
                                 .max()
                                 .orElse(0);
  }


  /**
   * search the score for a specific tile type by finding the size of the largest connected group of cells that contain tiles of the specified type in the environment
   *
   * @param tileType the tileType that we want to calculate his score
   * @return the score calculated of the tileType
   */
  private int calculateScoreTileType(TileType tileType) {
    return this.cellsMap.values().stream()
                                 .mapToInt(cell -> dfs(tileType, cell, new HashSet<>()))
                                 .max()
                                 .orElse(0);
  }


  /**
   * calculate the score for all the tileType
   *
   * @return a map where the keys are the different tile types and the values are the corresponding scores
   */
  @Override
  public final Map<TileType, Integer> calculateTileScore(){
    return Collections.unmodifiableMap(
              Arrays.stream(TileType.values())
                    .collect(Collectors.toMap(
                        tileType -> tileType,
                        tileType -> calculateScoreTileType(tileType)  // this::calculateScoreTileType
                      )
                    )
          );
  }


  /*
  @Override
  public final Map<TileType, Integer> calculateTileScore(){
    return Arrays.stream(TileType.values())
                  .collect(Collectors.toMap(
                    tileType -> tileType,
                    tileType -> calculateScoreTileType(tileType)  // this::calculateScoreTileType
                  ));
  }
  */

  /*
  @Override
  public final Map<TileType, Integer> calculateTileScore() {
    var tileTypes = TileType.values();
    var allTiles = this.cellsMap.values();
    var map = new HashMap<TileType, Integer>();
    for (var tileType : tileTypes) {
      var score = 0;
      var visited = new HashSet<Cell>();
      for (var cell : allTiles) {
        score = Math.max(score, dfs(tileType, cell, visited));
      }
      map.put(tileType, score);
    }
    return map;
  }
  */

}

