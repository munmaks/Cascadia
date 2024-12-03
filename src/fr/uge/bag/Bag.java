package fr.uge.bag;

import fr.uge.environment.Tile;
import java.util.Random;

public sealed interface Bag permits BagSquare, BagHexagonal {
    // public Tile getRandomTile();
    default Tile getRandomTile() {
        var random = new Random();
        var randomIndex = random.nextInt(tiles.size()); /* in [0, tiles.size()[ */
        return tiles.remove(randomIndex);
    }
    public Tile[] getStarter();
}
