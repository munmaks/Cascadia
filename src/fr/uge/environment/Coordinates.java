package fr.uge.environment;

/**
 * The Coordinates defined by x and y coordinates.
 * 
 * @param y - row
 * @param x - column
 */
public record Coordinates(int y, int x) {

  public Coordinates {}

  @Override
  public final String toString() { return "(" + x + ", " + y + ")"; }

}
