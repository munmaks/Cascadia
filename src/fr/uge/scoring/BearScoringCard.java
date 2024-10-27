package fr.uge.scoring;

/*
Bears are scored for forming groups of varying sizes.
These bear groups can take any shape or orientation, but they cannot be placed adjacent to each other.
Each group must precisely match the number of bears indicated on the scoring card to earn points.
There are card clarifications:
- (A) Earn points based on the total number of pairs of bears.
- (B) Score 10 points for each group of exactly three bears.
- (C) Score for groups of bears ranging in size from 1 to 3,
      with a bonus of 3 points for having one of each of the three group sizes.
- (D) Score for groups of bears with sizes ranging from 2 to 4.
*/


enum BearScoringType {

  FIRST,    /* Earn points based on the total number of pairs of bears. */

  SECOND,   /* Score 10 points for each group of exactly three bears. */

  THIRD,    /* Score for groups of bears ranging in size from 1 to 3,
               with a bonus of 3 points for having one of each of the three group sizes. */

  FOURTH;   /* Score for groups of bears with sizes ranging from 2 to 4. */
}


public record BearScoringCard(BearScoringType version) implements WildlifeScoringCard {

  public BearScoringCard {
    
  }

}
