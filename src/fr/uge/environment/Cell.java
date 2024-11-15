package fr.uge.environment;

import java.util.Objects;
//import java.util.List;

import fr.uge.util.Constants;

/**
 * Cell class for represent a cell where a tile can be placed */
public record Cell(Coordinates coordinates, int version) {

  /**
   *  Occupation status of tile represented by the cell.
   */
  private static boolean occupiedByTile = false;

  /* by default it's empty type of tile on this cell */
  private static Tile tile = new EmptyTile();

  /**<p>for version 1 - 2, we needn't rotation</p>
   * <p>for version 3:</p>
   * <p>1 turn clockwise = 60 degrees, 6 turns clockwise = 360 == 0 degrees.<br>
   * 1 turn counter clockwise = 300 degrees, 6 turns clockwise 360 == 0 degrees</p>
   * */
  private static int currentRotation = 0;

  public Cell {
    validateInputs(coordinates, version);
  }
  
  
  private static void validateInputs(Coordinates coordinates, int version) {
    Objects.requireNonNull(coordinates);
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    if (!Constants.isValidCoordinates(coordinates.y(), coordinates.x())) {
      throw new IllegalArgumentException(Constants.IllegalCoordinates);
    }
  }
  
  
  public final boolean isOccupied() {
    return occupiedByTile;
  }
  
  
  public final boolean placeTile(Tile tileToPlace) {
    Objects.requireNonNull(tileToPlace);
    if (isOccupied()) {
      return false;
    }
    tile = tileToPlace;
    occupiedByTile = true;
    return occupiedByTile;
  }
  
  
  public final Tile getTile() {
    return tile;
  }


  /**
   * Only in Hexagonal version
   * */
  public final void turnСlockwise() {
    if (version == Constants.VERSION_HEXAGONAL) {
      currentRotation = (currentRotation + 1) % Constants.MAX_ROTATIONS;
    }
  }

  /**
   * Only in Hexagonal version
   * */
  public final void turnСounterСlockwise() {
    if (version == Constants.VERSION_HEXAGONAL) {
      currentRotation = (currentRotation - 1 + Constants.MAX_ROTATIONS) % Constants.MAX_ROTATIONS;
    }
  }

  /**
   * Only in Hexagonal version
   * */
  public final int getRotation() {
    return (version == Constants.VERSION_HEXAGONAL) ? (currentRotation) : (0) ;
  }


  @Override
  public final String toString() {
    var builder = new StringBuilder();
    switch (tile) {
      case HabitatTile h -> { builder.append(h.toString() + " ").append(h.getAnimal()); }
      case KeystoneTile k -> { builder.append(k.toString() + " ").append(k.getAnimal()); }
      case EmptyTile e -> { System.out.println("Empty cell:"); }
      case StarterHabitatTile s -> { ; }
    }
    builder.append(" " + coordinates);
    return builder.toString();
  }


}
  