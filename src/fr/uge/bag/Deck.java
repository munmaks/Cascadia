package fr.uge.bag;

import java.util.Objects;
import java.util.Random;
//import java.util.List;
//import java.util.ArrayList;

import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;


import fr.uge.util.Constants;

public record Deck(int version) {

  /**
   * Indexes from WildlifeType `enum`:
   * BEAR   : 0
   * ELK    : 1
   * HAWK   : 2
   * FOX    : 3
   * SALMON : 4
   */
  private static int[] animals =  {
        Constants.ANIMAL_TOKENS,  /* BEAR   */
        Constants.ANIMAL_TOKENS,  /* ELK    */
        Constants.ANIMAL_TOKENS,  /* HAWK   */
        Constants.ANIMAL_TOKENS,  /* FOX    */
        Constants.ANIMAL_TOKENS   /* SALMON */
      };
  private static final Random random = new Random();

  /* big number to prevent infinity loop in method drawToken() */
  private static final int MAX_ITERATION = 1 << 15;   /* big number to prevent infinity loop */


  /* compact constructor */
  public Deck {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    if (version != Constants.VERSION_HEXAGONAL) {
      decreaseAnimals();
    }
  }

  /**
   * Draws a random WildlifeToken from the deck.
   * If the selected type has no tokens left,
   * it retries until a type with available tokens is found.
   *
   * @param void
   * @return WildlifeToken - the randomly selected token.
   */
  public final WildlifeToken getRandomToken(){
    var index = 0;
    var iteration = 0;

    /* we have max iteration, to prevent infinity loop */
    while (iteration <= MAX_ITERATION) {

        index = random.nextInt(animals.length);   /* random integer in range [0, 5[ */
        ++iteration;

        if (animals[index] > 0) {   /* if tokens of this animals are still available */
            animals[index]--;
            return new WildlifeToken(WildlifeType.values()[index]);
        }
    }
    /* normally it shouldn't happen */
    System.err.println("Maximum number of iterations exceeded in drawToken()");
    return null;
  }

  
  /**
   * This method is called when we need to replace a token on the game board with a new one.
   * @param token token to change
   * @return new token.
   */
  public final WildlifeToken updateToken(WildlifeToken token) {
    Objects.requireNonNull(token, "Token must not be null!");

    /* return into deck current token */
    var index = token.animal().ordinal();
    animals[index]++;

    return getRandomToken();
  }
  
  
  private static void decreaseAnimals() {
    for (var i = 0; i < animals.length; ++i) {
      /* if we play square version, so we need only a half of hexagonal version */
      animals[i] -= Constants.ANIMALS_SQUARE;
    }
  }
}
