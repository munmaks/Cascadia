package fr.uge.data.bag;


import fr.uge.data.environment.Tile;
import fr.uge.data.environment.WildlifeType;
import fr.uge.data.util.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class UtilsBag {

  /*
   * To prevent instantiation
   */
  private UtilsBag() { throw new IllegalStateException("Utility class, cannot be instantiated"); }

  public static void decreaseNumberOfTiles(LinkedList<Tile> tiles, int maxTilesForGame) {
    var random = new Random();
    tiles.removeIf(_ -> tiles.size() > maxTilesForGame && random.nextBoolean());
  }

  /**
   * Get a random tile from the list of tiles
   * 
   * @param tiles list of tiles
   * @return a random tile
   */
  public static Tile getRandomTile(LinkedList<Tile> tiles) { return tiles.removeFirst(); }

  /**
   * Get a random token from the list of tokens
   * 
   * @param animals list of tokens
   * @return a random token
   */
  public static WildlifeType getRandomToken(int[] animals) {
    return getRandomTokenStream(animals).findFirst().orElseGet(() -> getFallbackToken(animals));
  }

  /**
   * Get a random token stream
   * 
   * @param animals list of tokens
   * @return a stream of random tokens
   */
  private static Stream<WildlifeType> getRandomTokenStream(int[] animals) {
    var random = new Random();
    return IntStream.range(0, Constants.MAX_ITERATION).mapToObj(_ -> random.nextInt(animals.length))
        .filter(index -> animals[index] > 0).peek(index -> animals[index]--)
        .map(index -> WildlifeType.values()[index]);
  }

  /**
   * Get a fallback token
   * 
   * @param animals list of tokens
   * @return a fallback token
   */
  private static WildlifeType getFallbackToken(int[] animals) {
    return Arrays.stream(WildlifeType.values()).filter(animal -> animals[animal.ordinal()] > 0)
        .findFirst().orElseThrow(
            () -> new IllegalStateException("No tokens available, game over!")); /* to be sure */
  }

  /**
   * Update the token
   * 
   * @param token
   * @param animals
   * @return a new token
   */
  public static WildlifeType updateToken(WildlifeType token, int[] animals) {
    var index = token.ordinal();
    animals[index]++; /* return into deck current token */
    return getRandomToken(animals);
  }

  /**
   * Read tiles from a file
   * 
   * @param path         file path
   * @param tileConsumer consumer
   * @throws IOException if an I/O error occurs
   */
  public static void readTiles(String path, Consumer<String[]> tileConsumer) throws IOException {
    try(var reader=Files.newBufferedReader(Paths.get(path))) {
      reader.lines().map(line -> line.split("\\s+")).forEach(tileConsumer);
    }
  }

}
