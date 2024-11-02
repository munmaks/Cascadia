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


public record Bag(
    int nbPlayers             /* we remove tiles depending on it */
  ) {

  /* fill tiles with needed number of tiles for a game */
  private static final ArrayList<Tile> tiles = new ArrayList<Tile>();

  /* constants for configurations*/
  private static final String filePathKeystoneTile = "src/config/configKeystoneTile.txt";
  private static final String filePathHabitatTileThreeAnimals = "src/config/configHabitatTileThreeAnimals.txt";
  private static final String filePathHabitatTileTwoAnimals = "src/config/configHabitatTileTwoAnimals.txt";
  private static final String filePathStarterHabitatTile = "src/config/configStarterHabitatTile.txt";

  /* we need to get only `nbPlayers` starter habitats */
  private static final int nbNeighbors = 6;
  private static final int nbHabitatAndKeystoneTiles = 85;
  private static final int MAX_STARTER_HABITATS = 5;
  private static int indexStarterHabitatTile = -1;
  private static StarterHabitatTile[] starterHabitats = new StarterHabitatTile[MAX_STARTER_HABITATS];
  private static int maxTilesForGame;

  public Bag {
    /* normally shouldn't happen */
    if (nbPlayers <= 0 || nbPlayers > 4) {
      throw new IllegalArgumentException();
    }

    maxTilesForGame = (nbPlayers * 20) + 3;
    try {
      readHabitatTilesThreeAnimals();   /* 15 tiles */
      readHabitatTilesTwoAnimals();     /* 45 tiles */
      readKeystoneTiles();              /* 25 tiles */

      readStarterHabitatTiles();        /* 15 tiles (5 * 3) */
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    Collections.shuffle(tiles);
    decreseNumberOfTiles();

  }

  /**
   * add all habitats tiles in `tiles`.
   * */
  private void readHabitatTilesThreeAnimals() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(filePathHabitatTileThreeAnimals))) {
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
  private void readHabitatTilesTwoAnimals() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(filePathHabitatTileTwoAnimals))) {
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
  private void readKeystoneTiles() throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(filePathKeystoneTile))) {
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
  private void readStarterHabitatTiles() throws IOException {
    int index = 0;
    try (var reader = Files.newBufferedReader(Paths.get(filePathStarterHabitatTile))) {
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


  private static void shuffleStarterHabitats(StarterHabitatTile[] starterHabitats) {
    var random = new Random();
    for (var i = starterHabitats.length - 1; i > 0; i--) {
        var randomIndex = random.nextInt(i + 1);
        /* swap two starter habitats tiles */
        var tmp = starterHabitats[randomIndex];
        starterHabitats[randomIndex] = starterHabitats[i];
        starterHabitats[i] = tmp;
    }
  }


  private static void decreseNumberOfTiles() {
    int currentNumberOfTiles = nbHabitatAndKeystoneTiles;
    while (currentNumberOfTiles > maxTilesForGame) {
      tiles.remove(0);
      currentNumberOfTiles--;
    }
  }

  
  /**
   * Gives random Habitat or Keystone tile
   * @return one tile from array `tiles`
   * */
  public static Tile getRandomTile() {
    var random = new Random();
    var randomIndex = random.nextInt(tiles.size());
    return tiles.remove(randomIndex);
  }

  
  
  /**
   * User can get one starter habitat tile or nothing if there's anything
   * */
  public static StarterHabitatTile getStarterHabitatTile() {
    if (indexStarterHabitatTile >= MAX_STARTER_HABITATS) {  /* prevent overflow ... */
      return null;
    }
    indexStarterHabitatTile++;
    return starterHabitats[indexStarterHabitatTile];
  }
  
  
  private static KeystoneTile getKeystoneTile(
      String tile, String animal
    ) {
    return new KeystoneTile(
        TileType.valueOf(tile),
        WildlifeType.valueOf(animal),
        new Tile[nbNeighbors],
        -1, -1
    );
  }

  
  private static HabitatTile getHabitatTileTwoAnimals(
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
    return new HabitatTile(
        twoTiles,
        twoAnimals,
        new Tile[nbNeighbors],
        -1, -1
    );
  }

  
  private static HabitatTile getHabitatTileThreeAnimals(
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
    return new HabitatTile(
        twoTiles,
        threeAnimals,
        new Tile[nbNeighbors],
        -1, -1
    );
  }
  
  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append(tiles.toString());
    //builder.append(starterHabitats.toString());
    return builder.toString();
  }

//  // for tests
//  public static void main(String[] args) throws IOException {
//    var bag = new Bag(2);
//    System.out.println(bag);
//  }

}