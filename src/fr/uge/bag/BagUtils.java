package fr.uge.bag;


import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;



final class BagUtils {

  /*
   * To prevent instantiation
   */
  private BagUtils() {
    throw new IllegalStateException("Utility class, cannot be instantiated");
  }

  /**
   * reduce the number of tiles in a given list to match maxTilesForGame
   *
   * @param tiles the list of tiles that we actualise
   * @param maxTilesForGame the maximum number of tiles allowed in the game
   */
  public static void decreaseNumberOfTiles(
      LinkedList<Tile> tiles,
      int maxTilesForGame
    ) {
    // int currentNumberOfTiles = maxTilesForGame;
    // while (currentNumberOfTiles > maxTilesTotal) {
    //   --currentNumberOfTiles;
    //   tiles.remove(0);  // Remove the first element
    // }
    // tiles.stream()
    //      .limit(maxTilesForGame - maxTilesTotal)
    //      .forEach(tile -> tiles.remove(0));

    var random = new Random();
    tiles.removeIf(_ -> tiles.size() > maxTilesForGame && random.nextBoolean());
  }

  /**
   * take the first tile in tiles who have been randomized
   *
   * @param tiles the list of tiles
   * @return the first tile of the randomised LinkedList tiles
   */
  public static Tile getRandomTile(LinkedList<Tile> tiles) {
    return tiles.removeFirst();
  }


  /**
   * take a random animal in animals
   *
   * @param animals the list of animals
   * @return a random tile of animals
   */
  public static WildlifeType getRandomToken(int[] animals) {
    return getRandomTokenStream(animals).findFirst()
              .orElseGet(() -> getFallbackToken(animals));
  }


  /*
  public static Tile getRandomTile(ArrayList<Tile> tiles) {
    var random = new Random();
    var randomIndex = random.nextInt(tiles.size()); // [0, tiles.size())
    return tiles.remove(randomIndex);
  }
   */

  /**
   * create a stream of random `WildlifeType` tokens based on animals
   *
   * @param animals a list representing the counts of each type of animal
   * @return a `Stream<WildlifeType>` representing the randomly selected animal tokens
   */
  private static Stream<WildlifeType> getRandomTokenStream(int[] animals) {
    var random = new Random();
    return IntStream.range(0, Constants.MAX_ITERATION)
                    .mapToObj(_ -> random.nextInt(animals.length))
                    .filter(index -> animals[index] > 0)
                    .peek(index -> animals[index]--)
                    .map(index -> WildlifeType.values()[index]);
  }

  /**
   * retrieve a WildlifeType token from animals
   *
   * @param animals a list representing the counts of each type of animal
   * @return the first available `WildlifeType` based on the provided `animals` counts
   */
  private static WildlifeType getFallbackToken(int[] animals) {
    return Arrays.stream(WildlifeType.values())
                 .filter(animal -> animals[animal.ordinal()] > 0)
                 .findFirst()
                 .orElseThrow(() -> new IllegalStateException("No tokens available, game over!")); /* to be sure */
  }


  /**
   * replace the old WildlifeType of the token by a new one chose randomly
   *
   * @param token the token that we want to update
   * @param animals a list representing the counts of each type of animal
   * @return the new WildlifeType of the token
   */
  public static WildlifeType updateToken(WildlifeType token, int[] animals) {
    var index = token.ordinal();
    animals[index]++;   /* return into deck current token */
    return getRandomToken(animals);
  }

  /**
   * reads the tile data from a path and split each line into an array of string
   *
   * @param path the path to the file that contains the tile data
   * @param tileConsumer a `Consumer<String[]>` that processes each line of tile data
   * @throws IOException if an error happen while reading the file
   */
  public static void readTiles(String path, Consumer<String[]> tileConsumer) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
      reader.lines()
            .map(line -> line.split("\\s+"))
            .forEach(tileConsumer);
    }
  }

}
