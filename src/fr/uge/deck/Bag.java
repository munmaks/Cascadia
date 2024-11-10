package fr.uge.deck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Collections;

import fr.uge.environment.KeystoneTile;
import fr.uge.environment.HabitatTile;
import fr.uge.environment.StarterHabitatTile;
import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;
import fr.uge.environment.TileType;
import fr.uge.environment.Tile;


public record Bag(int nbPlayers, int version) {

  /* fill tiles with needed number of tiles for a game */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();

  /* constants for configurations*/
  private static final String PATH_KEYSTONE_TILE = "src/config/configKeystoneTile.txt";
  private static final String PATH_HABITAT_TILE_THREE_ANIMALS = "src/config/configHabitatTileThreeAnimals.txt";
  private static final String PATH_HABITAT_TILE_TWO_ANIMALS = "src/config/configHabitatTileTwoAnimals.txt";
  private static final String PATH_STARTER_HABITAT_TILE = "src/config/configStarterHabitatTile.txt";
  private static final String PATH_SQUARE_HABITAT_TILE = "src/config/configSquareHabitatTile.txt";

  /* we need to get only `nbPlayers` starter habitats */
  private static int nbNeighbors = 0;
  private static int indexStarterHabitatTile = -1;

  private static final int MAX_STARTER_HABITATS = 5;
  private static StarterHabitatTile[] starterHabitats = new StarterHabitatTile[MAX_STARTER_HABITATS];
  private static int maxTilesForGame;
  private static int maxTilesTotal;

  private static final int MIN_VERSION = 1;
  private static final int MAX_VERSION = 2;

  private static final int MIN_PLAYERS = 1;
  private static final int MAX_PLAYERS = 4;

  private static final int NEIGHBORS_DEFAULT = 6;
  private static final int NEIGHBORS_VERSION_ONE = 4;

  private static final int MAX_TILES_DEFAULT = 85;      /* 100 - 15 (starter habitat tiles) */
  private static final int MAX_TILES_VERSION_ONE = 50;  /* maximum for version 1 */

  private static final int NB_PLAYERS_VERSION_ONE = 2;
  private static final int TILE_COUNT_MAX_VERSION = 20;


  public Bag {
    validateInputs(nbPlayers, version);
    maxTilesForGame = calculateMaxTiles(nbPlayers, version);
    nbNeighbors   = (version == MAX_VERSION) ? NEIGHBORS_DEFAULT : NEIGHBORS_VERSION_ONE;
    maxTilesTotal = (version == MAX_VERSION) ? MAX_TILES_DEFAULT : MAX_TILES_VERSION_ONE;
    try {
      if (version == MAX_VERSION) {
          initializeVersionTwoTiles();
      } else {
          initializeDefaultTiles();
      }
    } catch (IOException e) {
        System.err.println("Error initializing tiles: " + e.getMessage());
    }
    Collections.shuffle(tiles);
    decreseNumberOfTiles();
  }


  private final void decreseNumberOfTiles() {
    int currentNumberOfTiles = maxTilesTotal;
    while (currentNumberOfTiles > maxTilesForGame) {
      --currentNumberOfTiles;
      tiles.remove(0);
    }
  }



  private void validateInputs(int nbPlayers, int version) {
    if (nbPlayers < MIN_PLAYERS || nbPlayers > MAX_PLAYERS) {
        throw new IllegalArgumentException(
            "nbPlayers must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS
          );
    }
    if (version < MIN_VERSION || version > MAX_VERSION) {
        throw new IllegalArgumentException(
            "Version must be between "   + MIN_VERSION + " and " + MAX_VERSION
          );
    }
    if (version != MAX_VERSION && nbPlayers != NB_PLAYERS_VERSION_ONE) {
      throw new IllegalArgumentException("Version 1 must have only 2 players");
    }
  }


  private int calculateMaxTiles(int nbPlayers, int version) {
    return (version == MAX_VERSION) ?
           ((TILE_COUNT_MAX_VERSION * nbPlayers) + 3) :
           ((TILE_COUNT_MAX_VERSION * NB_PLAYERS_VERSION_ONE) + 3 + 6); // two starter tiles 
  }
  
  
  private void initializeVersionTwoTiles() throws IOException {
    readHabitatTilesThreeAnimals();   /* 15 tiles */
    readHabitatTilesTwoAnimals();     /* 45 tiles */
    readKeystoneTiles();              /* 25 tiles */
    readStarterHabitatTiles();        /* 15 tiles (5 * 3) */
}
  
  private void initializeDefaultTiles() throws IOException {
    readSquareTiles();
  }
 
  /**
   * add all habitats tiles in `tiles`.
   * */
  private final void readSquareTiles() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(PATH_SQUARE_HABITAT_TILE))) {
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
   * add all habitats tiles in `tiles`.
   * */
  private final void readHabitatTilesThreeAnimals() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(PATH_HABITAT_TILE_THREE_ANIMALS))) {
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
   * add all habitats tiles in `tiles`.
   * */
  private final void readHabitatTilesTwoAnimals() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(PATH_HABITAT_TILE_TWO_ANIMALS))) {
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
   * add all keystone tiles in `tiles`.
   * */
  private final void readKeystoneTiles() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(PATH_KEYSTONE_TILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
        var keystone = getKeystoneTile(row[0], row[1]);
        tiles.add(keystone);
      }
    }
  }


  /**
   * add all starter habitat tiles in `tiles`.
   * source: github/Cascadia/docs/Cascadia_rules_english.pdf
   * KeystoneTile topTile  - 1 habitat, 1 animal
   * HabitatTile leftTile  - 2 habitats, 3 animals
   * HabitatTile rightTile - 2 habitats, 2 animals
   * */
  private final void readStarterHabitatTiles() throws IOException {
    int index = 0;
    try (var reader = Files.newBufferedReader(Paths.get(PATH_STARTER_HABITAT_TILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
        var topTile = getKeystoneTile(row[0], row[1]);  /* tile,  animal */

        var leftTile = getHabitatTileThreeAnimals(
              row[2], row[3], row[4], row[5], row[6]    /* tile, tile, animal, animal, animal */
            );
        var rightTile = getHabitatTileTwoAnimals(
              row[7], row[8], row[9], row[10]           /* tile, tile, animal, animal */
            );
        starterHabitats[index] = new StarterHabitatTile(topTile, leftTile, rightTile);
        index++;
      }
    }
    shuffleStarterHabitats(starterHabitats);  /* shuffle them for more random game */
  }


  
  /**
   * Gives random Habitat or Keystone tile
   * @return one tile from array `tiles`
   * */
  public final Tile getRandomTile() {
    var random = new Random();
    var randomIndex = random.nextInt(tiles.size()); /* from 0 to tiles.size()-1 */
    return tiles.remove(randomIndex);
  }


  /**
   * User can get one starter habitat tile
   * */
  public static StarterHabitatTile getStarterHabitatTile() {
    if (indexStarterHabitatTile >= MAX_STARTER_HABITATS) {  /* prevent overflow ... */
      return null;
    }
    indexStarterHabitatTile++;
    return starterHabitats[indexStarterHabitatTile];
  }

  
  private final KeystoneTile getKeystoneTile(
      String tile,
      String animal
    ) {
    return new KeystoneTile(
        TileType.valueOf(tile),
        WildlifeType.valueOf(animal)
        );
  }

  
  private final HabitatTile getHabitatTileTwoAnimals(
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


  private final HabitatTile getHabitatTileThreeAnimals(
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


  private final HabitatTile getSquareTiles(
      String oneTile,
      String firstAnimal, String secondAnimal
    ) {
    var tile = new TileType[]{TileType.valueOf(oneTile)};
    var tokens = new WildlifeType[]{
        WildlifeType.valueOf(firstAnimal),
        WildlifeType.valueOf(secondAnimal)
    };
    return new HabitatTile(tile, tokens);
  }
  
  
  private final void shuffleStarterHabitats(StarterHabitatTile[] starterHabitats) {
    var random = new Random();
    for (var i = starterHabitats.length - 1; i > 0; --i) {
        var randomIndex = random.nextInt(i + 1);
        /* swap two starter habitats tiles */
        var tmp = starterHabitats[randomIndex];
        starterHabitats[randomIndex] = starterHabitats[i];
        starterHabitats[i] = tmp;
    }
  }



  /* to delete i later */
  @Override
  public String toString() {
    var builder = new StringBuilder();
    int i = 1;
    for (var tile : tiles) {
      builder.append(i++ + " ").append(tile.toString()).append("\n");
    }
    //builder.append(starterHabitats.toString());
    return builder.toString();
  }


  // for tests
//  public static void main(String[] args) throws IOException {
//    var bag = new Bag(2, 1);
//    System.out.println(bag.toString());
//  }

}