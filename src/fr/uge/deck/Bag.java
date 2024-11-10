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

import fr.uge.util.Constants;


public record Bag(int nbPlayers, int version) {

  /* fill tiles with needed number of tiles for a game */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();

  /* we need to get only `nbPlayers` starter habitats */
  private static int nbNeighbors = 0;
  private static int indexStarterHabitatTile = -1;

//  private static final int MAX_STARTER_HABITATS = 5;
  private static StarterHabitatTile[] starterHabitats = new StarterHabitatTile[Constants.MAX_STARTER_HABITATS];
  private static int maxTilesForGame = 0;
  private static int maxTilesTotal = 0;



  public Bag {
    validateInputs(nbPlayers, version);
    maxTilesForGame = calculateMaxTiles(nbPlayers, version);
    maxTilesTotal = (version == Constants.VERSION_HEXAGONAL) ? Constants.MAX_TILES_HEXAGONAL : Constants.MAX_TILES_SQUARE;
    try {
      if (version == Constants.VERSION_HEXAGONAL) {
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



  private final void validateInputs(int nbPlayers, int version) {
    if (nbPlayers < Constants.MIN_PLAYERS ||
        nbPlayers > Constants.MAX_PLAYERS) {
        throw new IllegalArgumentException(
            "nbPlayers must be between " + Constants.MIN_PLAYERS + " and " + Constants.MAX_PLAYERS);
    }
    if (version < Constants.VERSION_SQUARE ||
        version > Constants.VERSION_HEXAGONAL) {
        throw new IllegalArgumentException(
            "Version must be between "   + Constants.VERSION_SQUARE + " and " + Constants.VERSION_HEXAGONAL);
    }
    if (version != Constants.VERSION_HEXAGONAL &&
        nbPlayers != Constants.NB_PLAYERS_SQUARE) {
      throw new IllegalArgumentException(
            "Square Version must have exactly 2 players");
    }
  }

  /**
   * TO IMPROVE LATER, COMMENT JUST FOR YOU MEHDI ;)
   * Calculate max tiles that bag going contains for different versions
   * Expression (20 * 2) + 3 + (3 * 2) means:
   * (tiles per players * nbPlayers) + constant for tiles on board + (starter tile * nbPlayers) 
   * P.S. starter tile contains 3 tiles
   * @param nbPlayers ...
   * @param version ...
   * */
  private int calculateMaxTiles(int nbPlayers, int version) {
    return (version == Constants.VERSION_HEXAGONAL) ?
           ((Constants.TILE_PER_PLAYER * nbPlayers) + Constants.THREE):
           (Constants.MAX_TILES_SQUARE - 1);    /* (20 * 2) + 3 + (3 * 2) = 49 */
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
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_SQUARE_HABITAT_TILE))) {
      String line = "";
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
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_HABITAT_TILE_THREE_ANIMALS))) {
      String line = "";
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
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_HABITAT_TILE_TWO_ANIMALS))) {
      String line = "";
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
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_KEYSTONE_TILE))) {
      String line = "";
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
    try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_STARTER_HABITAT_TILE))) {
      String line = "";
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
    if (indexStarterHabitatTile >= Constants.MAX_STARTER_HABITATS) {  /* prevent overflow ... */
      return null;
    }
    ++indexStarterHabitatTile;
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
    var tile = new TileType[]{ TileType.valueOf(oneTile) };
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
    var i = 1;
    for (var tile : tiles) {
      builder.append(i++).append(" ").append(tile.toString()).append("\n");
    }
    //builder.append(starterHabitats.toString());
    return builder.toString();
  }


  // for tests
//  public static void main(String[] args) throws IOException {
//    var bag = new Bag(4, 3);
//    System.out.println(bag.toString());
//  }

}