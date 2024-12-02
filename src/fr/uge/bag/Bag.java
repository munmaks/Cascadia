package fr.uge.bag;

import fr.uge.environment.Tile;


public sealed interface Bag permits BagSquare, BagHexagonal {
    public Tile getRandomTile();
    public Tile[] getStarter();
}
