package fr.uge.scoring;

/*
Salmon are scored for creating runs, defined as groups of adjacent salmon where each salmon is connected to at most two other salmon.
Runs may not have any other salmon adjacent to them. There are card clarifications:
  - (A) Score for each run based on size, up to a maximum size of 7.
  - (B) Score for each run based on size, up to a maximum size of 5.
  - (C) Score for each run based on size, with sizes between 3 and 5.
  - (D) Score for each run of salmon, earning one point for each salmon in the run,
        plus one point for each adjacent animal token, regardless of the type of animal.
*/

enum SalmonScoringType {

  FIRST,    /* Score for each run based on size, up to a maximum size of 7. */

  SECOND,   /* Score for each run based on size, up to a maximum size of 5. */

  THIRD,    /* Score for each run based on size, with sizes between 3 and 5. */

  FOURTH;   /* Score for each run of salmon, earning one point for each salmon in the run,
               plus one point for each adjacent animal token, regardless of the type of animal. */

}

public record SalmonScoringCard(SalmonScoringType version) implements WildlifeScoringCard {

  public SalmonScoringCard {
    
  }

}
