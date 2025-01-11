package fr.uge.bag;


import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;
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
    // int currentNumberOfTiles = maxTilesForGame;
    // while (currentNumberOfTiles > maxTilesTotal) {
    // --currentNumberOfTiles;
    // tiles.remove(0); // Remove the first element
    // }
    // tiles.stream()
    // .limit(maxTilesForGame - maxTilesTotal)
    // .forEach(tile -> tiles.remove(0));

    var random = new Random();
    tiles.removeIf(_ -> tiles.size() > maxTilesForGame && random.nextBoolean());
  }

  public static Tile getRandomTile(LinkedList<Tile> tiles) { return tiles.removeFirst(); }

  public static WildlifeType getRandomToken(int[] animals) {
    return getRandomTokenStream(animals).findFirst().orElseGet(() -> getFallbackToken(animals));
  }

  /*
   * public static Tile getRandomTile(ArrayList<Tile> tiles) { var random = new
   * Random(); var randomIndex = random.nextInt(tiles.size()); // [0,
   * tiles.size()) return tiles.remove(randomIndex); }
   */
  private static Stream<WildlifeType> getRandomTokenStream(int[] animals) {
    var random = new Random();
    return IntStream.range(0, Constants.MAX_ITERATION).mapToObj(_ -> random.nextInt(animals.length))
        .filter(index -> animals[index] > 0).peek(index -> animals[index]--)
        .map(index -> WildlifeType.values()[index]);
  }

  private static WildlifeType getFallbackToken(int[] animals) {
    return Arrays.stream(WildlifeType.values()).filter(animal -> animals[animal.ordinal()] > 0)
        .findFirst().orElseThrow(
            () -> new IllegalStateException("No tokens available, game over!")); /* to be sure */
  }

  public static WildlifeType updateToken(WildlifeType token, int[] animals) {
    var index = token.ordinal();
    animals[index]++; /* return into deck current token */
    return getRandomToken(animals);
  }

  public static void readTiles(String path, Consumer<String[]> tileConsumer) throws IOException {
    try(var reader=Files.newBufferedReader(Paths.get(path))) {
      reader.lines().map(line -> line.split("\\s+")).forEach(tileConsumer);
    }
  }

}
