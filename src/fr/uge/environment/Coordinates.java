package fr.uge.environment;

/**
 * The Coordinates defined by x and y coordinates.
 * 
 * @param x - row
 * @param y - column
 */
public record Coordinates(int x, int y) {

  public Coordinates {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("(x, y) coordiantes must be positifs");
    }
  }

  @Override
  public final String toString() {
    return "(" + x + ", " + y + ")";
  }
  
}
