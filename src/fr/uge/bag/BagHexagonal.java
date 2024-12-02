package fr.uge.bag;

import fr.uge.environment.HabitatTile;
import fr.uge.environment.KeystoneTile;
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


/**
 * Bag class contains all tiles needed for a game.
 * @author MUNAITPASOV_MAOUCHE 
 */
public final class BagHexagonal implements Bag {

  /* fill tiles with needed number of tiles for a game */
  private static final ArrayList<Tile> tiles = new ArrayList<>();

  private int indexStarterHabitatTile = -1;

  /* 3 tiles for each starter habitat tile (top, left, right), total 5 occurences */
  private final Tile[][] starters = new Tile[Constants.MAX_STARTER_HABITATS][Constants.MAX_TILES_ON_STARTER];
  private int maxTilesForGame = 0;
  private int maxTilesTotal = 0;


  public BagHexagonal(int numberOfPlayers){
    if (!Constants.isValidNbPlayers(numberOfPlayers)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_NUMBER_OF_PLAYERS);
    }
    this.maxTilesForGame = (Constants.TILE_PER_PLAYER * numberOfPlayers) + Constants.THREE;
    this.maxTilesTotal = Constants.MAX_TILES_HEXAGONAL;
    try {
        initializeVersionHexagonal();
    } catch (IOException e) {
        System.err.println("Error initializing tiles: " + e.getMessage());
    }
    Collections.shuffle(BagHexagonal.tiles);
    decreaseNumberOfTiles();
  }

  /**
   * Validate inputs for Bag constructor
   * @param numberOfPlayers number of players
   * @param version game version
   */
  private void validateInputs(int numberOfPlayers) {

  }


  /**
   * Decrease number of tiles in bag to maxTilesForGame
   */
  private void decreaseNumberOfTiles() {
    int currentNumberOfTiles = maxTilesTotal;
    while (currentNumberOfTiles > maxTilesForGame) {
      --currentNumberOfTiles;
      tiles.remove(0);
    }
  }



  /**
   * Initialize tiles for Hexagonal version
   * throws IOException if file not found or can't be read
   */
  private void initializeVersionHexagonal() throws IOException {
    readHabitatTilesThreeAnimals();   /* 15 tiles */
    readHabitatTilesTwoAnimals();     /* 45 tiles */
    readKeystoneTiles();              /* 25 tiles */
    readStarterHabitatTiles();        /* 15 tiles (5 * 3) */
  }



  
  /**
   * read all habitat tiles with three animals in `tiles`.
   * throws IOException if file not found or can't be read
   * */
  private void readHabitatTilesThreeAnimals() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_HABITAT_TILE_THREE_ANIMALS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
        var habitatTile = getHabitatTileThreeAnimals(
              row[0], row[1], row[2], row[3], row[4]
            );
        tiles.add(habitatTile);
      }
    }
  }


  /**
   * read all habitat tiles with two animals in `tiles`.
   * throws IOException if file not found or can't be read
   * */
  private void readHabitatTilesTwoAnimals() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_HABITAT_TILE_TWO_ANIMALS))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
        var habitatTile = getHabitatTileTwoAnimals(
              row[0], row[1], row[2], row[3]
            );
        tiles.add(habitatTile);
      }
    }
  }


  /**
   * read all keystone tiles in `tiles`.
   * throws IOException if file not found or can't be read
   * */
  private void readKeystoneTiles() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_KEYSTONE_TILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
        var keystone = getKeystoneTile(row[0], row[1]);
        tiles.add(keystone);
      }
    }
  }


  /**
   * <p>add all starter habitat tiles in `tiles`.</p>
   * <p>throws IOException if file not found or can't be read</p>
   * <p>StarterHabitatTile has 3 tiles:</p>
   * <p>KeystoneTile topTile  - 1 habitat, 1 animal</p>
   * <p>HabitatTile leftTile  - 2 habitats, 3 animals</p>
   * <p>HabitatTile rightTile - 2 habitats, 2 animals</p>
   * <p>source: github/Cascadia/docs/Cascadia_rules_english.pdf</p>
   * */
  private void readStarterHabitatTiles() throws IOException {
    int index = 0;
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_STARTER_HABITAT_TILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
                                   /* tile,  animal */
        var topTile = getKeystoneTile(row[0], row[1]);
                                                /* tile,  tile,  animal, animal, animal */
        var leftTile = getHabitatTileThreeAnimals(row[2], row[3], row[4], row[5], row[6]);
                                                /* tile, tile, animal, animal */
        var rightTile = getHabitatTileTwoAnimals(row[7], row[8], row[9], row[10]);
        starters[index][0] = topTile;
        starters[index][1] = leftTile;
        starters[index][2] = rightTile;
        index++;
      }
    }
    shuffleStarterHabitats(starters);  /* shuffle them for more random game */
  }



  /**
   * Gives random Habitat or Keystone tile
   * @return one tile from array `tiles`
   * */
  @Override
  public Tile getRandomTile() {
    var random = new Random();
    var randomIndex = random.nextInt(tiles.size()); /* in [0, tiles.size()[ */
    return tiles.remove(randomIndex);
  }


  @Override
  public Tile[] getStarter(){
    if (indexStarterHabitatTile >= Constants.MAX_STARTER_HABITATS) {  /* prevent overflow ... */
      return null;
    }
    ++indexStarterHabitatTile;
    return starters[indexStarterHabitatTile];
  }


  /**
   * Gives random KeystoneTile
   * @return one KeystoneTile from array `tiles`
   */
  private KeystoneTile getKeystoneTile(
      String tile,
      String animal
    ) {
    return new KeystoneTile(
        TileType.valueOf(tile),
        WildlifeType.valueOf(animal)
        );
  }

  /**
   * Gives random HabitatTile with two animals
   * @return one HabitatTile from array `tiles`
   */
  private HabitatTile getHabitatTileTwoAnimals(
      String firstTile, String secondTile,
      String firstAnimal, String secondAnimal
    ) {
    var twoTiles = new TileType[]{
        TileType.valueOf(firstTile),
        TileType.valueOf(secondTile)
    };
    var twoAnimals = new WildlifeType[]{
        WildlifeType.valueOf(firstAnimal),
        WildlifeType.valueOf(secondAnimal)
    };
    return new HabitatTile(twoTiles, twoAnimals);
  }

  /**
   * Gives random HabitatTile with three animals
   * @return one HabitatTile from array `tiles`
   */
  private HabitatTile getHabitatTileThreeAnimals(
      String firstTile, String secondTile,
      String firstAnimal, String secondAnimal, String thirdAnimal
    ) {
    var twoTiles = new TileType[]{
        TileType.valueOf(firstTile),
        TileType.valueOf(secondTile)
    };
    var threeAnimals = new WildlifeType[]{
        WildlifeType.valueOf(firstAnimal),
        WildlifeType.valueOf(secondAnimal),
        WildlifeType.valueOf(thirdAnimal)
    };
    return new HabitatTile(twoTiles, threeAnimals);
  }

  
  /**
   * Shuffle starter habitats tiles for more random game
   * @param starterHabitats array of StarterHabitatTile
   */
  private void shuffleStarterHabitats(Tile[][] starterHabitats) {
    var random = new Random();
    for (var i = starterHabitats.length - 1; i > 0; --i) {
      var randomIndex = random.nextInt(i + 1);
      /* swap two starter habitats tiles */
      var tmp = starterHabitats[randomIndex];
      starterHabitats[randomIndex] = starterHabitats[i];
      starterHabitats[i] = tmp;
    }
  }



  @Override
  public String toString() {
    var builder = new StringBuilder();
    for (var tile : tiles) {
      builder.append(" ").append(tile.toString()).append("\n");
    }
    //builder.append(starterHabitats.toString());
    return builder.toString();
  }

}
