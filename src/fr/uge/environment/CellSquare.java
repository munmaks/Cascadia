package fr.uge.environment;

import fr.uge.util.Constants;

import java.util.Objects;


public final class CellSquare {

  private final Coordinates coordinates;
  private Tile tile;
  private boolean occupied;

  public CellSquare(Coordinates coordinates) {
    this.coordinates = Objects.requireNonNull(coordinates);
    if (!Constants.isValidCoordinates(this.coordinates.y(), this.coordinates.x())) {
      throw new IllegalArgumentException(Constants.ILLEGAL_COORDINATES);
    }
    this.occupied = false;
    this.tile = null;
  }



  public final boolean occupied() {
    return this.occupied;
  }
  
  
  public final boolean placeTile(Tile tileToPlace) {
    Objects.requireNonNull(tileToPlace);
    if (occupied()) {
      return false;
    }
    this.tile = tileToPlace;
    this.occupied = true;
    return this.occupied;
  }
  
  
  public final Tile getTile() {
    return tile;
  }


  @Override
  public final String toString() {
    var builder = new StringBuilder();
    switch (tile) {
      case HabitatTile h -> { builder.append(coordinates).append(" ")
                                     .append(h.toString()).append(" ")
                                     .append((h.getAnimal() != null) ? (" ") : ("empty"));
                            }
      case KeystoneTile k -> { builder.append(coordinates).append(" ")
                                      .append(k.toString()).append(" ")
                                      .append((k.getAnimal() != null) ? (" ") : ("empty"));
                             }
      case EmptyTile e -> { /* builder.append("Empty cell"); */ }
    }
    // builder.append("\n");
    return builder.toString();
  }

    
  public final Coordinates coordinates() {
    return coordinates;
  }


}