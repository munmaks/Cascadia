package fr.uge.data.bag;


import fr.uge.data.environment.Tile;
import fr.uge.data.environment.TileType;
import fr.uge.data.environment.WildlifeType;
import fr.uge.data.util.Constants;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

/**
 * Bag class contains all tiles needed for a game.
 * 
 * @author MUNAITPASOV_MAOUCHE
 */
public final class SquareBag implements Bag {

  /**
   * Indexes from WildlifeType `enum`: <br>
   * BEAR : 0 <br>
   * ELK : 1 <br>
   * HAWK : 2 <br>
   * FOX : 3 <br>
   * SALMON : 4 <br>
   */
  private final int[] animals = {Constants.ANIMALS_SQUARE, /* BEAR */
      Constants.ANIMALS_SQUARE, /* ELK */
      Constants.ANIMALS_SQUARE, /* HAWK */
      Constants.ANIMALS_SQUARE, /* FOX */
      Constants.ANIMALS_SQUARE /* SALMON */
  };

  /*
   * ((Constants.TILES_PER_PLAYER + MAX_TILES_ON_STARTER) * numberOfPlayers) +
   * Constants.THREE = 49
   */
  /* ((20 + 3) * 2) + 3 = 49 */

  /* fill tiles with needed number of tiles for a game */
  // private final ArrayList<Tile> tiles = new ArrayList<>();
  private final LinkedList<Tile> tiles = new LinkedList<>();

  private static final int MAX_TILES_FOR_GAME = Constants.MAX_TILES_SQUARE - 1;

  /**
   * Constructor for SquareBag
   * 
   * @param numberOfPlayers
   */
  public SquareBag(int numberOfPlayers) {
    if (!Constants.isValidNbPlayers(numberOfPlayers)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_NUMBER_OF_PLAYERS);
    }
    try {
      initializeGame();
    } catch (IOException e) {
      System.err.println("Error initializing tiles: " + e.getMessage());
    }
    Collections.shuffle(this.tiles);
    UtilsBag.decreaseNumberOfTiles(this.tiles, MAX_TILES_FOR_GAME);
  }

  /**
   * read all square tiles from config files into `tiles` variable. throws
   * 
   * @throws IOException
   */
  private void initializeGame() throws IOException {
    UtilsBag.readTiles(Constants.PATH_HABITAT_TILE_SQUARE,
        row -> tiles.add(getSquareTiles(row[0], row[1], row[2])));
  }

  /**
   * Gives random HabitatTile with one tile and two animals
   * 
   * @return one HabitatTile from array `tiles`
   */
  private Tile getSquareTiles(String oneTile, String... animals) {
    var tile = TileType.valueOf(oneTile);
    var set = Set.of(WildlifeType.valueOf(animals[0]), WildlifeType.valueOf(animals[1]));
    return new Tile(tile, tile, set);
  }

  /**
   * Gives random tile from the bag
   * 
   * @return random tile from the bag
   * @throws IllegalStateException if the bag is empty
   */
  @Override
  public Tile getRandomTile() { return UtilsBag.getRandomTile(this.tiles); }

  /**
   * Gives 3 random tiles from the bag There no keystone tiles in the square
   * version
   * 
   * @return array of 3 random tiles
   */
  @Override
  public Tile[] getStarter() {
    /* topTile leftTile rightTile */
    return new Tile[] {getRandomTile(), getRandomTile(), getRandomTile()};
  }

  /************************ TOKENS ****************************/

  /**
   * This method is called when we need to replace a token on the game board with
   * a new one.
   * 
   * @param token token to change
   * @return new token.
   */
  @Override
  public final WildlifeType updateToken(WildlifeType token) {
    return UtilsBag.updateToken(token, this.animals);
  }

  /**
   * This method is called when we need to replace a token on the game board with
   * a new one.
   * 
   * @param token token to change
   * @return new token.
   */
  // @Override
  // public final WildlifeType updateToken(WildlifeType token) {
  // Objects.requireNonNull(token, "Token must not be null!");

  // /* return into deck current token */
  // var index = token.ordinal();
  // this.animals[index]++;

  // return getRandomToken();
  // }

  /**
   * Gives random token from the bag
   * 
   * @return random token from the bag
   */
  @Override
  public final WildlifeType getRandomToken() { return UtilsBag.getRandomToken(this.animals); }

}
