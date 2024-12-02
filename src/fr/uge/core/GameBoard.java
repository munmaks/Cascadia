package fr.uge.core;

import fr.uge.bag.Bag;
import fr.uge.bag.BagHexagonal;
import fr.uge.bag.BagSquare;
import fr.uge.bag.Deck;
import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeToken;
import fr.uge.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This method is used to initialise the tiles and tokens on the board
 */
public final class GameBoard {
  private final Bag bag;
  private final Deck deck;

  private boolean tokensAreUpdated = false;   /* update only once per player's turn */

  private static final HashMap<WildlifeToken, Integer> map = new HashMap<>();

  private static final ArrayList<Tile> tiles           = new ArrayList<>(Constants.TILES_ON_BOARD);
  private static final ArrayList<WildlifeToken> tokens = new ArrayList<>(Constants.TOKENS_ON_BOARD);

  // private int indexOfTokenToUpdate = 0;

  /* for later usage */
  // private static final WildlifeScoringCard[] scoringCards = new WildlifeScoringCard[Constants.NB_SCORING_CARDS];


  /*
   * <p>Indexes for WildlifeType `enum`:</p>
   * <ul>
   * <li>BEAR   : 0</li>
   * <li>ELK    : 1</li>
   * <li>HAWK   : 2</li>
   * <li>FOX    : 3</li>
   * <li>SALMON : 4</li>
   * </ul>
  */
  /**
   * This constructor is used to initialise the tiles and tokens on the board.
   * @param nbPlayers the number of players in the game
   * @param version the version of the game
   */
  public GameBoard(int nbPlayers, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    if (Constants.isInvalidSquareNbPlayers(nbPlayers, version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_SQUARE_NUMBER_OF_PLAYERS);
    }
    this.bag = (version == Constants.VERSION_HEXAGONAL) ? new BagHexagonal(nbPlayers) : new BagSquare(nbPlayers);
    this.deck = new Deck(version);
    for (int i = 0; i < Constants.TILES_ON_BOARD; ++i) {
      GameBoard.tiles.add(bag.getRandomTile());
      GameBoard.tokens.add(deck.getRandomToken());
    }
  }


  /**
   * This method is used to determine if all tokens are the same.
   */
  public final boolean tokensNeedUpdate() {
    if (this.tokensAreUpdated) { return false; }   /* already updated */
    return GameBoard.tokens.stream().distinct().count() == 1;
  }

  /**
   * This method is used to determine which token needs to be updated.
   * if there's a token with 3 or more occurrences, we return it.
   */
  private WildlifeToken getTokenToUpdate() {
    map.clear();
    return GameBoard.tokens.stream()
                           .filter(token -> map.merge(token, 1, Integer::sum) >= 3)
                           .findFirst()
                           .orElse(null);
  }


  /**
   * This method is used to determine if tokens can be updated.
   * @return true if tokens can be updated, false otherwise
   */
  public final boolean tokensCanBeUpdated() {
    if (this.tokensAreUpdated) { return false; }   /* already updated */
    return getTokenToUpdate() != null;
  }


  /* to add later: using nature tokens for every player */
  
  public final boolean areTokensUpdated() {
    return this.tokensAreUpdated;
  }


  /**
   * Turn Manager switch 
   * */
  public final void setDefaultTokensAreUpdated() {
    this.tokensAreUpdated = false;
  }
  

  public final void setTrueTokensAreUpdated() {
    this.tokensAreUpdated = true;
  }


  private WildlifeToken updateToken(WildlifeToken token) {
    Objects.requireNonNull(token);
    return deck.updateToken(token);
  }



  public final void updateTokens(){
    if (this.tokensAreUpdated) {
      System.err.println("Tokens are already updated, wait next turn please");
      return;
    }
    /* there will be always only one token with 3 or more occurences */
    WildlifeToken tokenToChange = getTokenToUpdate();

    /* if there's no token with 3 or more occurrences, no update is needed  */
    if (tokenToChange == null) { return; }

    IntStream.range(0, tokens.size())
             .filter(i -> GameBoard.tokens.get(i).equals(tokenToChange))
             .forEach(i -> GameBoard.tokens.set(i, updateToken(GameBoard.tokens.get(i))));
             //.forEach(i -> GameBoard.tokens[i] = updateToken(GameBoard.tokens[i]));
    this.tokensAreUpdated = true;
  }


  public final List<Tile> getCopyOfTiles() {
    return List.copyOf(GameBoard.tiles);  // we sent a copy
  }

  public final List<WildlifeToken> getCopyOfTokens() {
    return List.copyOf(GameBoard.tokens);  // we sent a copy
  }
  

  public Tile getTile(int index){
    if (index < 0 || index >= Constants.TILES_ON_BOARD) {
      throw new IllegalArgumentException("Index of tile out of bounds");
    }
    var tile = GameBoard.tiles.get(index);
    GameBoard.tiles.set(index, bag.getRandomTile());  /* replace the tile */
    return tile;
  }


  public WildlifeToken getToken(int index){
    if (index < 0 || index >= Constants.TOKENS_ON_BOARD) {
      throw new IllegalArgumentException("Index of wildlife token out of bounds");
    }
    var token = GameBoard.tokens.get(index);
    GameBoard.tokens.set(index, deck.getRandomToken());  /* replace the token */
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


