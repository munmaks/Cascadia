package fr.uge.environment;

public enum TileType {
  FOREST,
  WETLAND,
  MOUNTAIN,
  RIVER,
  PRARIE;

  /**
   * Indexes for TileType `enum`:
   * FOREST   : 0
   * WETLAND  : 1
   * MOUNTAIN : 2
   * RIVER    : 3
   * PRARIE   : 4
   */

  @Override
  public String toString() {
      return this.name();
  }

}
