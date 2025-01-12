package fr.uge.data.scoring;


import fr.uge.data.core.*;
import fr.uge.data.environment.*;

import java.util.*;

// Bears are scored for forming groups of varying sizes.
// These bear groups can take any shape or orientation, but they cannot be
// placed adjacent to each other.
// Each group must precisely match the number of bears indicated on the scoring
// card to earn points.
// There are card clarifications:
// - (A) Earn points based on the total number of pairs of bears.
// - (B) Score 10 points for each group of exactly three bears.
// - (C) Score for groups of bears ranging in size from 1 to 3,
// with a bonus of 3 points for having one of each of the three group sizes.
// - (D) Score for groups of bears with sizes ranging from 2 to 4.

enum BearScoringType {

  FIRST, /* Earn points based on the total number of pairs of bears. */

  SECOND, /* Score 10 points for each group of exactly three bears. */

  THIRD, /*
          * Score for groups of bears ranging in size from 1 to 3, with a bonus of 3
          * points for having one of each of the three group sizes.
          */

  FOURTH; /* Score for groups of bears with sizes ranging from 2 to 4. */
}


public final class BearScoringCard implements WildlifeScoringCard {

  /**
   * Take the list of group of bear, converts them into points and associates them
   * with the player's score
   *
   * @param animalVersion Which Card the player choose
   */
  public BearScoringCard(AnimalScoringType animalVersion) {
    // si version 1, points gqgnés = twoBear
    // si version 2, points gqgnés = threeBear
    // si version 3, points gqgnés = oneBear + twoBear + threeBear + 3 pts bonus si
    // trois groupes non nulles
    // si version 4, points gqgnés = twoBear + threeBear + fourBear
    // ArrayList<Integer> numbers = numberBear(player);
    // switch (version) {
    // case FIRST:
    // player.score += WildlifeScoringCard.pointConversionBear(numbers, true, false,
    // false);
    // case SECOND:
    // player.score += numbers.get(1) * 10;
    // case THIRD:
    // player.score += WildlifeScoringCard.pointConversionBear(numbers, false, true,
    // false);
    // case FOURTH:
    // player.score += WildlifeScoringCard.pointConversionBear(numbers, false,
    // false, true);
    // }
  }

  /**
   * This method take all the tiles in the player's environment and stock the
   * number of group of Bear in a List
   *
   * @param player The player whose score is calculated
   * @return list of number of 1, 2, 3 and 4 groups of bear
   */
  public List<Integer> numberBear(Player player) {
    // List<Cell> cells = player.getEnvironment().getCells();
    // ArrayList<Integer> numbers = new ArrayList<>();
    // numbers.add(0); // solo
    // numbers.add(0); // pair
    // numbers.add(0); // trio
    // numbers.add(0); // quatuor
    // // parcourt la grille et récupère dans des variables le nombre de groupe de
    // // différentes tailles
    // var onlyBear = cells.stream().filter(cell -> cell.getAnimal() ==
    // WildlifeType.BEAR).toList();
    // var res = WildlifeScoringCard.calculateGroupScore(onlyBear, player,
    // WildlifeType.BEAR);
    // for (var each : res) {
    // if (each > 0 && each < 5)
    // numbers.set(each, numbers.get(each) + 1); // vérifie si each est entre 1 et 4
    // inclus (on
    // // compte pas des groupes de 5 ou plus)
    // }
    // return List.copyOf(numbers);
    return null;
  }

}
