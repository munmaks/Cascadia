package fr.uge.bag;

import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;
import java.util.Objects;
import java.util.Random;

/**
 * The `Deck` class represents the deck of WildlifeTokens in the game.
 * It is responsible for drawing random tokens from the deck and updating the deck
 * when a token is replaced on the game board.
 */
public final class Deck {

  /**
   * Indexes from WildlifeType `enum`:
   * BEAR   : 0
   * ELK    : 1
   * HAWK   : 2
   * FOX    : 3
   * SALMON : 4
   */
  private static final int[] animals =  {
        Constants.ANIMAL_TOKENS,  /* BEAR   */
        Constants.ANIMAL_TOKENS,  /* ELK    */
        Constants.ANIMAL_TOKENS,  /* HAWK   */
        Constants.ANIMAL_TOKENS,  /* FOX    */
        Constants.ANIMAL_TOKENS   /* SALMON */
      };

  /* big number to prevent infinity loop in method drawToken() */
  private static final int MAX_ITERATION = 1 << 16;   /* big number to prevent infinity loop */


  /* compact constructor */
  public Deck(int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    if (version != Constants.VERSION_HEXAGONAL) {
      decreaseAnimals();
    }
  }

  /**
   * Draws a random WildlifeToken from the deck.<br>
   * If the selected type has no tokens left,<br>
   * it retries until a type with available tokens is found. <br>
   *
   * @return WildlifeToken - the randomly selected token.
   */
  public final WildlifeToken getRandomToken(){
    int index;
    int iteration = 0;
    var random = new Random();

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
    throw new IllegalArgumentException("Maximum number of iterations exceeded in drawToken()");
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

  /**
   * Decrease the number of animals in the deck by half.
   * This method is called when the game is played in the square version.
   */
  private static void decreaseAnimals() {
    for (var i = 0; i < animals.length; ++i) {
      /* if we play square version, so we need only a half of hexagonal version */
      animals[i] -= Constants.ANIMALS_SQUARE;
    }
  }
  
}
