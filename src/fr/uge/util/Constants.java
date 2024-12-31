package fr.uge.util;

public final class Constants {

  /* prevent instantiation of this utility class */
  private Constants() {
  }

  /* Game */
  public static final int NB_PLAYERS_SQUARE = 2;

  /* Turn Manager */
  public static final int MAX_GAME_TURNS = 2;

  /* Game Board */
  public static final int TOKENS_ON_BOARD = 4;
  public static final int TILES_ON_BOARD = 4;
  public static final int NB_SCORING_CARDS = 5;

  /* Environment */
  // public static final int MAX_ROW = 12;
  // public static final int MAX_COL = 12;

  public static final int NB_NEIGHBORS_SQUARE = 4;
  public static final int NB_NEIGHBORS_HEXAGONAL = 6;
  public static final int MAX_ROTATIONS = 6; /* for hexagonals */

  public static final int VERSION_SQUARE = 1;
  public static final int VERSION_HEXAGONAL = 3;

  public static final int NB_TOKENS = 5;

  /**
   * <b>
   * Direction offsets based on "odd-r" layout<br>
   * (x, y)</b><br>
   * <br>
   * even rows<br>
   * {<br>
   * (-1, -1), (-1, 0), (-1, 1), left habitat: (left up, left, left down)<br>
   * ( 0, 1), ( 1, 0), ( 0, -1) right habitat: (right down, right, right up)<br>
   * }<br>
   * <br>
   *
   * odd rows<br>
   * {<br>
   * ( 0, -1), (-1, 0), ( 0, 1), left habitat: (left up, left, left down)<br>
   * ( 1, 1), ( 1, 0), ( 1, -1) right habitat: (right down, right, right up)<br>
   * }<br>
   * <br>
   * Source: https://www.redblobgames.com/grids/hexagons/#neighbors-offset
   * </b>
   */
  public static final int[][][] HEXAGONE_DIRECTION_DIFFERENCES = {
      {
          { -1, -1 }, { -1, 0 }, { -1, 1 },
          { 0, 1 }, { 1, 0 }, { 0, -1 }
      },
      {
          { 0, -1 }, { -1, 0 }, { 0, 1 },
          { 1, 1 }, { 1, 0 }, { 1, -1 }
      }
  };

  /**
   * <b>
   * (x, y)</b><br>
   * {-1, 0} left <br>
   * {0, -1} down <br>
   * {1, 0} right <br>
   * {0, 1} up <br>
   */
  public static final int[][] SQUARE_DIRECTION_DIFFERENCES = {
      { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 }
  };

  /* Bag */

  /* constants for configurations */
  private static final String CONFIG_PATH = "config/";
  public static final String PATH_HABITAT_TILE_SQUARE = CONFIG_PATH + "configSquareHabitatTile.txt";
  public static final String PATH_KEYSTONE_TILE = CONFIG_PATH + "configKeystoneTile.txt";
  public static final String PATH_HABITAT_TILE_TWO_ANIMALS = CONFIG_PATH + "configHabitatTileTwoAnimals.txt";
  public static final String PATH_HABITAT_TILE_THREE_ANIMALS = CONFIG_PATH + "configHabitatTileThreeAnimals.txt";
  public static final String PATH_STARTER_HABITAT_TILE = CONFIG_PATH + "configStarterHabitatTile.txt";

  public static final int MAX_TILES_SQUARE = 50;
  public static final int MAX_TILES_HEXAGONAL = 85; /* 100 - 15 starter tiles */
  public static final int MAX_STARTER_HABITATS = 5; /* 15 = 5 * 3 (starter tiles) */
  public static final int MAX_TILES_ON_STARTER = 3; /* 3 animals on each starter habitat tiles */

  public static final int TILES_PER_PLAYER = 20;

  /* version hexagonal */
  public static final int MIN_PLAYERS = 1;
  public static final int MAX_PLAYERS = 4;

  /* exactly 3 tiles must remain on the game board in the end of the game */
  public static final int THREE = 3;

  /* Tokens */
  public static final int ANIMALS_HEXAGONAL = 20;
  public static final int ANIMALS_SQUARE = 10;
  public static final int MAX_ITERATION = 0xFF; /* some number to prevent infinity loop */

  /* useful methods across the project */
  public static boolean isValidVersion(int version) {
    return version >= VERSION_SQUARE &&
        version <= VERSION_HEXAGONAL;
  }

  public static boolean isValidCoordinates(int y, int x) {
    return true;
    // return y >= 0 && y < MAX_ROW &&
    // x >= 0 && x < MAX_COL;
  }

  public static boolean isValidNbPlayers(int nbPlayers) {
    return nbPlayers >= MIN_PLAYERS && nbPlayers <= MAX_PLAYERS;
  }

  public static boolean isInvalidSquareNbPlayers(int nbPlayers, int version) {
    return version != VERSION_HEXAGONAL && nbPlayers != NB_PLAYERS_SQUARE;
  }

  public static boolean isValidChoice(int choice) {
    return choice >= 1 && choice <= TOKENS_ON_BOARD;
  }

  /* Error messages, to improve later */
  // public static final String ILLEGAL_COORDINATES =
  // "Invalid coordinates, must be between (0, 0) and " +
  // "(" + MAX_ROW + ", " + MAX_COL + ")\n";

  public static final String ILLEGAL_NUMBER_OF_PLAYERS = "Number of Players must be between " + MIN_PLAYERS + " and "
      + MAX_PLAYERS + "\n";

  public static final String ILLEGAL_VERSION = "Version must be between " +
      VERSION_SQUARE + " and " + VERSION_HEXAGONAL + "\n";

  public static final String ILLEGAL_SQUARE_NUMBER_OF_PLAYERS = "Square Version must have exactly 2 players";

  /* Family and Intermidiate scoring cards */
  public static final int FAMILY_THREE_AND_PLUS = 9;
  public static final int INTERMEDIATE_FOUR_AND_PLUS = 12;

}
