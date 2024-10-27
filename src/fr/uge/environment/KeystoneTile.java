package fr.uge.environment;

public record KeystoneTile(Tile tile, WildlifeToken animal, int x, int y) implements Tile {

  public KeystoneTile {
    // check if not null etc ...
    
  }
  
  @Override
  public String toString() {
    return "Keystone Tile: tile - " + tile + " animal: " + animal;
  }
}
