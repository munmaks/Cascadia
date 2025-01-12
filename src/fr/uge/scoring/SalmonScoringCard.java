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

import fr.uge.core.Player;
import fr.uge.environment.Cell;
import java.util.ArrayList;
import java.util.List;
import static fr.uge.environment.WildlifeType.SALMON;

enum SalmonScoringType {

  FIRST,    /* Score for each run based on size, up to a maximum size of 7. */

  SECOND,   /* Score for each run based on size, up to a maximum size of 5. */

  THIRD,    /* Score for each run based on size, with sizes between 3 and 5. */

  FOURTH;   /* Score for each run of salmon, earning one point for each salmon in the run,
               plus one point for each adjacent animal token, regardless of the type of animal. */

}

public record SalmonScoringCard(SalmonScoringType version, Player player) implements WildlifeScoringCard {

  /**
   * Take the value of each and depend on his value and the boolean parameters return his point's conversion
   *
   * @param each the value that we want to convert
   * @param maxSeven a boolean who permits to convert each in a different way
   * @param maxFive a boolean who permits to convert each in a different way
   * @param threeToFive a boolean who permits to convert each in a different way
   * @return the conversion in point depend on the value of each and the boolean parameters
   */
  static int pointConversionSalmon(int each, boolean maxSeven, boolean maxFive, boolean threeToFive) {
    if (maxSeven) {
      return convertForMaxSeven(each);
    } else if (maxFive) {
      return convertForMaxFive(each);
    } else if (threeToFive) {
      return convertForThreeToFive(each);
    }
    return 0;
  }

  /**
   * Take the value of each and depend on his value, return his point's conversion
   *
   * @param each the value that we want to convert
   * @return the conversion in point depend on the value of each
   */
  static private int convertForMaxSeven(int each) {
    return switch (each) {
      case 1 -> 2;
      case 2 -> 5;
      case 3 -> 8;
      case 4 -> 12;
      case 5 -> 16;
      case 6 -> 20;
      default -> 25;
    };
  }

  /**
   * Take the value of each and depend on his value, return his point's conversion
   *
   * @param each the value that we want to convert
   * @return the conversion in point depend on the value of each
   */
  static private int convertForMaxFive(int each) {
    return switch (each) {
      case 1 -> 2;
      case 2 -> 4;
      case 3 -> 9;
      case 4 -> 11;
      default -> 17;
    };
  }

  /**
   * Take the value of each and depend on his value, return his point's conversion
   *
   * @param each the value that we want to convert
   * @return the conversion in point depend on the value of each
   */
  static private int convertForThreeToFive(int each) {
    return switch (each) {
      case 3 -> 10;
      case 4 -> 12;
      default -> 15;
    };
  }

  /**
   * Count the salmon in group in the environment of player and convert this value in point and return the list of it
   *
   * @param player current player which we want to calculate his score
   * @return a list of integer who stock the points earned by the player for each group of salmon
   */
  public ArrayList<Integer> numberSalmon(Player player){
    ArrayList<Integer> numbers = new ArrayList<>();
    int maxSeven= 0, maxFive = 0, threeToFive =0;
    List<Cell> cells = player.getEnvironment().getCells();
    var onlySalmon = cells.stream().filter(cell -> cell.getAnimal() == SALMON) // récupère uniquement les cellules avec des aigles
            .toList();
    List<Integer> numberSalmon = WildlifeScoringCard.calculateGroupScore(onlySalmon, player, SALMON);
    for(var each : numberSalmon) { // convertis pour chaque groupe son equivalent en point
      maxSeven += pointConversionSalmon(each, true, false, false);
      maxFive += pointConversionSalmon(each, false, true, false);
      if(each>3) threeToFive += pointConversionSalmon(each, false, false, true);
    }
    numbers.add(maxSeven);
    numbers.add(maxFive);
    numbers.add(threeToFive);
    return numbers;
  }

  /**
   * actualise the score of the player with the value in the list return by numberSalmon
   * The value depend on the version who chose the player
   *
   * @param version the version of card that the player choose
   * @param player the player that we want to actualise his score
   */
  public SalmonScoringCard {
    // Ne pas oublier de compter correctement les points
    // si version 1, points gqgnés = maxSeven
    // si version 2, points gqgnés = maxFive
    // si version 3, points gqgnés = threeToFive
    // si version 4, points gqgnés = pairAloneWithoutFox
    ArrayList<Integer> numbers = numberSalmon(player);
    switch (version) {
      case FIRST: player.score +=numbers.getFirst();
      case SECOND: player.score +=numbers.get(1);
      case THIRD: player.score +=numbers.get(2);
      //case FOURTH:
    }
  }


}
