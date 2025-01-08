package fr.uge.scoring;

/*
Hawks score by spreading out across the landscape and can earn points for each hawk, pairs of hawks, or lines of sight between hawks.
Lines of sight are straight lines connecting flat sides of hexagons, and they are interrupted only by the presence of another hawk.
There are card clarifications:
- (A) Score an increasing number of points for each hawk not adjacent to any other hawk.
- (B) Score an increasing number of points for each hawk not adjacent to any other hawk and having a direct line of sight to another hawk.
- (C) Score 3 points for each line of sight between two hawks, with the possibility of multiple lines involving the same hawk.
- (D) Score for each pair of hawks, with points increasing based on the number of unique animal types between them, 
      excluding other hawks.Each hawk can only be part of one pair.
*/


import fr.uge.core.Player;
import fr.uge.environment.Cell;
import fr.uge.environment.WildlifeType;

import java.util.*;

import static fr.uge.environment.WildlifeType.HAWK;

enum HawkScoringType {

  FIRST,    /* Score an increasing number of points for each hawk not adjacent to any other hawk. */

  SECOND,   /* Score an increasing number of points for each hawk not adjacent to any other hawk and having a direct line of sight to another hawk. */

  THIRD,    /* Score 3 points for each line of sight between two hawks, with the possibility of multiple lines involving the same hawk. */

  FOURTH;   /* Score for each pair of hawks, with points increasing based on the number of unique animal types between them, 
               excluding other hawks.Each hawk can only be part of one pair. */
}



public record HawkScoringCard(HawkScoringType version, Player player) implements WildlifeScoringCard {

  private boolean isAligned(Cell otherCell, int sameY, int sameDiagonalUp, int sameDiagonalDown) {
    return otherCell.getCoordinates().y() == sameY ||
            (otherCell.getCoordinates().x() - otherCell.getCoordinates().y()) == sameDiagonalUp ||
            (otherCell.getCoordinates().x() + otherCell.getCoordinates().y()) == sameDiagonalDown;
  }

  int hawkAligned(List<Cell> onlyHawk, Player player){ // rajoute un boolean hawk pour un cas spécifique
    Set<Cell> visited = new HashSet<>();
    Set<Cell> alignedCells = new HashSet<>();
    for (Cell currentCell : onlyHawk) {
      if (visited.contains(currentCell)) continue;
      int sameY = currentCell.getCoordinates().y();
      int sameDiagonalUp = currentCell.getCoordinates().x() - currentCell.getCoordinates().y();
      int sameDiagonalDown = currentCell.getCoordinates().x() + currentCell.getCoordinates().y();
      onlyHawk.stream()
              .filter(otherCell -> !visited.contains(otherCell) && !currentCell.equals(otherCell))
              .filter(otherCell -> isAligned(otherCell, sameY, sameDiagonalUp, sameDiagonalDown))
              .filter(otherCell -> !WildlifeScoringCard.sameWildlifeTypeInNeighborhood(otherCell, currentCell, player)) // permet d'ignorer les liens entre voisins proches mais reconnait les voisins éloignés
              .forEach(otherCell -> {
                alignedCells.add(otherCell);
                alignedCells.add(currentCell);
              });
      visited.add(currentCell);
    }
    return alignedCells.size();
  }

  // faire attention à pair to One qui accepte un autre aigle à proximité alors que alone non, possiblement faire une méthode à part pour pairToOne
  // pour alone, pair et multiplePair, on exclut automatiquement les aigles à proximité d'où les cas comme celui de la carte C (deux aigles à coté mais ignorer pour revenir dessus apres)
  public ArrayList<Integer> numberHawk(Player player){
    ArrayList<Integer> numbers = new ArrayList<>();
    List<Cell> cells = player.getEnvironment().getCells();
    int alone= 0, pair = 0, multiplePair =0, pairToOne =0;
    var onlyHawk = cells.stream().filter(cell -> cell.getAnimal() == HAWK) // récupère uniquement les cellules avec des aigles
            .toList();
    Map<Cell, Map<WildlifeType, Integer>> neighborSpecies = WildlifeScoringCard.countNeighborsForAnimals(onlyHawk, player);
    for(var each : neighborSpecies.values()) {
      if(WildlifeScoringCard.numberNeighborSpecies(each, true, HAWK, false) == WildlifeScoringCard.numberNeighborSpecies(each, false, HAWK, false)){
        alone+=WildlifeScoringCard.numberNeighborSpecies(each, true, HAWK, false); // si égal alors pas d'aigle en voisins
      }
    }
    pair = hawkAligned(onlyHawk, player);
    multiplePair = pair - 1; // le nombre d'aigle relié - 1 = nombre de ligne qui les relie
    numbers.add(alone);
    numbers.add(pair);
    numbers.add(multiplePair);
    return numbers;
  }

  public HawkScoringCard {
    // Ne pas oublier de compter correctement les points
    // si version 1, points gqgnés = alone
    // si version 2, points gqgnés = pair
    // si version 3, points gqgnés = multiplePair
    // si version 4, points gqgnés = pairAloneWithoutFox
    ArrayList<Integer> numbers = numberHawk(player);
    switch (version) {
      case FIRST: player.score += WildlifeScoringCard.pointConversionHawk(numbers.get(0), true, false, false);
      case SECOND: player.score += WildlifeScoringCard.pointConversionHawk(numbers.get(1), false, true, false);
      case THIRD: player.score += WildlifeScoringCard.pointConversionHawk(numbers.get(2), false, false, true);
      //case FOURTH:
    }
  }
  
}

