package fr.uge.environment;


import fr.uge.util.Constants;
import java.util.Objects;

public final class SquareCell implements Cell {
  private final Coordinates coordinates; /* immutable, once created can not be changed */
  private Tile tile;
  private WildlifeType placedAnimal;
  /**
   * Occupation status of tile represented by the cell.
   */
  private boolean occupiedByTile = false; /* idle */

  /**
   * Occupation status of animal represented by the cell.
   */
  private boolean occupiedByAnimal = false; /* idle */

  public SquareCell(Coordinates coordinates) {
    this.coordinates = Objects.requireNonNull(coordinates, "coordinates are null in CellSquare()");
    this.placedAnimal = null;
    this.tile = null; /* default tile */
  }

  @Override
  public final int getNumberOfNeighbors() { return Constants.NB_NEIGHBORS_SQUARE; }

  @Override
  public final boolean isOccupiedByTile() { return this.occupiedByTile; }

  @Override
  public final Tile getTile() { return this.tile; }

  @Override
  public final boolean placeTile(Tile tileToPlace) {
    Objects.requireNonNull(tileToPlace, "tileToPlace is null in CellSquare.placeTile()");
    if (isOccupiedByTile()) {
      return false;
    }
    this.tile = tileToPlace;
    this.occupiedByTile = true;
    return this.occupiedByTile;
  }

  @Override
  public final Coordinates getCoordinates() { return this.coordinates; }

  private boolean isOccupiedByAnimal() { return this.occupiedByAnimal; }

  @Override
  public boolean couldBePlaced(WildlifeType token) {
    Objects.requireNonNull(token, "token must not be null in HabitatTile.couldBePlaced()");
    if (isOccupiedByAnimal() || this.tile == null) {
      return false;
    }
    return this.tile.animals().contains(token);
  }

  @Override
  public boolean placeAnimal(WildlifeType token) {
    Objects.requireNonNull(token, "token is null in CellSquare.placeAnimal()");
    if (isOccupiedByAnimal()) {
      return false; /* we don't place animal, and return false (it wasn't placed) */
    }
    if (!this.couldBePlaced(token)) {
      System.err
          .println(token + " can't be placed on this tile it accepts only: " + this.tile.animals());
      return false;
    }
    this.placedAnimal = token;
    this.occupiedByAnimal = true;
    return this.occupiedByTile; /* we placed animal */
  }

  @Override
  public WildlifeType getAnimal() { return this.placedAnimal; }

  @Override
  public final String toString() {
    // Objects.requireNonNull(this.tile, "tile is null in CellSquare.toString()");
    var builder = new StringBuilder();
    builder.append(this.coordinates).append("  ");
    builder.append(this.tile == null ? "no tile" : this.tile.toString()).append("  ");
    builder.append(this.placedAnimal == null ? "empty" : this.placedAnimal);
    // if (this.placedAnimal != null) {
    // builder.append(this.placedAnimal);
    // }
    // builder.append("\n");
    return builder.toString();
  }

}
