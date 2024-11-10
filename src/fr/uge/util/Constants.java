package fr.uge.util;

//import java.util.ArrayList;

public final class Constants {

  /* prevent instantiation of this utility class */
  private Constants() { }

  /* Game */
  public static final int MAX_GAME_TURNS = 20;
  public static final int NB_PLAYERS_SQUARE = 2;


  /* Environment */
  public static final int MAX_SIZE = 12;  /* grid size, 10 - 14 */
  public static final int NB_NEIGHBORS_SQUARE = 4;
  public static final int NB_NEIGHBORS_HEXAGONAL = 6;
  public static final int MAX_ROTATIONS = 6;

  public static final int VERSION_SQUARE = 1;
  public static final int VERSION_HEXAGONAL = 3;

  
  
  /* Bag */

  /* constants for configurations */
  private static final String CONFIG_PATH = "src/config/";
  public static final String PATH_SQUARE_HABITAT_TILE        = CONFIG_PATH + "configSquareHabitatTile.txt";
  public static final String PATH_KEYSTONE_TILE              = CONFIG_PATH + "configKeystoneTile.txt";
  public static final String PATH_HABITAT_TILE_TWO_ANIMALS   = CONFIG_PATH + "configHabitatTileTwoAnimals.txt";
  public static final String PATH_HABITAT_TILE_THREE_ANIMALS = CONFIG_PATH + "configHabitatTileThreeAnimals.txt";
  public static final String PATH_STARTER_HABITAT_TILE       = CONFIG_PATH + "configStarterHabitatTile.txt";

  public static final int MAX_TILES_SQUARE = 50;
  public static final int MAX_TILES_HEXAGONAL = 85;      /* 100 - 15 starter tiles */
  public static final int MAX_STARTER_HABITATS = 5;      /* 15 = 5 * 3 (starter tiles) */

  public static final int TILE_PER_PLAYER = 20;

  public static final int MIN_PLAYERS = 1;
  public static final int MAX_PLAYERS = 4;

  /* exactly 3 tiles must remain on the game board in the end of the game */
  public static final int THREE = 3;

  
  
  /* Deck */
  public static final int ANIMAL_TOKENS = 20;
}

