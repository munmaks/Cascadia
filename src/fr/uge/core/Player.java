package fr.uge.core;

import fr.uge.environment.Environment;
import fr.uge.scoring.BearScoringCard;
import fr.uge.scoring.ElkScoringCard;
import fr.uge.scoring.FamilyScoringCard;
import fr.uge.scoring.FoxScoringCard;
import fr.uge.scoring.HawkScoringCard;
import fr.uge.scoring.IntermediateScoringCard;
import fr.uge.scoring.SalmonScoringCard;
import fr.uge.util.Constants;
import java.util.Objects;

public final class Player {
  private static Environment environment;
  private static int natureTokens = 0;
  private static String name = null;

  /**
   * To think later how we get here all wildlife scoring card?
   * List, all in parameters or ...
   * */
  public Player(String playerName, int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    Objects.requireNonNull(playerName, "Player name cannot be null");
    this.environment = new Environment(version);
    this.name = playerName;
  }


  /** calculates the player's based on his environment and current wildlife cards */
  public int calculateScore() {
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
  
  
  
  public final int calculateFamilyScore(FamilyScoringCard card) {
    
    return 0;
  }
  
  
  
  public final int calculateIntermediateScore(IntermediateScoringCard card) {
    
    return 0;
  }

    @Override
    public String toString() {

        return "Player{" +
                "environment=" + environment +
                ", natureTokens=" + natureTokens +
                ", name='" + name + '\'' +
                '}';
    }

}
