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


import fr.uge.core.Player;
import fr.uge.environment.Cell;
import fr.uge.environment.WildlifeType;
import java.util.*;
import static fr.uge.environment.WildlifeType.FOX;

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


public record FoxScoringCard(HawkScoringType version, Player player) implements WildlifeScoringCard {

  /**
   * Take the value of each and depend on his value and the boolean parameters return his point's conversion
   *
   * @param each the value that we want to convert
   * @param AloneAndMorePresentAnimal a boolean who permits to convert each in a different way
   * @param AloneWithoutFox a boolean who permits to convert each in a different way
   * @return the conversion in point depend on the value of each and the boolean parameters
   */
  static int pointConversionFox(int each, boolean AloneAndMorePresentAnimal, boolean AloneWithoutFox) {
    if (AloneAndMorePresentAnimal) {
      return convertForAloneAndMorePresentAnimal(each);
    }
    if (AloneWithoutFox) {
      return convertForAloneWithoutFox(each);
    }
    return 0;
  }

  /**
   * Take the value of each and depend on his value, return his point's conversion
   *
   * @param each the value that we want to convert
   * @return the conversion in point depend on the value of each
   */
  static private int convertForAloneAndMorePresentAnimal(int each) {
    return switch (each){
      case 1 -> 1;
      case 2 -> 2;
      case 3 -> 3;
      case 4 -> 4;
      case 5 -> 5;
      default -> 6;
    };
  }

  /**
   * Take the value of each and depend on his value, return his point's conversion
   *
   * @param each the value that we want to convert
   * @return the conversion in point depend on the value of each
   */
  static private int convertForAloneWithoutFox(int each) {
    return switch (each){
      case 1 -> 3;
      case 2 -> 5;
      default -> 7;
    };
  }

  /**
   * Search in the map whose Wildlife type is the most present and return it
   *
   * @param speciesCount a Map whose list the Wildlife type and the number of it
   * @return the most present Wildlife type in speciesCount
   */
  public WildlifeType getMostCommonWildlifeType(Map<WildlifeType, Integer> speciesCount) {
    WildlifeType result = null;
    int max=0;
    for(var each : speciesCount.entrySet()) {
      if(each.getValue() > max) {
        result = each.getKey();
      }
    }
    return result;
  }

  /*
  public Map<Cell, Cell> findFoxPairs(List<Cell> foxCells) {
    Map<Cell, Cell> foxPairs = new HashMap<>();
    Set<Cell> visited = new HashSet<>();
    for (Cell fox1 : foxCells) {
      if (visited.contains(fox1)) {
        continue;
      }
      List<Cell> neighbors = player.getEnvironment().getNeighbors(fox1);
      for (Cell neighbor : neighbors) {
        if (foxCells.contains(neighbor) && !visited.contains(neighbor)) {
          foxPairs.put(fox1, neighbor);
          visited.add(fox1);
          visited.add(neighbor);
          break;
        }
      }
    }
    return foxPairs;
  }
   */

  // juste regarder autour et compter
  // attention pour onlyMorePresentAnimal, il faudra dabord trouver l'animal le plus présent puis le compter uniquement lui
  // pour pairAloneWithoutFox, à voir si on peut compter plusieurs fois les meme animaux ou non

  /**
   * Check all the cells in player's environment and count the number of Fox fill the card's conditions.
   * Convert directly the result in points and stock them in a List
   *
   * @param player player which we want to calculate his score
   * @return a list of integer who list the points earned for each formation of fox
   */
  public ArrayList<Integer> numberFox(Player player){
    List<Cell> cells = player.getEnvironment().getCells();
    ArrayList<Integer> numbers = new ArrayList<>();
    int aloneWithFox= 0, aloneWithoutFox = 0, onlyMorePresentAnimal =0, beforePointConversion;
    var onlyFox = cells.stream().filter(cell -> cell.getAnimal() == FOX) // récupère uniquement les cellules avec des renards
            .toList();
    Map<Cell, Map<WildlifeType, Integer>> neighborSpecies = WildlifeScoringCard.countNeighborsForAnimals(onlyFox, player);
    for(var each : neighborSpecies.values()) {
      beforePointConversion = WildlifeScoringCard.numberNeighborSpecies(each, true, FOX, false);
      aloneWithFox += pointConversionFox(beforePointConversion, true, false);
      beforePointConversion = (int)each.entrySet().stream().filter(entry -> entry.getKey() != FOX).filter(entry -> entry.getValue() == 2).count(); // vérifie pour chaque fox si ces voisins sont en pairs et si oui, les compte
      aloneWithoutFox += pointConversionFox(beforePointConversion, false, true); // convertis le compte d'animaux paire obtenus en points
      beforePointConversion = WildlifeScoringCard.numberNeighborSpecies(each, false, getMostCommonWildlifeType(each), true);
      onlyMorePresentAnimal += pointConversionFox(beforePointConversion, true, false);
    }
    numbers.add(aloneWithFox);
    numbers.add(aloneWithoutFox);
    numbers.add(onlyMorePresentAnimal);
    return numbers;
  }

  /**
   * Take the list of points for each formation and associate in function of the version chose
   *
   * @param version the version of card that the player choose
   * @param player the player that we want to actualise his score
   */
  public FoxScoringCard {
    // Ne pas oublier de compter correctement les points
    // si version 1, points gqgnés = aloneWithFox
    // si version 2, points gqgnés = aloneWithoutFox
    // si version 3, points gqgnés = onlyMorePresentAnimal
    // si version 4, points gqgnés = pairAloneWithoutFox
    ArrayList<Integer> numbers = numberFox(player);
    switch (version) {
      case FIRST: player.score += numbers.get(0);
      case SECOND: player.score += numbers.get(1);
      case THIRD: player.score += numbers.get(2);
    }
  }

}