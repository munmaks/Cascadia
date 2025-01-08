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
  public ArrayList<Integer> numberFox(Player player){
    List<Cell> cells = player.getEnvironment().getCells();
    ArrayList<Integer> numbers = new ArrayList<>();
    int aloneWithFox= 0, aloneWithoutFox = 0, onlyMorePresentAnimal =0, beforePointConversion;
    var onlyFox = cells.stream().filter(cell -> cell.getAnimal() == FOX) // récupère uniquement les cellules avec des renards
            .toList();
    Map<Cell, Map<WildlifeType, Integer>> neighborSpecies = WildlifeScoringCard.countNeighborsForAnimals(onlyFox, player);
    for(var each : neighborSpecies.values()) {
      beforePointConversion = WildlifeScoringCard.numberNeighborSpecies(each, true, FOX, false);
      aloneWithFox += WildlifeScoringCard.pointConversionFox(beforePointConversion, true, false);
      beforePointConversion = (int)each.entrySet().stream().filter(entry -> entry.getKey() != FOX).filter(entry -> entry.getValue() == 2).count(); // vérifie pour chaque fox si ces voisins sont en pairs et si oui, les compte
      aloneWithoutFox += WildlifeScoringCard.pointConversionFox(beforePointConversion, false, true); // convertis le compte d'animaux paire obtenus en points
      beforePointConversion = WildlifeScoringCard.numberNeighborSpecies(each, false, getMostCommonWildlifeType(each), true);
      onlyMorePresentAnimal += WildlifeScoringCard.pointConversionFox(beforePointConversion, true, false);
    }
    numbers.add(aloneWithFox);
    numbers.add(aloneWithoutFox);
    numbers.add(onlyMorePresentAnimal);
    return numbers;
  }

  public FoxScoringCard {
    // Ne pas oublier de compter correctement les points
    // si version 1, points gqgnés = aloneWithFox
    // si version 2, points gqgnés = aloneWithoutFox
    // si version 3, points gqgnés = onlyMorePresentAnimal
    // si version 4, points gqgnés = pairAloneWithoutFox
    ArrayList<Integer> numbers = numberFox(player);
    switch (version) {
      case FIRST:
      case SECOND:
      case THIRD:
    }
  }

}