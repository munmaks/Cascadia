package fr.uge.bag;

import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeType;

public sealed interface Bag permits SquareBag, HexagonalBag {
    Tile[] getStarter();

    Tile getRandomTile();

    WildlifeType getRandomToken();

    WildlifeType updateToken(WildlifeType token);
    // default Tile getRandomTile() {
    // var random = new Random();
    // var randomIndex = random.nextInt(tiles.size()); /* in [0, tiles.size()[ */
    // return tiles.remove(randomIndex);
    // }
}
