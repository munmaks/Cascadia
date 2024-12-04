package fr.uge.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;


import fr.uge.util.Constants;

/**
 * Player's environment of all tiles and placed wildlife tokens */
public final class EnvironmentHexagonal implements Environment {

  /* 100 - 196 total tiles to show in player's environment */
  // private final Cell[][] grid = new Cell[Constants.MAX_ROW][Constants.MAX_COL];

  
  /* environment of all placed tiles by one player */
  private final HashSet<Coordinates> cellsSet;

  /* environment of all placed tiles by one player */
  private final HashMap<Coordinates, Cell> cellsMap;


  public EnvironmentHexagonal() {
    /* class for all check and valid parameters to stop checking everytime THE SAME THING */
    // initializeGrid(version);
    this.cellsMap = new HashMap<>();
    this.cellsSet = new HashSet<>();
  }


  

  // private void initializeGrid(int version) {
  //   for (var row = 0; row < Constants.MAX_ROW; ++row) {  /* row is `y` */
  //     for (var col = 0; col < Constants.MAX_COL; ++col) { /* col is `x` */
  //       grid[row][col] = new Cell(new Coordinates(row, col), version);
  //     }
  //   }
  // }

  
  /* adds a new tile to Player's environment */
//  private void addTile(Tile tile) {
//    Objects.requireNonNull(tile);
//
//    /* update all neighbors */
//    
//    tiles.add(tile);
//  }

  @Override
  public Cell getOneNeighbor(Cell cell, int direction) {
    var parity = cell.getCoordinates().y() & 1;  /* parity: 0 - even rows, 1 - odd rows */
    var diff = Constants.HEXAGONE_DIRECTION_DIFFERENCES[parity][direction];

    var neighborRow = cell.getCoordinates().y() + diff[1];
    var neighborCol = cell.getCoordinates().x() + diff[0];

    // if (!Constants.isValidCoordinates(neighborRow, neighborCol)) {  /* validate neighbor coordinates */
    //   return null;
    // }
    var currCoordinates = new Coordinates(neighborRow, neighborCol);
    var neighbor = new CellHexagonal(currCoordinates);
    if (!cellsMap.containsKey(currCoordinates)) {  /* insert in hashmap `cellsMap` and return new coordinates */
      cellsMap.put(currCoordinates, neighbor);
    }
    return neighbor;
  }

  
  /**
   * <b>gets a copy of list of all valid neighbors for a given hex cell.</b>
   * 
   * @param cell The hex cell for which to retrieve neighbors.
   * @return     An immutable list of neighboring cells.
   */
  @Override
  public List<Cell> getNeighbors(Cell cell) {
    var neighbors = new ArrayList<Cell>();
    for (var direction = 0; direction < Constants.MAX_ROTATIONS; ++direction) {
      var neighbor = getOneNeighbor(cell, direction);
      if (null != neighbor) {
        neighbors.add(neighbor);
      }
    }
    return List.copyOf(neighbors);
  }
  
  
  
  /**
   * To determine neighbors when calculating final score
   * */
  // public final List<Cell> getNeighbors(Cell cell) {
  //   Objects.requireNonNull(cell);
  //   return switch (cell){
  //     case CellSquare square -> getNeighborsSquare(cell);
  //     case CellHexagonal hexagonal -> getNeighborsHexagonal(cell);
  //   };
  // }
  


  // /**
  //  * To determine neighbors when calculating final score
  //  * */
  // public final List<Cell> getNeighborsCells(Coordinates coordinates) {
  //   Objects.requireNonNull(coordinates);
  //   return switch (coordinates){
  //     case CellSquare square -> getNeighborsSquareCoordinates(coordinates);
  //     case CellHexagonal hexagonal -> getOffsetNeighborsCoordinates(coordinates);
  //   };
  // }


  
  private void addNeighborsInSet(Cell cell) {
    Objects.requireNonNull(cell, "cell can't be null in addNeighborsInSet()");
    var neighbors = getNeighbors(cell);
    for (var neighbor : neighbors){
      var currCoordinates = neighbor.getCoordinates();
      if (!cellsMap.get(currCoordinates).isOccupied()) {
        this.cellsSet.add(currCoordinates);
      }
    }
  }


  // public final boolean placeCell(Coordinates coordinates){
  //   Objects.requireNonNull(coordinates, "Coordinates can't be null");
  //   Objects.requireNonNull(cell, "Cell can't be null");

  //   if (!cellsMap.containsKey(coordinates)){
  //     cellsMap.put(coordinates, cell);
  //     // delete those coordinates from cellsSet (it's occupied now)
  //     return true;
  //   }
  //   return false;
  // }



  /* places a wildlife token on a tile within the environment */
  @Override
  public final boolean placeTile(Cell cell, Tile tile) {
    Objects.requireNonNull(cell, "Cell can't be null");
    Objects.requireNonNull(tile, "Tile can't be null");
    if (cell.placeTile(tile)) {
      addNeighborsInSet(cell);  /* placed and added to all player's occupied cells */
      var currCoordinates = cell.getCoordinates();
      this.cellsMap.put(currCoordinates, cell);
      this.cellsSet.remove(currCoordinates);
      return true;
    }
    return false;
  }


  /**
   * Determine possibility to placed a wildlife token on player's environment. 
   * */
  @Override
  public final boolean canBePlacedWildlifeToken(WildlifeType token) {
    Objects.requireNonNull(token);
    boolean flag = false;

    for (var cell : this.cellsMap.values()) {
      switch (cell.getTile()) {
        case HabitatTile habitat -> { flag = habitat.canBePlaced(token);}
        case KeystoneTile keystone -> { flag = keystone.canBePlaced(token); }
        /* normally shouldn't happen */
        case EmptyTile empty -> { System.err.println("Can't place wildlife token on " + empty.toString()); }
      }
      if (flag) { /* found */
        return flag;
      }
    }
    return flag;
  }

  @Override
  public final boolean placeAnimal(Cell cell, WildlifeType token) {
    Objects.requireNonNull(cell);
    Objects.requireNonNull(token);
    if (!cell.isOccupied()) {
      return false;
    }
    var currentTile = cell.getTile();
    boolean flag = false;
    switch (currentTile) {
      case HabitatTile habitat -> { flag = habitat.placeAnimal(token);}
      case KeystoneTile keystone -> { flag = keystone.placeAnimal(token); }
      /* normally shouldn't happen */
      case EmptyTile empty -> { System.err.println("Can't place wildlife token on " + empty.toString()); }
    }
    return flag;
  }


  /* DEPRECATED, TO DELETE LATER, DONT FORGET */
  @Override
  public final Cell getCellOrCreate(Coordinates coordinates) {
    // if (!Constants.isValidCoordinates(coordinates.y(), coordinates.x())) {
    //   throw new IllegalArgumentException(Constants.ILLEGAL_COORDINATES);
    // }
    if (!cellsMap.containsKey(coordinates)) {
      var cell = switch (Constants.VERSION_HEXAGONAL) {
        case Constants.VERSION_HEXAGONAL -> new CellHexagonal(coordinates);
        case Constants.VERSION_SQUARE -> new CellSquare(coordinates);
        default -> throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
      };
      cellsMap.put(coordinates, cell);
      this.cellsSet.add(coordinates);
    }
    return cellsMap.get(coordinates);
    // return grid[y][x];
  }

  
  
//  public final List<Cell> getCellsWithToken(WildlifeToken token){
//    Objects.requireNonNull(token);
//    var list = new ArrayList<Cell>();
//    for (var tile : cells) {
//      if (tile.)
//    }
//    return List.copyOf(list);
//  }
  


  /* gets all tiles in the environment */
  @Override
  public final List<Cell> getCells() {
    return List.copyOf(this.cellsMap.values());
  }
  

  @Override
  public final Set<Coordinates> getPossibleCells() {

    // for (var cell : this.cellsSet){
    //   var neighbors = getNeighborsCells(cell);
    //   for (var neighbor : neighbors){
    //     if (!neighbor.isOccupied()) {
    //       this.cellsSet.add(neighbor);
    //     }
    //   }
    // }
    return Set.copyOf(this.cellsSet);
  }

  
  
  // public final Set<Coordinates> getPossibleCoordinates() {
  //   var set = new HashSet<Coordinates>();

  //   for (var cell : this.cellsSet){
  //     var neighbors = getNeighborsCells(cell);
  //     for (var neighbor : neighbors){
  //       if (!neighbor.isOccupied()) {
  //         set.add(neighbor.getCoordinates());
  //       }
  //     }
  //   }
  //   return Set.copyOf(set);
  // }

  
  @Override
  public void printAllNeighbors(Coordinates coordinates) {
    var neighbors = getNeighbors(cellsMap.get(coordinates));
    for (var neighbor : neighbors) {
      var cell = cellsMap.get(neighbor.getCoordinates());
      if (cell.getTile() instanceof HabitatTile){
        System.out.println(neighbor + " - " + cell.getTile().toString() + " "); //+ (neighbor.isOccupied() ? (neighbor.getTile().getAnimal()) : " no Animal"));
      }
    }
    // System.out.println(cell.coordinates() + " : " + getNeighborsCells(cell));
  }




  @Override
  public String toString() {
    var builder = new StringBuilder();
    // for (var row = 0; row < Constants.MAX_ROW; ++row) {
    //   for (var col = 0; col < Constants.MAX_COL; ++col) {
    //     builder.append(cellsMap.get(new Coordinates(row, col)).toString());
    //   }
    //   builder.append("\n");
    // }
    return builder.toString();
  }
  
  
  // for tests
  // public static void main(String[] main) {
  //   var version = 3;
  //   var env = new Environment(version);
  //   var bag = new Bag(3, version);
  //   var deck = new Deck(version);
  //   var coord = new Coordinates(0, 0);
  //   var tile = bag.getRandomTile();
  //   var token = deck.getRandomToken();

  //   var cell = env.getCell(coord);
  //   env.placeTile(cell, tile);
  //   env.placeWildlifeToken(cell, token);

  //   // cell.turnСounterСlockwise();
  //   // System.out.println("rotation: " + cell.getRotation());
  //   System.out.println("tile: " + tile.toString() + " token: " + token.toString());
  //   System.out.println(cell);
  //   System.out.println(env.getCells());
  // }
  
  
}

