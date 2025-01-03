package fr.uge.scoring;

/*
Elk are scored for forming groups that match the specific shape or formation depicted on the card.
Elk groups can be placed adjacent to each other, but each elk may only score once for a single group or formation.
When scoring connected elk groups, always choose the interpretation that yields the highest point total.
There are card clarifications:
- (A) Score for groups arranged in straight lines,
      connecting flat sides of hexagons in any orientation.
- (B) Score for groups matching the exact shapes shown, in any orientation.
- (C) Score for each contiguous group of elk, with points increasing based on size and shape,
      which can be of any configuration.
- (D) Elk groups must be arranged in a circular formation as illustrated.
*/


enum ElkScoringType {

  FIRST,    /* Score for groups arranged in straight lines,
               connecting flat sides of hexagons in any orientation. */

  SECOND,   /* Score for groups matching the exact shapes shown, in any orientation. */

  THIRD,    /* Score for each contiguous group of elk, with points increasing based on size and shape,
               which can be of any configuration. */

  FOURTH;   /* Elk groups must be arranged in a circular formation as illustrated. */
}



public final class ElkScoringCard implements WildlifeScoringCard {

    public ElkScoringCard(ElkScoringType version) {
    
  }
}
