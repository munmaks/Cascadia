package fr.uge.core;

import fr.uge.environment.Environment;
import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;
import fr.uge.scoring.BearScoringCard;
import fr.uge.scoring.ElkScoringCard;
import fr.uge.scoring.FamilyAndIntermediateScoringCards;
import fr.uge.scoring.FoxScoringCard;
import fr.uge.scoring.HawkScoringCard;
import fr.uge.scoring.SalmonScoringCard;
import fr.uge.util.Constants;
import java.util.Objects;

public final class Player {
  private final String name;    // just for counting later
  private final Environment environment;
  private int natureTokens = 0;


  /**
   * To think later how we get here all wildlife scoring card?
   * List, all in parameters or ...
   * */
  public Player(String name, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.name = Objects.requireNonNull(name, "Player name cannot be null");
    this.environment = new Environment(version);
  }


  public final Environment environment() {
    return this.environment;
  }

  public final int natureTokens() {
    return this.natureTokens;
  }

  public final String name() {
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


  /** calculates the player's based on his environment and current wildlife cards */
  public final int calculateScore() {
    int score = 0;
    
    return score;
  }
  
  
  
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
  
  
  
  public final int calculateFamilyAbdIntermediateScore(FamilyAndIntermediateScoringCards card) {
    int score = 0;
    var allTokens = new WildlifeToken[Constants.NB_TOKENS];
    for (var i = 0; i < Constants.NB_TOKENS; ++i) {
      allTokens[i] = new WildlifeToken(WildlifeType.values()[i]);
    }
    for (var token : allTokens) {
      var map = card.returnWildlifeTokenMap(token);
      score += map.getOrDefault(token, 0);
    }
    return score;
  }
  
  
  @Override
  public String toString() {
      return "Player, name: [ " + name + " ]\n" +
              "environment= " + environment.toString() +
              "\nnatureTokens=" + natureTokens + '\n';
  }


}
