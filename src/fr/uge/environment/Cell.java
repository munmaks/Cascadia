package fr.uge.environment;

import java.util.List;
import java.util.Objects;

/**
 * Cell class for represent a cell where a tile can be placed */
public record Cell(Coordinates coordinates, int version) {

  /* for version 3, we have constant */
  private static final int MAX_ROTATIONS = 6;

  /**
   *  Occupation status of tile represented by the cell.
   */
  private static boolean occupiedByTile = false;


  /* by default it's empty type of tile on this cellule */
  private static Tile tile = null;


  /**<p>for version 1 - 2, we needn't rotation</p>
   * <p>for version 3:</p>
   * <p>1 turn clockwise = 60 degrees, 6 turns clockwise = 360 == 0 degrees.<br>
   * 1 turn counter clockwise = 300 degrees, 6 turns clockwise 360 == 0 degrees</p>
   * */
  private static int currentRotation = 0;

  

  public Cell {
    Objects.requireNonNull(coordinates);
    if (version <= 0 || version > 4) {
      throw new IllegalArgumentException("Game Version can only be 1, 2 or 3");
    }

    if (coordinates.x() < 0 || coordinates.y() < 0) {
      throw new IllegalArgumentException("Invalid coordinates x or y");
    }

    tile = new EmptyTile(coordinates, version);
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

  
  public final Coordinates getCoordinates() {
    return coordinates;
  }



  public final void turnСlockwise() {
    if (version == 1) {
      return;
    }
    currentRotation = (currentRotation + 1) % MAX_ROTATIONS;
  }


  public final void turnСounterСlockwise() {
    if (version == 1) {
      return;
    }
    currentRotation = (currentRotation - 1 + MAX_ROTATIONS) % MAX_ROTATIONS;
  }


  public final int getRotation() {
    if (version == 3) {
      return currentRotation;
    }
    return 0;
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
  