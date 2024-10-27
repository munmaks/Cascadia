package fr.uge.scoring;

import java.util.List;


//public record ScoreCalculator(Environment env, List<WildlifeScoringCard> scoringCards) {
public record ScoreCalculator(List<WildlifeScoringCard> scoringCards) {
  
  /* compact */
  public ScoreCalculator {
    
  }

  public int calculateScore() {
    // TO DO
    return 0;
  }
}
