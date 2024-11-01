package fr.uge.deck;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;


public record Deck() {
  /**
   * Indexes from WildlifeType `enum`:
   * BEAR   : 0
   * ELK    : 1
   * HAWK   : 2
   * FOX    : 3
   * SALMON : 4
   */

                               /* BEAR   ELK  HAWK   FOX  SALMON; */
  private static int[] animals =  {20,   20,   20,   20,   20};
  private static final Random random = new Random();

  /* big number to prevent infinty loop in method drawToken() */
  private static final int MAX_ITERATION = 1 << 15;

  /* compact constructor */
  public Deck {
    
  }

  /**
   * Draws a random WildlifeToken from the deck.
   * If the selected type has no tokens left,
   * it retries until a type with available tokens is found.
   *
   * @param void
   * @return WildlifeToken - the randomly selected token.
   */
  public WildlifeToken drawToken(){
    var index = 0;
    var iteration = 0;

    /* we have max iteration, to prevent infinity loop */
    while (true && iteration <= MAX_ITERATION) {
        
        /* random int in range (from 0 to animals.length - 1) */
        index = random.nextInt(animals.length);
        ++iteration;

        /* if tokens of this animals are still available */
        if (animals[index] > 0) {
            animals[index]--;
            return new WildlifeToken(WildlifeType.values()[index]);
        }
    }
    System.err.println("Max Interation in drawToken()");  /* normally it shouldn't happen */
    return null;
  }

  
  /**
   * This method is called when we need to replace a token on the gameboard with a new one.
   * @param token token to change
   * @return new token.
   */
  public WildlifeToken updateToken(WildlifeToken token) {
    Objects.requireNonNull(token, "Token must not be null!");

    /* return into deck current token */
    var index = token.animal().ordinal();
    animals[index]++;

    return drawToken();
  }

}
