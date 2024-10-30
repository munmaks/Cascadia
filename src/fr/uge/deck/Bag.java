package fr.uge.deck;

import fr.uge.environment.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public record Bag() {

  public Bag {
    ArrayList<KeystoneTile> allkeystonetile = new ArrayList<KeystoneTile>();
    ArrayList<HabitatTile> allhabitattile = new ArrayList<HabitatTile>();
    ArrayList<StarterHabitatTile> allstarterhabitattile = new ArrayList<StarterHabitatTile>();
    String filepaththreeanimaltile = "src/config/configHabitatTileThreeAnimals.txt";
    String filepathttwoanimaltile = "src/config/configHabitatTileTwoAnimals.txt";
    String filepathkeystonetile = "src/config/configKeystoneTile.txt";
    String filepathstarterhabitattile = "src/config/configStarterHabitatTile.txt";
    try {
      List<String> rowthreeanimal = Files.readAllLines(Paths.get(filepaththreeanimaltile));
      List<String> rowtwoanimal = Files.readAllLines(Paths.get(filepathttwoanimaltile));
      List<String> rowkeystone = Files.readAllLines(Paths.get(filepathkeystonetile));
      List<String> rowstarter = Files.readAllLines(Paths.get(filepathstarterhabitattile));
      for (String elem : rowthreeanimal ) {
        String[] words = elem.split("\\s+");
        var habit = new HabitatTile(TileType.valueOf(words[0]), TileType.valueOf(words[1]), new WildlifeToken(WildlifeType.valueOf(words[2])), new WildlifeToken(WildlifeType.valueOf(words[3])), new WildlifeToken(WildlifeType.valueOf(words[4])), new ArrayList<>(), -1, -1);
        allhabitattile.add(habit);
      }
      for (String elem : rowtwoanimal ) {
        String[] words = elem.split("\\s+");
        var habit = new HabitatTile(TileType.valueOf(words[0]), TileType.valueOf(words[1]), new WildlifeToken(WildlifeType.valueOf(words[2])), new WildlifeToken(WildlifeType.valueOf(words[3])), null, new ArrayList<>(), -1, -1);
        allhabitattile.add(habit);
      }
      for (String elem : rowkeystone ) {
        String[] words = elem.split("\\s+");
        var key = new KeystoneTile(TileType.valueOf(words[0]), new WildlifeToken(WildlifeType.valueOf(words[1])), -1, -1);
        allkeystonetile.add(key);
      }
      for (String elem : rowstarter ) {
        String[] words = elem.split("\\s+");
        var starter = new StarterHabitatTile(new KeystoneTile(TileType.valueOf(words[0]), new WildlifeToken(WildlifeType.valueOf(words[1])), -1, -1),
                new HabitatTile(TileType.valueOf(words[2]), TileType.valueOf(words[3]), new WildlifeToken(WildlifeType.valueOf(words[4])), new WildlifeToken(WildlifeType.valueOf(words[5])), null, new ArrayList<>(), -1, -1),
                new HabitatTile(TileType.valueOf(words[6]), TileType.valueOf(words[7]), new WildlifeToken(WildlifeType.valueOf(words[8])), new WildlifeToken(WildlifeType.valueOf(words[9])), new WildlifeToken(WildlifeType.valueOf(words[10])), new ArrayList<>(), -1, -1));
        allstarterhabitattile.add(starter);
      }
    } catch (IOException e) {
			System.err.println(e.getMessage());
      System.exit(1);
		}
    //System.out.println(allhabitattile);
    //System.out.println(allkeystonetile);
    //System.out.println(allstarterhabitattile);
	}
  public static void main(String[] args) {
    Bag bag = new Bag();
  }
}