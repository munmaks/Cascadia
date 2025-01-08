package fr.uge.scoring;

import fr.uge.core.Player;
import fr.uge.environment.Cell;
import fr.uge.environment.WildlifeType;

import java.util.*;

public sealed interface WildlifeScoringCard permits
    FamilyAndIntermediateScoringCards,  /* they're similair, so Family and Scoring card are in the same class */
    BearScoringCard,
    FoxScoringCard,
    SalmonScoringCard,
    ElkScoringCard,
    HawkScoringCard {

	static Map<WildlifeType, Integer> countNeighborSpecies(Cell cell, Player player) {
		Map<WildlifeType, Integer> speciesCount = new HashMap<>();
		List<Cell> neighbors = player.getEnvironment().getNeighbors(cell);
		for (Cell neighbor : neighbors) {
			WildlifeType animal = neighbor.getAnimal();
			if (animal != null) { // Ignorer les cellules sans animaux
				speciesCount.put(animal, speciesCount.getOrDefault(animal, 0) + 1);
			}
		}
		return speciesCount;
	}

	static Map<Cell, Map<WildlifeType, Integer>> countNeighborsForAnimals(List<Cell> cellsWithAnimal, Player player) {
		Map<Cell, Map<WildlifeType, Integer>> animalNeighborCounts = new HashMap<>();
		for (Cell animalCell : cellsWithAnimal) {
			Map<WildlifeType, Integer> speciesCount = countNeighborSpecies(animalCell, player);
			animalNeighborCounts.put(animalCell, speciesCount);
		}
		return animalNeighborCounts;
	}

	static int numberNeighborSpecies(Map<WildlifeType, Integer> speciesCount, boolean exception, WildlifeType animal, boolean onlyMostCommonAnimal){ // si on compte l'exception, exception = 1, sinon exception = 0
		int res = 0;
		ArrayList<WildlifeType> count = new ArrayList<>();
		for(var each : speciesCount.entrySet()) {
			if(each.getKey() == animal) { // l'animal important
				if(exception && !count.contains(each.getKey())) { // vérifie si tous les animaux sont bien différents
					res += each.getValue();  // on compte l'animal
					count.add(each.getKey());
					continue;
				}
				if(onlyMostCommonAnimal){  // si on compte uniquement l'animal le plus présent
					res += each.getValue();
					continue;
				}
				continue; // on ne compte dans aucun cas l'animal
			}
			res += each.getValue();  // sinon on compte juste le nombre d'espece différente autour
		}
		return res;
	}

	static int groupAnimal(Cell cell, Set<Cell> visited, Player player) {
		if (visited.contains(cell)) {
			return 0;
		}
		visited.add(cell);
		int score = 1;
		var neighbors = player.getEnvironment().getNeighbors(cell);
		for (var neighbor : neighbors) {
			score += groupAnimal(neighbor, visited, player);
		}
		return score;
	}

	static List<Integer> calculateGroupScore(List<Cell> cellsWithAnimal, Player player) { // renvoie un liste avec la taille des groupe constitué
		var visited = new HashSet<Cell>();
		var groupScores = new ArrayList<Integer>();

		for (var cell : cellsWithAnimal) {
			if (!visited.contains(cell)) {
				int groupSize = groupAnimal(cell, visited, player);
				if (groupSize > 0) {
					groupScores.add(groupSize);
				}
			}
		}
		return groupScores;
	}

	static public boolean notANeighbor(Cell Animal, WildlifeType type, Player player) {
		List<Cell> neighbors = player.getEnvironment().getNeighbors(Animal);
		for(var each : neighbors){
			if(each.getAnimal().equals(type)){
				return false; // si l'animal fait partie des voisins, il renvoie faux
			}
		}
		return true;
	}

	static boolean sameWildlifeTypeInNeighborhood(Cell cell1, Cell cell2, Player player) {
		List<Cell> neighbors = player.getEnvironment().getNeighbors(cell1);
		if(neighbors.contains(cell2)){ // vérifie si ils sont voisins
			return cell1.getAnimal().equals(cell2.getAnimal()); // vérifie si ils ont le meme type et return true si c'est le cas
		}
		return false;
	}

	static private int convertForGroup(int each) {
		return switch (each) {
			case 1 -> 2;
			case 2 -> 4;
			case 3 -> 7;
			case 4 -> 10;
			case 5 -> 14;
			case 6 -> 18;
			case 7 -> 23;
			default -> 28;
		};
	}

	static private int convertForRow(int each) {
		return switch (each){
			case 1 -> 2;
			case 2 -> 5;
			case 3 -> 9;
			default -> 13;
		};
	}

	static int pointConversionElk(int each, boolean row, boolean group) {
		if (group) {
			return convertForGroup(each);
		}
		if (row) {
			return convertForRow(each);
		}
		return 0;
	}

	static int pointConversionFox(int each, boolean AloneAndMorePresentAnimal, boolean AloneWithoutFox) {
		if (AloneAndMorePresentAnimal) {
			return convertForAloneAndMorePresentAnimal(each);
		}
		if (AloneWithoutFox) {
			return convertForAloneWithoutFox(each);
		}
		return 0;
	}

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

	static private int convertForAloneWithoutFox(int each) {
		return switch (each){
			case 1 -> 3;
			case 2 -> 5;
			default -> 7;
		};
	}

	static int pointConversionBear(ArrayList<Integer> groupSize, boolean pair, boolean oneToThree, boolean twoToFour) {
		if (pair) {
			return convertForPair(groupSize);
		}
		if (oneToThree) {
			return convertForOneToThree(groupSize);
		}
		if (twoToFour) {
			return convertForTwoToFour(groupSize);
		}
		return 0;
	}

	static private int convertForPair(ArrayList<Integer> groupSize) {
		return switch (groupSize.getFirst()){
			case 1 -> 4;
			case 2 -> 11;
			case 3 -> 19;
			default -> 27;
		};
	}

	static private int convertForOneToThree(ArrayList<Integer> groupSize) {
		int res = 0;
		if(groupSize.getFirst()!=0 && groupSize.get(1)!=0 && groupSize.get(2)!=0) res = 3;
		res += groupSize.getFirst()*2;
		res += groupSize.get(1)*5;
		res += groupSize.get(2)*8;
		return res;
	}

	static private int convertForTwoToFour(ArrayList<Integer> groupSize) {
		int res = 0;
		res += groupSize.get(1)*5;
		res += groupSize.get(2)*8;
		res += groupSize.get(2)*13;
		return res;
	}

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

	static private int convertForMaxFive(int each) {
		return switch (each) {
			case 1 -> 2;
			case 2 -> 4;
			case 3 -> 9;
			case 4 -> 11;
			default -> 17;
		};
	}

	static private int convertForThreeToFive(int each) {
		return switch (each) {
			case 3 -> 10;
			case 4 -> 12;
			default -> 15;
		};
	}

	static int pointConversionHawk(int each, boolean alone, boolean pair, boolean multiplePair) {
		if (alone) {
			return convertForAloneHawk(each);
		} else if (pair) {
			return convertForPairHawk(each);
		} else if (multiplePair) {
			return each * 3;
		}
		return 0;
	}

	static private int convertForAloneHawk(int each) {
		return switch (each) {
			case 1 -> 2;
			case 2 -> 5;
			case 3 -> 8;
			case 4 -> 11;
			case 5 -> 14;
			case 6 -> 18;
			case 7 -> 22;
			default -> 26;
		};
	}

	static private int convertForPairHawk(int each) {
		return switch (each) {
			case 2 -> 5;
			case 3 -> 9;
			case 4 -> 12;
			case 5 -> 16;
			case 6 -> 20;
			case 7 -> 24;
			default -> 28;
		};
	}
}

