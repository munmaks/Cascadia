package fr.uge.data.util;

public final class Constants {

  /* prevent instantiation of this utility class */
  private Constants() {}

  /* Game */
  public static final int NB_PLAYERS_SQUARE = 2;

  /* Turn Manager */
  public static final int MAX_GAME_TURNS = 20;

  /* Game Board */
  public static final int TOKENS_ON_BOARD = 4;
  public static final int TILES_ON_BOARD = 4;
  public static final int NB_SCORING_CARDS = 5;

  public static final int NB_NEIGHBORS_SQUARE = 4;
  public static final int NB_NEIGHBORS_HEXAGONAL = 6;
  public static final int MAX_ROTATIONS = 6; /* for hexagonals */

  public static final int VERSION_SQUARE = 1;
  public static final int VERSION_HEXAGONAL = 3;

  public static final int NB_TOKENS = 5;

  /* Bag */

  /* constants for configurations */
  private static final String CONFIG_PATH = "config/";
  public static final String PATH_HABITAT_TILE_SQUARE = CONFIG_PATH + "configSquareHabitatTile.txt";
  public static final String PATH_KEYSTONE_TILE = CONFIG_PATH + "configKeystoneTile.txt";
  public static final String PATH_HABITAT_TILE_TWO_ANIMALS = CONFIG_PATH
      + "configHabitatTileTwoAnimals.txt";
  public static final String PATH_HABITAT_TILE_THREE_ANIMALS = CONFIG_PATH
      + "configHabitatTileThreeAnimals.txt";
  public static final String PATH_STARTER_HABITAT_TILE = CONFIG_PATH
      + "configStarterHabitatTile.txt";

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

  /**
   * Check if the version is valid
   * 
   * @param version the version
   * @return true if the version is valid, false otherwise
   */
  public static boolean isValidVersion(int version) {
    return version >= VERSION_SQUARE && version <= VERSION_HEXAGONAL;
  }

  /**
   * Check if the number of players is valid
   * 
   * @param numberOfPlayers the number of players
   * @return true if the number of players is valid, false otherwise
   */
  public static boolean isValidNbPlayers(int numberOfPlayers) {
    return numberOfPlayers >= MIN_PLAYERS && numberOfPlayers <= MAX_PLAYERS;
  }

  /**
   * Check if the square version has exactly 2 players
   * 
   * @param numberOfPlayers the number of players
   * @param version         the version
   * @return true if the square version has exactly 2 players, false otherwise
   */
  public static boolean isInvalidSquareNbPlayers(int numberOfPlayers, int version) {
    return version != VERSION_HEXAGONAL && numberOfPlayers != NB_PLAYERS_SQUARE;
  }

  /**
   * Check if the choice is valid
   * 
   * @param choice the choice
   * @return true if the choice is valid, false otherwise
   */
  public static boolean isValidChoice(int choice) {
    return choice >= 1 && choice <= TOKENS_ON_BOARD;
  }

  public static final String ILLEGAL_NUMBER_OF_PLAYERS = "Number of Players must be between "
      + MIN_PLAYERS + " and " + MAX_PLAYERS + "\n";

  public static final String ILLEGAL_VERSION = "Version must be between " + VERSION_SQUARE + " and "
      + VERSION_HEXAGONAL + "\n";

  public static final String ILLEGAL_SQUARE_NUMBER_OF_PLAYERS = "Square Version must have exactly 2 players";

  /* Family and Intermidiate scoring cards */
  public static final int FAMILY_THREE_AND_PLUS = 9;
  public static final int INTERMEDIATE_FOUR_AND_PLUS = 12;

}
