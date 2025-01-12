package fr.uge.data.bag;


import fr.uge.data.environment.Tile;
import fr.uge.data.environment.TileType;
import fr.uge.data.environment.WildlifeType;
import fr.uge.data.util.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bag class contains all tiles needed for a game.
 * 
 * @author MUNAITPASOV_MAOUCHE
 */
public final class HexagonalBag implements Bag {

  /**
   * Indexes from WildlifeType `enum`: <br>
   * BEAR : 0 <br>
   * ELK : 1 <br>
   * HAWK : 2 <br>
   * FOX : 3 <br>
   * SALMON : 4 <br>
   */
  private final int[] animals = {Constants.ANIMALS_HEXAGONAL, /* BEAR */
      Constants.ANIMALS_HEXAGONAL, /* ELK */
      Constants.ANIMALS_HEXAGONAL, /* HAWK */
      Constants.ANIMALS_HEXAGONAL, /* FOX */
      Constants.ANIMALS_HEXAGONAL /* SALMON */
  };

  /* fill tiles with needed number of tiles for a game */
  private final LinkedList<Tile> tiles = new LinkedList<>();

  private final Tile[][] starters = new Tile[Constants.MAX_STARTER_HABITATS][Constants.MAX_TILES_ON_STARTER];
  private final int maxTilesForGame;

  private int indexStarterHabitatTile = -1;

  /**
   * Constructor for HexagonalBag
   * 
   * @param numberOfPlayers
   */
  public HexagonalBag(int numberOfPlayers) {
    if (!Constants.isValidNbPlayers(numberOfPlayers)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_NUMBER_OF_PLAYERS);
    }
    this.maxTilesForGame = (Constants.TILES_PER_PLAYER * numberOfPlayers) + Constants.THREE;
    try {
      initializeGame();
    } catch (IOException e) {
      System.err.println("Error initializing tiles: " + e.getMessage());
    }
    Collections.shuffle(this.tiles);
    UtilsBag.decreaseNumberOfTiles(this.tiles, this.maxTilesForGame);
  }

  /**
   * Initialize the game by reading all tiles from the files.
   * 
   * @throws IOException
   */
  private void initializeGame() throws IOException {
    readHabitatTilesThreeAnimals(); /* 15 tiles */
    readHabitatTilesTwoAnimals(); /* 45 tiles */
    readKeystoneTiles(); /* 25 tiles */
    readStarterHabitatTiles(); /* 15 tiles (5 * 3) */
  }

  /**
   * add all habitat tiles with three animals in `tiles`.
   * 
   * @throws IOException
   */
  private void readHabitatTilesThreeAnimals() throws IOException {
    /* tile, tile, animal, animal, animal */
    UtilsBag.readTiles(Constants.PATH_HABITAT_TILE_THREE_ANIMALS,
        row -> tiles.add(getHabitatTile(row[0], row[1], row[2], row[3], row[4])));
  }

  /**
   * add all habitat tiles with two animals in `tiles`.
   * 
   * @throws IOException
   */
  private void readHabitatTilesTwoAnimals() throws IOException {
    /* tile, tile, animal, animal */
    UtilsBag.readTiles(Constants.PATH_HABITAT_TILE_TWO_ANIMALS,
        row -> tiles.add(getHabitatTile(row[0], row[1], row[2], row[3])));
  }

  /**
   * add all keystone tiles in `tiles`.
   * 
   * @throws IOException
   */
  private void readKeystoneTiles() throws IOException {
    /* tile, animal */
    UtilsBag.readTiles(Constants.PATH_KEYSTONE_TILE,
        row -> tiles.add(getKeystoneTile(row[0], row[1])));
  }

  /**
   * <p>
   * add all starter habitat tiles in `tiles`.
   * </p>
   * <p>
   * throws IOException if file not found or can't be read
   * </p>
   * <p>
   * StarterHabitatTile has 3 tiles:
   * </p>
   * <p>
   * KeystoneTile topTile - 1 habitat, 1 animal
   * </p>
   * <p>
   * HabitatTile leftTile - 2 habitats, 3 animals
   * </p>
   * <p>
   * HabitatTile rightTile - 2 habitats, 2 animals
   * </p>
   * <p>
   * source: github/Cascadia/docs/Cascadia_rules_english.pdf
   * </p>
   */
  private void readStarterHabitatTiles() throws IOException {
    UtilsBag.readTiles(Constants.PATH_STARTER_HABITAT_TILE, row -> {
      var index = tiles.size();
      /* toptile */ /* tile, animal */
      this.starters[index][0] = getKeystoneTile(row[0], row[1]);
      /* leftTile */ /* tile, tile, animal, animal, animal */
      this.starters[index][1] = getHabitatTile(row[2], row[3], row[4], row[5], row[6]);
      /* rightTile */ /* tile, tile, animal, animal */
      this.starters[index][2] = getHabitatTile(row[7], row[8], row[9], row[10]);
    });
    shuffleStarterHabitats(this.starters); // shuffle them for a more random game
  }

  /**
   * Gives random Tile
   * 
   * @return one Tile from array `tiles`
   */
  @Override
  public Tile getRandomTile() { return UtilsBag.getRandomTile(this.tiles); }

  /**
   * Gives random StarterHabitatTile
   * 
   * @return one StarterHabitatTile from array `starters`
   */
  @Override
  public Tile[] getStarter() {
    if (this.indexStarterHabitatTile >= Constants.MAX_STARTER_HABITATS) { /* prevent overflow ... */
      return null;
    }
    this.indexStarterHabitatTile++;
    return this.starters[this.indexStarterHabitatTile];
  }

  /**
   * Gives random KeystoneTile
   * 
   * @return one KeystoneTile from array `tiles`
   */
  private static Tile getKeystoneTile(String tile, String animal) {
    return new Tile(TileType.valueOf(tile), TileType.valueOf(tile),
        Set.of(WildlifeType.valueOf(animal)));
  }

  /* to improve this method, we can use reflection */

  /**
   * Gives random HabitatTile with three animals
   * 
   * @return one HabitatTile from array `tiles`
   */
  private Tile getHabitatTile(String firstTile, String secondTile, String... animals) {
    var firstHabitat = TileType.valueOf(firstTile);
    var secondHabitat = TileType.valueOf(secondTile);
    var setAnimals = Arrays.stream(animals).map(WildlifeType::valueOf).collect(Collectors.toSet());
    return new Tile(firstHabitat, secondHabitat, setAnimals);
  }

  /**
   * Shuffle starter habitats tiles for more random game
   * 
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
   * Draws a random WildlifeType from the deck.<br>
   * If the selected type has no tokens left,<br>
   * it retries until a type with available tokens is found. <br>
   *
   * @return WildlifeType - the randomly selected token.
   */
  @Override
  public final WildlifeType getRandomToken() { return UtilsBag.getRandomToken(this.animals); }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    for (var tile : this.tiles) {
      builder.append(" ").append(tile.toString()).append("\n");
    }
    // builder.append(starterHabitats.toString());
    return builder.toString();
  }

}
