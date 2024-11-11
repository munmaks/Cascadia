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

  private static int z = 0;

  /**<p>for version 1 - 2, we needn't rotation</p>
   * <p>for version 3:</p>
   * <p>1 turn clockwise = 60 degrees, 6 turns clockwise = 360 == 0 degrees.<br>
   * 1 turn counter clockwise = 300 degrees, 6 turns clockwise 360 == 0 degrees</p>
   * */
  private static int currentRotation = 0;


  private static boolean valid = false;



  public Cell {
    valid = validateInputs(coordinates, version);
    if (valid) {
      calculateThirdParameter(coordinates);      
    } else {
      
    }
  }
  
  
  private final boolean validateInputs(Coordinates coordinates, int version) {
    Objects.requireNonNull(coordinates);
    boolean flag = false;
    flag = Constants.isValidVersion(version);
    if (flag) {
      throw new IllegalArgumentException(
          "Game Version should be from " + Constants.VERSION_SQUARE + " to " +
          Constants.VERSION_HEXAGONAL + ", your version - " + version);
    }
    flag = Constants.isValidCoordinates(coordinates.y(), coordinates.x());
    if (flag) {
      throw new IllegalArgumentException(
          "(" + coordinates.x() + ", " + coordinates.y() + ") should be from(0, 0) to (" + 
          Constants.MAX_COL + ", " + Constants.MAX_ROW + ")");
    }
    return flag;
  }
  

  public final boolean validCoordinates() {
    return valid;
  }

  private final void calculateThirdParameter(Coordinates coordinates) {
    z = -(coordinates.x() + coordinates.y());
  }


  public final int getThirdParameter() {
    return z;
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



  public final void turnСlockwise() {
    if (version == Constants.VERSION_HEXAGONAL) {
      currentRotation = (currentRotation + 1) % Constants.MAX_ROTATIONS;
    }
  }


  public final void turnСounterСlockwise() {
    if (version == Constants.VERSION_HEXAGONAL) {
      currentRotation = (currentRotation - 1 + Constants.MAX_ROTATIONS) % Constants.MAX_ROTATIONS;
    }
  }


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
  