package fr.uge.data.environment;


import java.util.Objects;

import fr.uge.data.util.Constants;

public final class SquareCell implements Cell {
  private final Coordinates coordinates;
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

  /**
   * Constructor for SquareCell.
   * 
   * @param coordinates
   */
  public SquareCell(Coordinates coordinates) {
    this.coordinates = Objects.requireNonNull(coordinates, "coordinates are null in CellSquare()");
    this.placedAnimal = null;
    this.tile = null; /* default tile */
  }

  /**
   * get the number of neighbors of a cell.
   */
  @Override
  public final int getNumberOfNeighbors() { return Constants.NB_NEIGHBORS_SQUARE; }

  /**
   * checks if the cell is occupied by a tile.
   * 
   * @return true if the cell is occupied by a tile, false otherwise.
   */
  @Override
  public final boolean isOccupiedByTile() { return this.occupiedByTile; }

  /**
   * get the tile of the cell.
   * 
   * @return the tile of the cell.
   */
  @Override
  public final Tile getTile() { return this.tile; }

  /**
   * place a tile in the cell.
   * 
   * @param tileToPlace the tile to place in the cell.
   * @return true if the tile was placed, false otherwise.
   */
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

  /**
   * get coordinates of the cell.
   * 
   * @return the coordinates of the cell.
   */
  @Override
  public final Coordinates getCoordinates() { return this.coordinates; }

  /**
   * checks if the cell is occupied by an animal.
   * 
   * @return
   */
  private boolean isOccupiedByAnimal() { return this.occupiedByAnimal; }

  /**
   * checks if the cell could be placed with a given animal.
   * 
   * @param token the animal to place.
   * @return true if the cell could be placed with the given animal, false
   *         otherwise.
   */
  @Override
  public boolean couldBePlaced(WildlifeType token) {
    Objects.requireNonNull(token, "token must not be null in HabitatTile.couldBePlaced()");
    if (isOccupiedByAnimal() || this.tile == null) {
      return false;
    }
    return this.tile.animals().contains(token);
  }

  /**
   * place an animal in the cell.
   * 
   * @param token the animal to place.
   * @return true if the animal was placed, false otherwise.
   */
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

  /**
   * get the animal placed in the cell. If no animal is placed, return null.
   * 
   * @return the animal placed in the cell.
   */
  @Override
  public WildlifeType getAnimal() { return this.placedAnimal; }

  /**
   * remove the animal from the cell.
   * 
   * @return true if the animal was removed, false otherwise.
   */
  @Override
  public final String toString() {
    var builder = new StringBuilder();
    builder.append(this.coordinates).append("  ");
    builder.append(this.tile == null ? "no tile" : this.tile.toString()).append("  ");
    builder.append(this.placedAnimal == null ? "empty" : this.placedAnimal);
    return builder.toString();
  }

}
