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
    if (y < 0 || y >= Constants.MAX_ROW ||
        x < 0 || x >= Constants.MAX_COL){
      throw new IllegalArgumentException(
          "(" + x + ", " + y + ") coordiantes must be valid from(0, 0) to (" + 
          Constants.MAX_COL + ", " + Constants.MAX_ROW + ")");
    }
  }

  @Override
  public final String toString() {
    return "(" + x + ", " + y + ")";
  }

}
