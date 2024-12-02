package fr.uge.bag;

import fr.uge.environment.HabitatTile;
import fr.uge.environment.Tile;
import fr.uge.environment.TileType;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;



public final class BagSquare implements Bag {
  /* fill tiles with needed number of tiles for a game */
  private static final ArrayList<Tile> tiles = new ArrayList<>();

  private int indexStarterHabitatTile = -1;

  /* 3 tiles for each starter habitat tile (top, left, right), total 5 occurences */
  private final Tile[][] starters = new Tile[Constants.MAX_STARTER_HABITATS][Constants.MAX_TILES_ON_STARTER];
  private int maxTilesForGame = 0;
  private int maxTilesTotal = 0;


  public BagSquare(int numberOfPlayers) {
    if (numberOfPlayers < 1 || numberOfPlayers > 4) {
      throw new IllegalArgumentException("Invalid number of players");
    }
    this.maxTilesForGame = Constants.MAX_TILES_SQUARE - 1;  /* (20 * 2) + 3 + (3 * 2) = 49 */
    this.maxTilesTotal = Constants.MAX_TILES_SQUARE;
    try {
        // initializeVersionHexagonal();
        initializeVersionSquare();
    } catch (IOException e) {
        System.err.println("Error initializing tiles: " + e.getMessage());
    }
    Collections.shuffle(BagSquare.tiles);
    decreaseNumberOfTiles();
  }


  /**
   * Decrease number of tiles in bag to maxTilesForGame
   */
  private void decreaseNumberOfTiles() {
    int currentNumberOfTiles = maxTilesTotal;
    while (currentNumberOfTiles > maxTilesForGame) {
      --currentNumberOfTiles;
      BagSquare.tiles.remove(0);
    }
  }


  /**
   * Initialize tiles for Square version
   * throws IOException if file not found or can't be read
   */
  private void initializeVersionSquare() throws IOException {
    readSquareTiles();
  }


  /**
   * read all square tiles in `tiles`.
   * throws IOException if file not found or can't be read
  */
  private void readSquareTiles() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_SQUARE_HABITAT_TILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
                                       /* tile   animal  animal */
        var habitatTile = getSquareTiles(row[0], row[1], row[2]);
        tiles.add(habitatTile);
      }
    }
  }


  /**
   * Gives random HabitatTile with one tile and two animals
   * @return one HabitatTile from array `tiles`
   */
  private HabitatTile getSquareTiles(
      String oneTile,
      String firstAnimal, String secondAnimal
    ) {
    var tile = new TileType[]{ TileType.valueOf(oneTile) };
    var tokens = new WildlifeType[]{
        WildlifeType.valueOf(firstAnimal),
        WildlifeType.valueOf(secondAnimal)
    };
    return new HabitatTile(tile, tokens);
  }

  @Override
  public Tile getRandomTile() {
    var random = new Random();
    var randomIndex = random.nextInt(tiles.size()); /* in [0, tiles.size()[ */
    return tiles.remove(randomIndex);
  }


  @Override
  public Tile[] getStarter(){
                       /*  topTile          leftTile         rightTile  */
    return new Tile[] { getRandomTile(), getRandomTile(), getRandomTile() };
  }

}

