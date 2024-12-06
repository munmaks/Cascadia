package fr.uge.environment;

import fr.uge.util.Constants;
import java.util.Objects;


public final class CellSquare implements Cell {
  private final Coordinates coordinates;  /* immutable, once created can not be changed */
  private Tile tile;
  private boolean occupied;

  public CellSquare(Coordinates coordinates) {
    this.coordinates = Objects.requireNonNull(coordinates, "coordinates are null in CellSquare()");    
    this.tile = new EmptyTile();  /* default tile */
    this.occupied = false;
  }


  @Override
  public final int getNumberOfNeighbors() {
    return Constants.NB_NEIGHBORS_SQUARE;
  } 


  @Override
  public final boolean isOccupied() {
    return this.occupied;
  }
  
  @Override
  public final boolean placeTile(Tile tileToPlace) {
    Objects.requireNonNull(tileToPlace, "tileToPlace is null in CellSquare.placeTile()");
    if (isOccupied()) {
      return false;
    }
    this.tile = tileToPlace;
    this.occupied = true;
    return this.occupied;
  }
  

  @Override
  public final Tile getTile(){
    return this.tile;
  }


  @Override
  public final String toString() {
    Objects.requireNonNull(this.tile, "tile is null in CellSquare.toString()");
    var builder = new StringBuilder();
    switch (this.tile) {
      case HabitatTile h -> { builder.append(this.coordinates).append(" ")
                                     .append(h.toString()).append(" ")
                                     .append((h.getAnimal() != null) ? ("") : ("empty"));
                            }
      case KeystoneTile k -> { builder.append(this.coordinates).append(" ")
                                      .append(k.toString()).append(" ")
                                      .append((k.getAnimal() != null) ? ("") : ("empty"));
                             }
      case EmptyTile e -> { builder.append(this.coordinates).append(" ").append(e.toString()); }
    }
    // builder.append("\n");
    return builder.toString();
  }
  

  @Override
  public final Coordinates getCoordinates() {
    return this.coordinates;
  }


}