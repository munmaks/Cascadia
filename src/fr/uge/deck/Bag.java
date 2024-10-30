package fr.uge.deck;

import fr.uge.environment.KeystoneTile;
import fr.uge.environment.HabitatTile;
import fr.uge.environment.StarterHabitatTile;
import fr.uge.environment.WildlifeToken;
import fr.uge.environment.TileType;
import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public record Bag(ArrayList<Tile> tiles) {

  private static String filePathHabitatTileThreeAnimals = "src/config/configHabitatTileThreeAnimals.txt";
  private static String filePathHabitatTileTwoAnimals = "src/config/configHabitatTileTwoAnimals.txt";
  private static String filePathKeystoneTile = "src/config/configKeystoneTile.txt";
  private static String filePathStarterHabitatTile = "src/config/configStarterHabitatTile.txt";
  private static final int nbNeighbor = 6;

  public Bag {
//    private static String filepaththreeanimaltile = "src/config/configHabitatTileThreeAnimals.txt";
//    private static String filepathttwoanimaltile = "src/config/configHabitatTileTwoAnimals.txt";
//    private static String filepathkeystonetile = "src/config/configKeystoneTile.txt";
//    private static String filepathstarterhabitattile = "src/config/configStarterHabitatTile.txt";
//    var allkeystonetile = new ArrayList<KeystoneTile>();
//    var allhabitattile = new ArrayList<HabitatTile>();
//    var allstarterhabitattile = new ArrayList<StarterHabitatTile>();

//    try {
//      var rowthreeanimal = Files.readAllLines(Paths.get(filepaththreeanimaltile));
//      var rowtwoanimal = Files.readAllLines(Paths.get(filepathttwoanimaltile));
//      var rowkeystone = Files.readAllLines(Paths.get(filepathkeystonetile));
//      var rowstarter = Files.readAllLines(Paths.get(filepathstarterhabitattile));
//      for (String elem : rowthreeanimal ) {
//        String[] words = elem.split("\\s+");
//        var habit = new HabitatTile(TileType.valueOf(words[0]), TileType.valueOf(words[1]), new WildlifeToken(WildlifeType.valueOf(words[2])), new WildlifeToken(WildlifeType.valueOf(words[3])), new WildlifeToken(WildlifeType.valueOf(words[4])), new ArrayList<>(), -1, -1);
//        allhabitattile.add(habit);
//      }
//      for (String elem : rowtwoanimal ) {
//        String[] words = elem.split("\\s+");
//        var habit = new HabitatTile(TileType.valueOf(words[0]), TileType.valueOf(words[1]), new WildlifeToken(WildlifeType.valueOf(words[2])), new WildlifeToken(WildlifeType.valueOf(words[3])), null, new ArrayList<>(), -1, -1);
//        allhabitattile.add(habit);
//      }
//      for (String elem : rowkeystone ) {
//        String[] words = elem.split("\\s+");
//        var key = new KeystoneTile(TileType.valueOf(words[0]), new WildlifeToken(WildlifeType.valueOf(words[1])), -1, -1);
//        allkeystonetile.add(key);
//      }
//      for (String elem : rowstarter ) {
//        String[] words = elem.split("\\s+");
//        var starter = new StarterHabitatTile(new KeystoneTile(TileType.valueOf(words[0]), new WildlifeToken(WildlifeType.valueOf(words[1])), -1, -1),
//                new HabitatTile(TileType.valueOf(words[2]), TileType.valueOf(words[3]), new WildlifeToken(WildlifeType.valueOf(words[4])), new WildlifeToken(WildlifeType.valueOf(words[5])), null, new ArrayList<>(), -1, -1),
//                new HabitatTile(TileType.valueOf(words[6]), TileType.valueOf(words[7]), new WildlifeToken(WildlifeType.valueOf(words[8])), new WildlifeToken(WildlifeType.valueOf(words[9])), new WildlifeToken(WildlifeType.valueOf(words[10])), new ArrayList<>(), -1, -1));
//        allstarterhabitattile.add(starter);
//      }
//      System.out.println("its ok");
//    } catch (IOException e) {
//      System.err.println(e.getMessage());
//      System.exit(1);
//    }
  }

    /**
     * add all habitats tiles in `tiles`.
     * */
    public void readHabitatTilesThreeAnimals() throws IOException {
      var allThreeAnimals = Files.readAllLines(Paths.get(filePathHabitatTileThreeAnimals));
      for (var row : allThreeAnimals) {
        var words = row.split("\\s+");
        var habitats = new TileType[] {
            TileType.valueOf(words[0]),
            TileType.valueOf(words[1])
        };
        var animals = new WildlifeType[]{
            WildlifeType.valueOf(words[2]),     // first animal
            WildlifeType.valueOf(words[3]),     // second animal
            WildlifeType.valueOf(words[4])      // third animal
        };
        var habit = new HabitatTile(habitats, animals, new Tile[nbNeighbor], -1, -1);
        tiles.add(habit);
      }
    }

    /**
     * add all habitats tiles in `tiles`.
     * */
    public void readHabitatTilesTwoAnimals() throws IOException {
      var allTwoAnimals = Files.readAllLines(Paths.get(filePathHabitatTileTwoAnimals));
      for (var row : allTwoAnimals) {
        var words = row.split("\\s+");
        var habitats = new TileType[]{
            TileType.valueOf(words[0]),
            TileType.valueOf(words[1])
        };
        var animals = new WildlifeType[]{
            WildlifeType.valueOf(words[2]),     // first animal
            WildlifeType.valueOf(words[3]),     // second animal
        };
        var habit = new HabitatTile(habitats, animals, new Tile[nbNeighbor], -1, -1);
        tiles.add(habit);
      }
    }

    
    /**
     * add all keystone tiles in `tiles`.
     * */
    public void readKeystoneTiles() throws IOException {
      var allKeystones = Files.readAllLines(Paths.get(filePathKeystoneTile));
      for (var row : allKeystones) {
        var words = row.split("\\s+");
        var habit = new KeystoneTile(
            TileType.valueOf(words[0]),                         // one habitat
            WildlifeType.valueOf(words[1]),  // one animal
            new Tile[nbNeighbor],
            -1, -1
        );
        tiles.add(habit);
      }
    }

    
    
    /**
     * add all starter habitat tiles in `tiles`.
     * */
    public void readStarterHabitatTiles() throws IOException {
      /*
      source: github/Cascadia/docs/Cascadia_rules_english.pdf
      KeystoneTile topTile  - 1 habitat, 1 animal
      HabitatTile leftTile  - 2 habitats, 3 animal
      HabitatTile rightTile - 2 habitats, 2 animal
      */
      // Need to devide this methode in future
      var allStarterHabitats = Files.readAllLines(Paths.get(filePathStarterHabitatTile));
      for (var row : allStarterHabitats) {
        var words = row.split("\\s+");
        var topTile = new KeystoneTile(
            TileType.valueOf(words[0]),                         // one habitat
            WildlifeType.valueOf(words[1]),  // one animal
            new Tile[nbNeighbor],
            -1, -1
        );

        var leftHabitats = new TileType[]{
            TileType.valueOf(words[2]),
            TileType.valueOf(words[3])
        };
        var leftAnimals = new WildlifeType[]{
            WildlifeType.valueOf(words[4]),     // first animal
            WildlifeType.valueOf(words[5]),     // second animal
            WildlifeType.valueOf(words[6]),     // third animal
        };
        var leftTile = new HabitatTile(leftHabitats, leftAnimals, new Tile[nbNeighbor], -1, -1);

        
        var rightHabitats = new TileType[]{
            TileType.valueOf(words[7]),
            TileType.valueOf(words[8])
        };
        var rightAnimals = new WildlifeType[]{
            WildlifeType.valueOf(words[9]),     // first animal
            WildlifeType.valueOf(words[10]),     // second animal
        };
        var rightTile = new HabitatTile(rightHabitats, rightAnimals, new Tile[nbNeighbor], -1, -1);

        tiles.add(new StarterHabitatTile(topTile, leftTile, rightTile));
      }
    }
    
    public static void main(String[] args) {
      var tiles = new ArrayList<Tile>();
      Bag bag = new Bag(tiles);
//      System.out.println(bag.allhabitattile());
  //    System.out.println(bag.allkeystonetile());
  //    System.out.println(allstarterhabitattile);
  //    System.out.println(bag);
    }

}