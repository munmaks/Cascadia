package fr.uge.bag;

import fr.uge.environment.Tile;
import fr.uge.environment.TileType;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.Set;


public final class SquareBag implements Bag {

  /**
   * Indexes from WildlifeType `enum`: <br>
   * BEAR   : 0 <br>
   * ELK    : 1 <br>
   * HAWK   : 2 <br>
   * FOX    : 3 <br>
   * SALMON : 4 <br>
   */
  private static final int[] animals =  {
    Constants.ANIMALS_SQUARE,  /* BEAR   */
    Constants.ANIMALS_SQUARE,  /* ELK    */
    Constants.ANIMALS_SQUARE,  /* HAWK   */
    Constants.ANIMALS_SQUARE,  /* FOX    */
    Constants.ANIMALS_SQUARE   /* SALMON */
  };

  /* fill tiles with needed number of tiles for a game */
  private static final ArrayList<Tile> tiles = new ArrayList<>();

  /* ((Constants.TILES_PER_PLAYER + MAX_TILES_ON_STARTER) * numberOfPlayers) + Constants.THREE = 49 */
  /* ((20 + 3) * 2) + 3 = 49 */

  private static final int MAX_TILES_FOR_GAME = Constants.MAX_TILES_SQUARE - 1;
  private static final int MAX_TILES_TOTAL = Constants.MAX_TILES_SQUARE;

  public SquareBag(int numberOfPlayers) {
    if (!Constants.isValidNbPlayers(numberOfPlayers)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_NUMBER_OF_PLAYERS);
    }
    try {
      initializeGame();
    } catch (IOException e) {
      System.err.println("Error initializing tiles: " + e.getMessage());
    }
    Collections.shuffle(SquareBag.tiles);
    decreaseNumberOfTiles();
  }


  /**
   * Decrease number of tiles in bag to maxTilesForGame
   * Result: it takes one tile from the bag
   */
  private void decreaseNumberOfTiles() {
    int currentNumberOfTiles = SquareBag.MAX_TILES_FOR_GAME;
    while (currentNumberOfTiles > SquareBag.MAX_TILES_TOTAL) {
      --currentNumberOfTiles;
      SquareBag.tiles.remove(0);  /* remove first element */
    }
  }


  /**
   * Initialize tiles for Square version
   * throws IOException if file not found or can't be read
   */
  private void initializeGame() throws IOException {
    readSquareTiles();
  }


  /**
   * read all square tiles from config files into `tiles` variable.
   * throws IOException if file not found or can not be read
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
  private Tile getSquareTiles(
      String oneTile,
      String firstAnimal, String secondAnimal
    ) {
    var tile = TileType.valueOf(oneTile);
    var set = Set.of(WildlifeType.valueOf(firstAnimal), WildlifeType.valueOf(secondAnimal));
    return new Tile(tile, tile, set);
  }

  @Override
  public Tile getRandomTile() {
    var random = new Random();
    var randomIndex = random.nextInt(SquareBag.tiles.size()); /* in [0, tiles.size()[ */
    return SquareBag.tiles.remove(randomIndex);
  }


  @Override
  public Tile[] getStarter(){
                      /*  topTile          leftTile         rightTile  */
    return new Tile[]{ getRandomTile(), getRandomTile(), getRandomTile() };
  }


  /************************ TOKENS ****************************/


  /**
   * This method is called when we need to replace a token on the game board with a new one.
   * @param token token to change
   * @return new token.
   */
  @Override
  public final WildlifeType updateToken(WildlifeType token) {
    Objects.requireNonNull(token, "Token must not be null!");

    /* return into deck current token */
    var index = token.ordinal();
    animals[index]++;

    return getRandomToken();
  }


  /**
   * Draws a random WildlifeType from the deck.<br>
   * If the selected type has no tokens left,<br>
   * it retries until a type with available tokens is found. <br>
   *
   * @return WildlifeType - the randomly selected token.
   */
  @Override
  public final WildlifeType getRandomToken(){
    int index;
    int iteration = 0;
    var random = new Random();

    /* we have max iteration, to prevent infinity loop */
    while (iteration <= Constants.MAX_ITERATION) {
        index = random.nextInt(animals.length);   /* random integer in range [0, 5[ */
        ++iteration;

        if (animals[index] > 0) {   /* if tokens of this animals are still available */
            animals[index]--;
            return WildlifeType.values()[index];
        }
    }
    /* normally it shouldn't happen */
    throw new IllegalArgumentException("Maximum number of iterations exceeded in drawToken()");
  }


}

