package fr.uge.environment;

import fr.uge.util.Constants;
import java.util.Objects;

/**
 * Cell class for represent a cell where a tile can be placed */
public final class CellHexagonal implements Cell {

  private final Coordinates coordinates;
  /**
   *  Occupation status of tile represented by the cell.
   */
  private boolean occupied;

  /* by default it's empty type of tile on this cell */
  private Tile tile;

  /**<p>for version 1 - 2, we needn't rotation</p>
   * <p>for version 3:</p>
   * <p>1 turn clockwise = 60 degrees, 6 turns clockwise = 360 == 0 degrees.<br>
   * 1 turn counter clockwise = 300 degrees, 6 turns clockwise 360 == 0 degrees</p>
   * */
  private int currentRotation = 0;

  public CellHexagonal(Coordinates coordinates) {

    this.coordinates = Objects.requireNonNull(coordinates);

    this.occupied = false;
    this.tile = new EmptyTile();
  }
  
  @Override
  public final int getNumberOfNeighbors() {
    return Constants.NB_NEIGHBORS_HEXAGONAL;
  }
  
  
  @Override
  public final boolean isOccupied() {
    return this.occupied;
  }
  
  @Override
  public final boolean placeTile(Tile tileToPlace) {
    Objects.requireNonNull(tileToPlace);
    if (isOccupied()) {
      return false;
    }
    tile = tileToPlace;
    this.occupied = true;
    return this.occupied;
  }
  
  @Override
  public final Tile getTile() {
    return tile;
  }


  /**
   * Only in Hexagonal version
   * */
  public final void turnСounterСlockwise() {
    this.currentRotation = (this.currentRotation + 1) % Constants.MAX_ROTATIONS;
  }

  /**
   * Only in Hexagonal version
   * */
  public final void turnСlockwise() {
    this.currentRotation = (this.currentRotation - 1 + Constants.MAX_ROTATIONS) % Constants.MAX_ROTATIONS;
  }

  /**
   * Only in Hexagonal version
   * */
  public final int getRotation() {
    return this.currentRotation;
  }


  @Override
  public final String toString() {
    var builder = new StringBuilder();
    switch (tile) {
      case HabitatTile h -> { builder.append(this.coordinates).append(" ")
                                     .append(h.toString()).append(" ")
                                     .append((h.getAnimal() != null) ? (" ") : ("empty"));
                            }
      case KeystoneTile k -> { builder.append(this.coordinates).append(" ")
                                      .append(k.toString()).append(" ")
                                      .append((k.getAnimal() != null) ? (" ") : ("empty"));
                             }
      case EmptyTile e -> { /* builder.append("Empty cell"); */ }
    }
    // builder.append("\n");
    return builder.toString();
  }

  @Override
  public final Coordinates getCoordinates() {
    return this.coordinates;
  }

}
  