package fr.uge.core;

import fr.uge.environment.Environment;
import fr.uge.environment.HexagonalEnvironment;
import fr.uge.environment.SquareEnvironment;
import fr.uge.scoring.BearScoringCard;
import fr.uge.scoring.ElkScoringCard;
import fr.uge.scoring.FoxScoringCard;
import fr.uge.scoring.HawkScoringCard;
import fr.uge.scoring.SalmonScoringCard;
import fr.uge.util.Constants;
import java.util.Objects;


public final class Player {
  private final String name;    /* just for counting later */
  private final Environment environment;
  private int natureTokens = 0;
  public int score = 0;

  /**
   * To think later how we get here all wildlife scoring card?
   * List, all in parameters or ...
   * */
  public Player(String name, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    this.name = Objects.requireNonNull(name, "Player name cannot be null");

    this.environment = (version == Constants.VERSION_HEXAGONAL) ?
                        new HexagonalEnvironment() :
                        new SquareEnvironment();
  }


  public final Environment getEnvironment() {
    return this.environment;
  }

  public final int getNatureTokens() {
    return this.natureTokens;
  }

  public final String getName() {
    return this.name;
  }


  public boolean canUseNatureTokens(){
    return this.natureTokens > 0;
  }


  /**
   * for later usage, 
   * decreases the number of nature tokens
   */
  public final boolean useNatureTokens(){
    if (!canUseNatureTokens()) {
      return false; /* do nothing */
    }
    this.natureTokens--;
    return true;
  }


  /** calculates the player's score based on his environment and current wildlife cards */
  public final int calculateScore() {
    score += environment.calculateTileScore()
                        .values()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .sum();
    return score;
  }



  // public final Map<TileType, Integer> calculateTileScore() {
  //   var tileTypes = TileType.values();
  //   var score = 0;
  //   var allTiles = environment.getCells();

  //   return score;
  // }
  



  
  public final int calculateBearScore(BearScoringCard card) {
    
    return 0;
  }
  
  
  
  public final int calculateElkScore(ElkScoringCard card) {
    
    return 0;
  }
  
  
  
  public final int calculateHawkScore(HawkScoringCard card) {
    
    return 0;
  }
  
  
  
  public final int calculateFoxScore(FoxScoringCard card) {
    
    return 0;
  }
  
  
  
  public final int calculateSalmonScore(SalmonScoringCard card) {
    
    return 0;
  }
  
  
  
  @Override
  public String toString() {
      return "Player, name: [ " + name + " ]\n" +
              "environment= " + environment.toString() +
              "\nnatureTokens=" + natureTokens + '\n';
  }


}
