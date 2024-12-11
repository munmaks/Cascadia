package fr.uge.bag;


import fr.uge.environment.Tile;
import fr.uge.environment.TileType;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Bag class contains all tiles needed for a game.
 * @author MUNAITPASOV_MAOUCHE 
 */
public final class HexagonalBag implements Bag {

  /**
   * Indexes from WildlifeType `enum`: <br>
   * BEAR   : 0 <br>
   * ELK    : 1 <br>
   * HAWK   : 2 <br>
   * FOX    : 3 <br>
   * SALMON : 4 <br>
   */
  private final int[] animals =  {
    Constants.ANIMALS_HEXAGONAL,  /* BEAR   */
    Constants.ANIMALS_HEXAGONAL,  /* ELK    */
    Constants.ANIMALS_HEXAGONAL,  /* HAWK   */
    Constants.ANIMALS_HEXAGONAL,  /* FOX    */
    Constants.ANIMALS_HEXAGONAL   /* SALMON */
  };

  /* fill tiles with needed number of tiles for a game */
  private final ArrayList<Tile> tiles = new ArrayList<>();

  /* 3 tiles for each starter habitat tile (top, left, right), total 5 occurences */
  private final Tile[][] starters = new Tile[Constants.MAX_STARTER_HABITATS][Constants.MAX_TILES_ON_STARTER];
  private int maxTilesForGame = 0;
  private int maxTilesTotal = 0;
  
  private int indexStarterHabitatTile = -1;

  public HexagonalBag(int numberOfPlayers){
    if (!Constants.isValidNbPlayers(numberOfPlayers)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_NUMBER_OF_PLAYERS);
    }
    this.maxTilesForGame = (Constants.TILES_PER_PLAYER * numberOfPlayers) + Constants.THREE;
    this.maxTilesTotal = Constants.MAX_TILES_HEXAGONAL;
    try {
      initializeGame();
    } catch (IOException e) {
      System.err.println("Error initializing tiles: " + e.getMessage());
    }
    Collections.shuffle(this.tiles);
    BagUtils.decreaseNumberOfTiles(this.tiles, this.maxTilesForGame, this.maxTilesTotal);
  }



  /**
   * Decrease number of tiles in bag to maxTilesForGame
   * Result: it takes only needed number of tiles for a game (depends on number of players)
   */
  // private void decreaseNumberOfTiles() {
  //   int currentNumberOfTiles = this.maxTilesTotal;
  //   while (currentNumberOfTiles > this.maxTilesForGame){
  //     --currentNumberOfTiles;
  //     this.tiles.remove(0);   /* remove first element */
  //   }
  // }



  /**
   * Initialize tiles for Hexagonal version
   * throws IOException if file not found or can't be read
   */
  private void initializeGame() throws IOException {
    readHabitatTilesThreeAnimals();   /* 15 tiles */
    readHabitatTilesTwoAnimals();     /* 45 tiles */
    readKeystoneTiles();              /* 25 tiles */
    readStarterHabitatTiles();        /* 15 tiles (5 * 3) */
  }


  private void readTiles(String path, Function<String[], Tile> tileMapper) throws IOException {
    try (var reader = Files.newBufferedReader(Paths.get(path))) {
      String line;
      while ((line = reader.readLine()) != null) {
        var row = line.split("\\s+");
        tiles.add(tileMapper.apply(row));
      }
    }
  }

  private void readHabitatTilesThreeAnimals() throws IOException {           /* tile,   tile,  animal, animal, animal */
    readTiles(Constants.PATH_HABITAT_TILE_THREE_ANIMALS, row -> getHabitatTile(row[0], row[1], row[2], row[3], row[4]));
  }

  private void readHabitatTilesTwoAnimals() throws IOException {            /* tile,   tile,  animal, animal */
    readTiles(Constants.PATH_HABITAT_TILE_TWO_ANIMALS, row -> getHabitatTile(row[0], row[1], row[2], row[3]));
  }

  private void readKeystoneTiles() throws IOException {           /* tile,  animal */
    readTiles(Constants.PATH_KEYSTONE_TILE, row -> getKeystoneTile(row[0], row[1]));
  }



  
  /**
   * read all habitat tiles with three animals in `tiles`.
   * throws IOException if file not found or can't be read
   * */
  // private void readHabitatTilesThreeAnimals() throws IOException {
  //   try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_HABITAT_TILE_THREE_ANIMALS))) {
  //     String line;
  //     while ((line = reader.readLine()) != null) {
  //       var row = line.split("\\s+");
  //       tiles.add(                /* tile,   tile,   animal, animal, animal */
  //         getHabitatTile(row[0], row[1], row[2], row[3], row[4])
  //       );
  //     }
  //   }
  // }


  /**
   * read all habitat tiles with two animals in `tiles`.
   * throws IOException if file not found or can't be read
   * */
  // private void readHabitatTilesTwoAnimals() throws IOException {
  //   try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_HABITAT_TILE_TWO_ANIMALS))) {
  //     String line;
  //     while ((line = reader.readLine()) != null) {
  //       var row = line.split("\\s+");
  //       tiles.add(             /* tile,   tile,   animal, animal */
  //         getHabitatTile(row[0], row[1], row[2], row[3])
  //       );
  //     }
  //   }
  // }

  /**
   * read all keystone tiles in `tiles`.
   * throws IOException if file not found or can't be read
   * */
  // private void readKeystoneTiles() throws IOException {
  //   try (var reader = Files.newBufferedReader(Paths.get(Constants.PATH_KEYSTONE_TILE))) {
  //     String line;
  //     while ((line = reader.readLine()) != null) {
  //       var row = line.split("\\s+");
  //       tiles.add(      /* tile,  animal */
  //         getKeystoneTile(row[0], row[1])
  //       );
  //     }
  //   }
  // }







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
        /* toptile */                         /* tile,  animal */
        this.starters[index][0] = getKeystoneTile(row[0], row[1]);
        /* leftTile */                                /* tile,   tile,   animal, animal, animal */
        this.starters[index][1] = getHabitatTile(row[2], row[3], row[4], row[5], row[6]);
        /* rightTile */                            /* tile,   tile,   animal, animal */
        this.starters[index][2] = getHabitatTile(row[7], row[8], row[9], row[10]);
        index++;
      }
    }
    shuffleStarterHabitats(this.starters);  /* shuffle them for more random game */
  }



  /**
   * Gives random Habitat or Keystone tile
   * @return one tile from array `tiles`
   * */
  @Override
  public Tile getRandomTile() {
    // var random = new Random();
    // var randomIndex = random.nextInt(this.tiles.size()); /* in [0, tiles.size()[ */
    // return this.tiles.remove(randomIndex);
    return BagUtils.getRandomTile(this.tiles);
  }


  @Override
  public Tile[] getStarter(){
    if (this.indexStarterHabitatTile >= Constants.MAX_STARTER_HABITATS) {  /* prevent overflow ... */
      return null;
    }
    this.indexStarterHabitatTile++;
    return starters[this.indexStarterHabitatTile];
  }


  /**
   * Gives random KeystoneTile
   * @return one KeystoneTile from array `tiles`
   */
  private static Tile getKeystoneTile(
      String tile,
      String animal
    ) {
    return new Tile(
      TileType.valueOf(tile),
      TileType.valueOf(tile),
      Set.of(WildlifeType.valueOf(animal))
    );
  }


  /* to improve this method, we can use reflection */

  /**
   * Gives random HabitatTile with two animals
   * @return one HabitatTile from array `tiles`
   */
  // private Tile getHabitatTileTwoAnimals(
  //     String firstTile, String secondTile,
  //     String firstAnimal, String secondAnimal
  //   ) {
  //   var firstHabitat = TileType.valueOf(firstTile);
  //   var secondHabitat = TileType.valueOf(secondTile);
  //   var twoAnimals = Set.of(
  //       WildlifeType.valueOf(firstAnimal),
  //       WildlifeType.valueOf(secondAnimal)
  //   );
  //   return new Tile(firstHabitat, secondHabitat, twoAnimals);
  // }

  /**
   * Gives random HabitatTile with three animals
   * @return one HabitatTile from array `tiles`
   */
  private Tile getHabitatTile(String firstTile, String secondTile, String... animals) {
    var firstHabitat = TileType.valueOf(firstTile);
    var secondHabitat = TileType.valueOf(secondTile);
    var setAnimals = Arrays.stream(animals)
                             .map(WildlifeType::valueOf)
                             .collect(Collectors.toSet());
    return new Tile(firstHabitat, secondHabitat, setAnimals);
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
    for (var tile : this.tiles) {
      builder.append(" ").append(tile.toString()).append("\n");
    }
    //builder.append(starterHabitats.toString());
    return builder.toString();
  }


  /************************ TOKENS ****************************/




  @Override
  public final WildlifeType updateToken(WildlifeType token) {
    return BagUtils.updateToken(token, this.animals);
  }

  /**
   * This method is called when we need to replace a token on the game board with a new one.
   * @param token token to change
   * @return new token.
   */
  // @Override
  // public final WildlifeType updateToken(WildlifeType token) {
  //   /* return into deck current token */
  //   var index = token.ordinal();
  //   this.animals[index]++;
  //   return getRandomToken();
  // }


  /**
   * Draws a random WildlifeType from the deck.<br>
   * If the selected type has no tokens left,<br>
   * it retries until a type with available tokens is found. <br>
   *
   * @return WildlifeType - the randomly selected token.
   */
  @Override
  public final WildlifeType getRandomToken() {
    return BagUtils.getRandomToken(this.animals);
  }

  //     return getRandomTokenStream()
  //             .findFirst()
  //             .orElseGet(this::getFallbackToken);
  // }

  // private Stream<WildlifeType> getRandomTokenStream() {
  //     var random = new Random();
  //     return IntStream.range(0, Constants.MAX_ITERATION)
  //                     .mapToObj(_ -> random.nextInt(this.animals.length))
  //                     .filter(index -> this.animals[index] > 0)
  //                     .peek(index -> this.animals[index]--)
  //                     .map(index -> WildlifeType.values()[index]);
  // }

  // private WildlifeType getFallbackToken() {
  //     return Arrays.stream(WildlifeType.values())
  //                 .filter(animal -> this.animals[animal.ordinal()] > 0)
  //                 .findFirst()
  //                 .orElseThrow(() -> new IllegalStateException("No tokens available, game over!"));
  // }






  /**
   * Draws a random WildlifeType from the deck.<br>
   * If the selected type has no tokens left,<br>
   * it retries until a type with available tokens is found. <br>
   *
   * @return WildlifeType - the randomly selected token.
   */
  // @Override
  // public final WildlifeType getRandomToken(){
  //   var iteration = 0;
  //   var random = new Random();
  //   var length = this.animals.length;
  //   /* we have max iteration, to prevent infinity loop */
  //   while (iteration <= Constants.MAX_ITERATION) {
  //       var index = random.nextInt(length);   /* random integer in range [0, 5[ */
  //       ++iteration;

  //       if (this.animals[index] > 0) {   /* if tokens of this animals are still available */
  //           this.animals[index]--;
  //           return WildlifeType.values()[index];
  //       }
  //   }
  //   // if we left from while loop, we have to give him something
  //   return Arrays.stream(WildlifeType.values())
  //                .filter(animal -> this.animals[animal.ordinal()] > 0)
  //                .findFirst()
  //                .orElseThrow(() -> new IllegalStateException("No tokens available, game over!"));
  // }


}
