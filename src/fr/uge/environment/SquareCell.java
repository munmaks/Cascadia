package fr.uge.environment;

import fr.uge.util.Constants;
import java.util.Objects;


public final class SquareCell implements Cell {
  private final Coordinates coordinates;  /* immutable, once created can not be changed */
  private Tile tile;
  private WildlifeType placedAnimal;
  /**
   *  Occupation status of tile represented by the cell.
   */
  private boolean occupiedByTile = false;  /* idle */

  /**
   *  Occupation status of animal represented by the cell.
   */
  private boolean occupiedByAnimal = false;  /* idle */


  public SquareCell(Coordinates coordinates) {
    this.coordinates = Objects.requireNonNull(coordinates, "coordinates are null in CellSquare()");    
    this.placedAnimal = null;
    this.tile = null;  /* default tile */
  }

  /**
   * accessor for the number of neighbor for a square cell
   *
   * @return the number of neighbor in a square grill
   */
  @Override
  public final int getNumberOfNeighbors() {
    return Constants.NB_NEIGHBORS_SQUARE;
  }

  /*
   * @return possibility to place tile on the cell
   */
  @Override
  public final boolean isOccupiedByTile() {
    return this.occupiedByTile;
  }


  /**
   * accessor for the tile
   *
   * @return the tile
   */
  @Override
  public final Tile getTile(){
    return this.tile;
  }

  /**
   * check if the tile has been placed or not and if not, place tileToPlace in the tile
   *
   * @param tileToPlace the tile that we want to place
   * @return false if an animal is already occupied, true otherwise
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
   * accessor for the coordinates
   *
   * @return the coordinates
   */
  @Override
  public final Coordinates getCoordinates() {
    return this.coordinates;
  }

  /**
   * accessor for occupiedByAnimal
   *
   * @return if the tile is occupied by an animal or not
   */
  private boolean isOccupiedByAnimal() {
    return this.occupiedByAnimal;
  }

  /**
   * check if the token can be place or not
   *
   * @param token token that we want to place
   * @return true if the tile can be place well, false otherwise
   */
  @Override
  public boolean canBePlaced(WildlifeType token) {
    Objects.requireNonNull(token, "token must not be null in HabitatTile.canBePlaced()");
    if (isOccupiedByAnimal() || this.tile == null) {
      return false;
    }
    return this.tile.animals().contains(token);
  }

  /**
   * place token in the square cell if it's possible otherwise do nothing
   *
   * @param token the token that we want to place in the square cell
   * @return true if the token has been well place, false otherwise
   */
  @Override
  public boolean placeAnimal(WildlifeType token){
    Objects.requireNonNull(token, "token is null in CellSquare.placeAnimal()");
    if (isOccupiedByAnimal()) {
      return false;     /* we don't place animal, and return false (it wasn't placed) */
    }
    if (!this.canBePlaced(token)) {
      System.err.println(token + " can't be placed on this tile it accepts only: " + this.tile.animals());
      return false;
    }
    this.placedAnimal = token;
    this.occupiedByAnimal = true;
    return this.occupiedByTile;    /* we placed animal */
  }

  /**
   * accessor for placedAnimal
   *
   * @return the WildlifeType of the animal placed in the tile
   */
  @Override
  public WildlifeType getAnimal(){
    return this.placedAnimal;
  }

  @Override
  public final String toString() {
    // Objects.requireNonNull(this.tile, "tile is null in CellSquare.toString()");
    var builder = new StringBuilder();
    builder.append(this.coordinates).append("  ");
    builder.append(this.tile == null ? "no tile" : this.tile.toString()).append("  ");
    builder.append(this.placedAnimal == null ? "empty" : this.placedAnimal);
    // if (this.placedAnimal != null) {
    //   builder.append(this.placedAnimal);
    // }
    // builder.append("\n");
    return builder.toString();
  }
  

}
