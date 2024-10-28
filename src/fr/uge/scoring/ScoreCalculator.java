package fr.uge.scoring;

import java.util.List;

// It accepts `env` because for every environment we should calculate number of 
// public record ScoreCalculator(Environment env, List<WildlifeScoringCard> scoringCards) {

public record ScoreCalculator(List<WildlifeScoringCard> scoringCards) {
  
  /* compact */
  public ScoreCalculator {
    
  }

  
  public int calculateScore() {
    // TO DO
    return 0;
  }
}
