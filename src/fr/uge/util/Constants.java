package fr.uge.util;

//import java.util.ArrayList;

public final class Constants {

  /* prevent instantiation of this utility class */
  private Constants() { }

  /* Game */
  public static final int MAX_GAME_TURNS = 20;
  public static final int NB_PLAYERS_SQUARE = 2;


  /* Environment */
  // public static final int MAX_SIZE = 12;  /* grid size, 10 - 14 */
  public static final int MAX_ROW = 12;
  public static final int MAX_COL = 12;
  
  public static final int NB_NEIGHBORS_SQUARE = 4;
  public static final int NB_NEIGHBORS_HEXAGONAL = 6;
  public static final int MAX_ROTATIONS = 6;

  public static final int VERSION_SQUARE = 1;
  public static final int VERSION_HEXAGONAL = 3;

  
  
  /**<b>
   * Direction offsets based on "odd-r" layout<br>
   *       (x, y)<br><br>
   *       even rows<br>
   *      {<br>
   *       (-1, -1), (-1,  0), (-1,  1), left habitat: (left up, left, left down)<br>
   *       ( 0,  1), ( 1,  0), ( 0,  -1) right habitat: (right down, right, right up)<br>
   *      }<br><br>
   *
   *       odd rows<br>
   *     {<br>
   *      ( 0, -1), (-1,  0), ( 0,  1), left habitat: (left up, left, left down)<br>
   *      ( 1,  1), ( 1,  0), ( 1, -1)  right habitat: (right down, right, right up)<br>
   *     }<br><br>
   * Source: https://www.redblobgames.com/grids/hexagons/#neighbors-offset
   * </b>
   * */
  public static final int[][][] HEXAGONE_DIRECTION_DIFFERENCES = {
         {
           {-1, -1}, {-1,  0}, {-1,  1},
           { 0,  1}, { 1,  0}, { 0,  -1}
         },
         {
           { 0, -1}, {-1,  0}, { 0,  1},
           { 1,  1}, { 1,  0}, { 1, -1}
         }
  };

  /**
   * <b>
   *    (x, y) <br>
   *   {0, -1}  down <br>
   *   {0, 1}   up   <br>
   *   {-1, 0}  left <br>
   *   {1, 0}   right <br>
   * </b>
   * */
  public static final int [][] SQUARE_DIRECTION_DIFFERENCES = {
      { 0, -1}, { 0,  1}, {-1,  0}, { 1,  0}
  };
  
  
  

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
  
  
  public static boolean isValidVersion(int version) {
    return version >= VERSION_SQUARE &&
           version <= VERSION_HEXAGONAL;
  }
  
  public static boolean isValidCoordinates(int x, int y) {
    return (y >= 0 && y < MAX_ROW) &&
           (x >= 0 && x < MAX_COL);
  }
  
}


