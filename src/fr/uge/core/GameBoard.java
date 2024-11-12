package fr.uge.core;

import java.util.HashMap;

import fr.uge.bag.Bag;
import fr.uge.bag.Deck;

import fr.uge.environment.WildlifeToken;

import fr.uge.util.Constants;



public final class GameBoard {
  
  private static Bag bag;
  private static Deck deck;
  
  private static final WildlifeToken[] tokens = new WildlifeToken[Constants.TOKENS_ON_BOARD];
  private static int index = 0;
  
  public GameBoard(int nbPlayers, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    bag = new Bag(nbPlayers, version);
    deck = new Deck(version);
    for (var i = 0; i < Constants.TOKENS_ON_BOARD; ++i) {
      tokens[i] = deck.getRandomToken();
    }
    
  }
  
  /**
   * <p>Indexes for WildlifeType `enum`:</p>
   * <ul>
   * <li>BEAR   : 0</li>
   * <li>ELK    : 1</li>
   * <li>HAWK   : 2</li>
   * <li>FOX    : 3</li>
   * <li>SALMON : 4</li>
   * </ul>
   */
//  public final boolean canBeUpdateTokens() {
//    var map = new HashMap<WildlifeToken, Integer>();
//    
//    for (var i = 0; i < tokens.length; ++i) {
//      map.put(tokens[i], map.getOrDefault(tokens[i], 1));
//      // [tokens[i].animal().ordinal()] += 1;
//    }
//    for (var elem : map) {
//      
//    }
//    
//  }
//  
}
