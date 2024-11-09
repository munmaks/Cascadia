package fr.uge.environment;

/**
 * The Coordinates defined by x and y coordinates.
 * 
 * @param x - column
 * @param y - row
 */
public record Coordinates(int x, int y) {

  public Coordinates {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("(x, y) coordiantes must be positifs");
    }
  }
  
}
