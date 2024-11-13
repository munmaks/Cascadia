package fr.uge.core;

import java.util.HashMap;
import java.util.Objects;
import fr.uge.bag.Bag;
import fr.uge.bag.Deck;

import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;
import fr.uge.scoring.WildlifeScoringCard;
import fr.uge.util.Constants;



public final class GameBoard {
  private static Bag bag = null;
  private static Deck deck = null;
  private static final WildlifeScoringCard[] scoringCards = new WildlifeScoringCard[5]; // to add later Constants.SCORING_CARDS
  private static final WildlifeToken[] tokens = new WildlifeToken[Constants.TOKENS_ON_BOARD];
  private static final HashMap<WildlifeToken, Integer> map = new HashMap<WildlifeToken, Integer>();
  private static int indexOfTokenToUpdate = 0;
  
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
   * This method is used to determine if the tokens on the board need to be updated.
   */
  public final boolean tokensNeedUpdate() {
    // count the number of occurences of each token, and store it in a map, but clear it first
    map.clear();
    for (var i = 0; i < tokens.length; ++i) {
      map.put(
          tokens[i],
          map.getOrDefault(tokens[i], 0) + Integer.valueOf(1)
        );
    }
    // check if there we have 3 or 4 occurences of a token
    for (var elem : map.entrySet()) {
      if (elem.getValue() >= 3) {
        return true;
      }
    }
    return false;
  }

  private static WildlifeToken updateToken(WildlifeToken token) {
    Objects.requireNonNull(token);
    return deck.updateToken(token);
  }
  

  public final void updateTokens(){
    /* if token to change occurs 3 or more times, change it
       to improve code later */
    WildlifeToken tokenToChange = null; // new WildlifeToken(WildlifeType.BEAR);
    for (var elem : map.entrySet()) {
      if (elem.getValue() >= 3) {
        tokenToChange = elem.getKey();
        break;
      }
    }
    // there will be always only one token with 3 or more occurences
    // WildlifeToken tokenToChange = map.entrySet().stream()
    //     .filter(entry -> entry.getValue() >= 3)
    //     .map(entry -> entry.getKey())
    //     .findFirst()
    //     .orElse(null);

    // If there's no token with 3 or more occurrences, no update is needed
    if (tokenToChange == null) {
      return;
    }

    for (var i = 0; i < tokens.length; ++i) {
      if (tokens[i].equals(tokenToChange)) {
        tokens[i] = updateToken(tokens[i]);
      }
    }
  }


  public static void main(String[] args) {
    GameBoard gb = new GameBoard(2, 3);
    System.out.println("Tokens before update:");
    for (var i = 0; i < Constants.TOKENS_ON_BOARD; ++i) {
      System.out.println(tokens[i]);
    }
    System.out.println("Tokens need update: " + gb.tokensNeedUpdate());

    gb.updateTokens();
    System.out.println("Tokens after update:");
    for (var i = 0; i < Constants.TOKENS_ON_BOARD; ++i) {
      System.out.println(tokens[i]);
    }
  }

}


