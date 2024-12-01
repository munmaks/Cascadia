package fr.uge.core;

import fr.uge.bag.Bag;
import fr.uge.bag.Deck;
import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeToken;
import fr.uge.util.Constants;
import java.util.HashMap;
import java.util.Objects;

/**
 * This method is used to initialise the tiles and tokens on the board
 */
public final class GameBoard {
  private final Bag bag;
  private final Deck deck;

  private static final HashMap<WildlifeToken, Integer> map = new HashMap<>();
  private boolean areUpdated = false;   /* update only once per player's turn */
  
  private final Tile[] tiles = new Tile[Constants.TOKENS_ON_BOARD];
  private final WildlifeToken[] tokens = new WildlifeToken[Constants.TOKENS_ON_BOARD];

  // private int indexOfTokenToUpdate = 0;

  /* for later usage */
  // private static final WildlifeScoringCard[] scoringCards = new WildlifeScoringCard[Constants.NB_SCORING_CARDS];


  /*
   *    * <p>Indexes for WildlifeType `enum`:</p>
   * <ul>
   * <li>BEAR   : 0</li>
   * <li>ELK    : 1</li>
   * <li>HAWK   : 2</li>
   * <li>FOX    : 3</li>
   * <li>SALMON : 4</li>
   * </ul>
   * 
  */

  public GameBoard(int nbPlayers, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    if (Constants.isInvalidSquareNbPlayers(nbPlayers, version)) {
      throw new IllegalArgumentException(Constants.IllegalSquareNbPlayers);
    }
    this.bag = new Bag(nbPlayers, version);
    this.deck = new Deck(version);
    for (var i = 0; i < Constants.TILES_ON_BOARD; ++i) {
      this.tiles[i] = bag.getRandomTile();
    }
    for (var i = 0; i < Constants.TOKENS_ON_BOARD; ++i) {
      this.tokens[i] = deck.getRandomToken();
    }
  }
  
  /**
   * This method is used to determine if the tokens on the board need to be updated.
   */
  public final boolean tokensNeedUpdate() {
    if (areUpdated) { return false; }   /* already updated */
    /* count the number of occurences of each token,
       and store it in a map clear it before using it */
    map.clear();
    for (var i = 0; i < tokens.length; ++i) {
      map.put(tokens[i], map.getOrDefault(tokens[i], 0) + 1);
    }
    /* check if there we have 3 or 4 occurences of a token */
    for (var elem : map.entrySet()) {
      if (elem.getValue() == 4) {
        areUpdated = true;
        return true;
      }
    }
    return false;
  }




  public final boolean tokensCanBeUpdated() {
    if (areUpdated) { return false; }   /* already updated */
    /* count the number of occurences of each token,
       and store it in a map clear it before using it */
    map.clear();
    for (var i = 0; i < tokens.length; ++i) {
      map.put(tokens[i], map.getOrDefault(tokens[i], 0) + 1);
    }
    /* check if there we have 3 or 4 occurences of a token */
    for (var elem : map.entrySet()) {
      if (elem.getValue() >= 3) { return true; }
    }
    return false;
  }


  /* to add later: using nature tokens for every player */
  
  
  public final boolean areTokensUpdated() {
    return areUpdated;
  }
  
  /**
   * Turn Manager switch 
   * */
  public final void setDefaultAreUpdate() {
    areUpdated = false;
  }
  
  
  private WildlifeToken updateToken(WildlifeToken token) {
    Objects.requireNonNull(token);
    return deck.updateToken(token);
  }
  
  // FOR TESTS, to delete later
  // private void testUpdateTokens() {
  //   for (var i = 0; i < tokens.length; ++i) {
  //     tokens[i] = updateToken(tokens[i]);
  //   }
  // }
  
  
  public final void updateTokens(){
    if (areUpdated) {
      System.err.println("Tokens are already updated, wait next turn please");
      return;
    }

    // there will be always only one token with 3 or more occurences
    WildlifeToken tokenToChange = map.entrySet().stream()
          .filter(entry -> entry.getValue() >= 3)
          .map(entry -> entry.getKey())
          .findFirst()
          .orElse(null);

    // if there's no token with 3 or more occurrences, no update is needed
    if (tokenToChange == null) { return; }

    for (var i = 0; i < tokens.length; ++i) {
      if (tokens[i].equals(tokenToChange)) { tokens[i] = updateToken(tokens[i]); }
    }
    areUpdated = true;
  }


  public final Tile[] getCopyOfTiles() {
    return tiles.clone();   // we sent a copy
  }

  public final WildlifeToken[] getCopyOfTokens() {
    return tokens.clone();  // we sent a copy
  }
  

  public Tile getTile(int index){
    if (index < 0 || index >= Constants.TILES_ON_BOARD) {
      throw new IllegalArgumentException("Index out of bounds");
    }
    // System.err.println(tiles[index]);
    var tile = tiles[index];
    tiles[index] = bag.getRandomTile();  /* replace the tile */
    return tile;
  }


  public WildlifeToken getToken(int index){
    if (index < 0 || index >= Constants.TOKENS_ON_BOARD) {
      throw new IllegalArgumentException("Index out of bounds");
    }
    // System.err.println(tokens[index]);
    var token = tokens[index];
    tokens[index] = deck.getRandomToken();  /* replace the token */
    return token;
  }

  public Bag getBag() {
    return bag;
  }

  public Deck getDeck() {
    return deck;
  }

  // public static void main(String[] args) {
  //   GameBoard gb = new GameBoard(2, 1);
  //   System.out.println("Tokens before update:");

  //   while (!gb.tokensNeedUpdate()) {
  //     gb.testUpdateTokens();
  //   }
  //   for (var i = 0; i < Constants.TOKENS_ON_BOARD; ++i) {
  //     System.out.println(tokens[i]);
  //   }
  //   System.out.println("Tokens need update: " + gb.tokensNeedUpdate());
    
  //   gb.updateTokens();
  //   System.out.println("Tokens after update:");
  //   for (var i = 0; i < Constants.TOKENS_ON_BOARD; ++i) {
  //     System.out.println(tokens[i]);
  //   }

  // }

}


