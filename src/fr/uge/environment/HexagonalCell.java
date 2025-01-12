package fr.uge.environment;


import fr.uge.util.Constants;
import java.util.Objects;

/**
 * Cell class for represent a cell where a tile can be placed
 */
public final class HexagonalCell implements Cell {

  private final Coordinates coordinates;
  /**
   * Occupation status of tile represented by the cell.
   */
  private boolean occupiedByTile; /* idle */

  /**
   * Occupation status of animal represented by the cell.
   */
  private boolean occupiedByAnimal; /* idle */

  /* by default it's empty type of tile on this cell */
  private Tile tile;

  private WildlifeType placedAnimal = null;

  /**
   * <p>
   * for version 1 - 2, we needn't rotation
   * </p>
   * <p>
   * for version 3:
   * </p>
   * <p>
   * 1 turn clockwise = 60 degrees, 6 turns clockwise = 360 == 0 degrees.<br>
   * 1 turn counter clockwise = 300 degrees, 6 turns clockwise 360 == 0 degrees
   * </p>
   */
  private int currentRotation = 0;

  public HexagonalCell(Coordinates coordinates) {

    this.coordinates = Objects.requireNonNull(coordinates);

    this.occupiedByTile = false;
    this.occupiedByAnimal = false;
    this.tile = null;
  }

  @Override
  public final int getNumberOfNeighbors() { return Constants.NB_NEIGHBORS_HEXAGONAL; }

  /*
   * @return possibility to place tile on the cell
   */
  @Override
  public final boolean isOccupiedByTile() { return this.occupiedByTile; }

  private boolean isOccupiedByAnimal() { return this.occupiedByAnimal; }

  @Override
  public final boolean placeTile(Tile tileToPlace) {
    Objects.requireNonNull(tileToPlace);
    if (isOccupiedByAnimal()) {
      return false;
    }
    this.tile = tileToPlace;
    this.occupiedByTile = true;
    return this.occupiedByTile;
  }

  @Override
  public final Tile getTile() { return this.tile; }

  @Override
  public final WildlifeType getAnimal() { return this.placedAnimal; }

  /**
   * Only in Hexagonal version
   */
  public final void turnСounterСlockwise() {
    this.currentRotation = (this.currentRotation + 1) % Constants.MAX_ROTATIONS;
  }

  /**
   * Only in Hexagonal version
   */
  public final void turnСlockwise() {
    this.currentRotation = (this.currentRotation - 1 + Constants.MAX_ROTATIONS)
        % Constants.MAX_ROTATIONS;
  }

  /**
   * Only in Hexagonal version
   */
  public final int getRotation() { return this.currentRotation; }

  @Override
  public boolean couldBePlaced(WildlifeType token) {
    Objects.requireNonNull(token, "token must not be null in HabitatTile.couldBePlaced()");
    if (isOccupiedByAnimal()) {
      return false;
    }
    return this.tile.animals().contains(token);
  }

  @Override
  public boolean placeAnimal(WildlifeType token) {
    Objects.requireNonNull(token, "animal must not be null in placeToken()");
    if (isOccupiedByAnimal()) {
      return false; /* we don't place animal, and return false (it wasn't placed) */
    }
    if (!couldBePlaced(token)) {
      return false;
    }
    this.placedAnimal = token;
    this.occupiedByAnimal = true;
    return this.occupiedByAnimal; /* we placed animal */
  }

  @Override
  public final String toString() {
    var builder = new StringBuilder();
    builder.append(this.coordinates).append(" ").append(tile.toString()).append(" ");
    // builder.append("\n");
    return builder.toString();
  }

  @Override
  public final Coordinates getCoordinates() { return this.coordinates; }

}
