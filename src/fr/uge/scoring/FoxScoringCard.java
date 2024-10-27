package fr.uge.scoring;

/*
Foxes score based on their adjacency to other animals, either individually or in pairs.
Each fox or fox pair is scored independently, with points increasing depending on certain conditions in adjacent habitat spaces.
There are card clarifications:
- (A) Score for each fox, with points increasing based on the number of unique animal types,
      including other foxes, directly adjacent to it.

- (B) Score for each fox, with points increasing based on the number of unique animal pairs,
      not including other fox pairs, directly adjacent to it.
      Pairs of other animals do not need to be adjacent to each other.

- (C) Score for each fox, with points increasing based on the number of similar animals,
      excluding other foxes, directly adjacent to it.
      Only the most abundant adjacent animal type is scored.

- (D) Score for each fox pair, with points increasing based on the number of unique animal pairs,
      not including other fox pairs, directly adjacent to it.
      Pairs of other animals do not need to be adjacent to each other. */


enum FoxScoringType {

  FIRST,    /* Score for each fox, with points increasing based on the number of unique animal types,
               including other foxes, directly adjacent to it. */

  SECOND,   /* Score for each fox, with points increasing based on the number of unique animal pairs,
               not including other fox pairs, directly adjacent to it.
               Pairs of other animals do not need to be adjacent to each other. */

  THIRD,    /* Score for each fox, with points increasing based on the number of similar animals,
               excluding other foxes, directly adjacent to it.
               Only the most abundant adjacent animal type is scored. */

  FOURTH;   /* Score for each fox pair, with points increasing based on the number of unique animal pairs,
               not including other fox pairs, directly adjacent to it.
               Pairs of other animals do not need to be adjacent to each other. */
}


public record FoxScoringCard() implements WildlifeScoringCard {

  public FoxScoringCard {
    
  }

}











