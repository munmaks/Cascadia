package fr.uge.environment;

public record StarterHabitatTile(
      HabitatTile leftTile,
      HabitatTile rightTile,
      KeystoneTile topTile
    ) implements Tile {

  //  private final HabitatTile leftTile;
  //  private final HabitatTile rightTile;
  //  private final KeystoneTile top;

  /*
   * configStarterHabitatTile.txt
   * 15 Tiles : with three animals
   * Mountain, Wetland : Elk, Hawk, Fox
   * */
  // five entities
  public StarterHabitatTile {
  
  }
}
