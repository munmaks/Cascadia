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

import fr.uge.core.*;
import fr.uge.environment.*;
import java.util.*;
import static fr.uge.environment.WildlifeType.BEAR;


enum BearScoringType {

  FIRST,    /* Earn points based on the total number of pairs of bears. */

  SECOND,   /* Score 10 points for each group of exactly three bears. */

  THIRD,    /* Score for groups of bears ranging in size from 1 to 3,
               with a bonus of 3 points for having one of each of the three group sizes. */

  FOURTH;   /* Score for groups of bears with sizes ranging from 2 to 4. */
}


public record BearScoringCard(BearScoringType version, Player player) implements WildlifeScoringCard {

  public ArrayList<Integer> numberBear(Player player){
    List<Cell> cells = player.getEnvironment().getCells();
    ArrayList<Integer> numbers = new ArrayList<>();
    numbers.add(0); // solo
    numbers.add(0); // pair
    numbers.add(0); // trio
    numbers.add(0); // quatuor
    // parcourt la grille et récupère dans des variables le nombre de groupe de différentes tailles
    var onlyBear = cells.stream().filter(cell -> cell.getAnimal() == BEAR) // récupère uniquement les cellules avec des ours
                    .toList();
    List<Integer> res = WildlifeScoringCard.calculateGroupScore(onlyBear, player); // renvoie une liste avec la taille des groupes
    for(var each : res){
      if(each>0 && each < 5) numbers.set(each, numbers.get(each) + 1); // vérifie si each est entre 1 et 4 inclus (on compte pas des groupes de 5 ou plus)
    }
    return numbers;
  }

  public BearScoringCard {
    // si version 1, points gqgnés = twoBear
    // si version 2, points gqgnés = threeBear
    // si version 3, points gqgnés = oneBear + twoBear + threeBear + 3 pts bonus si trois groupes non nulles
    // si version 4, points gqgnés = twoBear + threeBear + fourBear
    ArrayList<Integer> numbers = numberBear(player);
    switch(version){
      case FIRST: player.score += WildlifeScoringCard.pointConversionBear(numbers, true, false, false);
      case SECOND: player.score += numbers.get(1) * 10;
      case THIRD: player.score += WildlifeScoringCard.pointConversionBear(numbers, false, true, false);
      case FOURTH: player.score += WildlifeScoringCard.pointConversionBear(numbers, false, false, true);
    }
  }
}
