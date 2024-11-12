package fr.uge.environment;

import fr.uge.util.Constants;

/**
 * The Coordinates defined by x and y coordinates.
 * 
 * @param y - row
 * @param x - column
 */
public record Coordinates(int y, int x){

  public Coordinates {
    if (!Constants.isValidCoordinates(y, x)) {
      throw new IllegalArgumentException(Constants.IllegalCoordinates);
    }
  }

  @Override
  public final String toString() {
    return "(" + x + ", " + y + ")";
  }

}
