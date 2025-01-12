package fr.uge.data.bag;

import fr.uge.data.environment.Tile;
import fr.uge.data.environment.WildlifeType;

public sealed interface Bag permits SquareBag, HexagonalBag {
    Tile[] getStarter();

    Tile getRandomTile();

    WildlifeType getRandomToken();

    WildlifeType updateToken(WildlifeType token);
}
