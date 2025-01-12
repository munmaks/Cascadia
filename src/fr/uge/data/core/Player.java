package fr.uge.data.core;


import fr.uge.data.environment.Environment;
import fr.uge.data.environment.HexagonalEnvironment;
import fr.uge.data.environment.SquareEnvironment;
import fr.uge.data.util.Constants;

import java.util.Objects;

/**
 * Represents a player in the game. A player has a name, an environment, a
 * number of nature tokens and a score.
 */
public final class Player {
  private final String name; /* just for counting later */
  private final Environment environment;
  private int natureTokens = 0;
  private int score = 0;

  public Player(String name, int version) {
    this.name = Objects.requireNonNull(name, "Player name cannot be null");

    this.environment = (version == Constants.VERSION_HEXAGONAL) ? new HexagonalEnvironment()
        : new SquareEnvironment();
  }

  /**
   * @return the player's environment
   */
  public final Environment getEnvironment() { return this.environment; }

  /**
   * @return the player's score
   */
  public final int getNatureTokens() { return this.natureTokens; }

  /**
   * @return the player's score
   */
  public final String getName() { return this.name; }

  /**
   * @return the player's score
   */
  public boolean canUseNatureTokens() { return this.natureTokens > 0; }

  /**
   * Uses a nature token to choose a tile and token
   * 
   * @return
   */
  public final boolean useNatureTokens() {
    if (!canUseNatureTokens()) {
      return false; /* do nothing */
    }
    this.natureTokens--;
    return true;
  }

  /**
   * Adds a nature token to the player's inventory
   * 
   * @return the player's final score based on the environment
   */
  public final int calculateScore() {
    score += environment.calculateTileScore().values().stream().mapToInt(Integer::intValue).sum();
    return score;
  }

  /**
   * toString method for the player
   */
  @Override
  public String toString() {
    return "Player, name: [ " + name + " ]\n" + "environment= " + environment.toString()
        + "\nnatureTokens=" + natureTokens + '\n';
  }

}
