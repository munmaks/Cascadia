package fr.uge.environment;

public enum WildlifeType {
  BEAR,
  ELK,
  HAWK,
  FOX,
  SALMON;

  /**
   * Indexes for WildlifeType `enum`:
   * BEAR   : 0
   * ELK    : 1
   * HAWK   : 2
   * FOX    : 3
   * SALMON : 4
   */
  
  @Override
  public String toString() {
      return this.name();
  }

}
