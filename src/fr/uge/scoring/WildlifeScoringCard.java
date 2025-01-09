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

	/**
	 * Take all the cells in the neighborhood of cell with an animal in it and stock them in a map with their number
	 *
	 * @param cell the cell which we want to get his neighbor's Wildlife type and number
	 * @param player current player which we want to calculate his score
	 * @return a Map which associate the Wildlife type of the cell's neighborhood's cells with their respective number
	 */
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

	/**
	 * Scans all the cells in cellsWithAnimal, then creat a Map<Cell, Map<WildlifeType, Integer>> which for each cell we got the Wildlife type and his respective number of his neighborhood
	 *
	 * @param cellsWithAnimal A list of cells with only one Wildlife type of animal
	 * @param player current player which we want to calculate his score
	 * @return a Map<Cell, Map<WildlifeType, Integer>> with information of the neighborhood of each cell in cellsWithAnimal
	 */
	static Map<Cell, Map<WildlifeType, Integer>> countNeighborsForAnimals(List<Cell> cellsWithAnimal, Player player) {
		Map<Cell, Map<WildlifeType, Integer>> animalNeighborCounts = new HashMap<>();
		for (Cell animalCell : cellsWithAnimal) {
			Map<WildlifeType, Integer> speciesCount = countNeighborSpecies(animalCell, player);
			animalNeighborCounts.put(animalCell, speciesCount);
		}
		return animalNeighborCounts;
	}

	/**
	 * Scan all the values in speciesCount and, depends on the parameter, count specifically one or multiple type of animal in speciesCount than return the result
	 *
	 * @param speciesCount a Map<WildlifeType, Integer> who stock the animal and their respective number in the neighborhood of a cell
	 * @param exception a boolean who permits, if false, to skip the count of the parameter animal
	 * @param animal the wildlife Type of the animal that we maybe want to skip or not, depends on the choice of the player
	 * @param onlyMostCommonAnimal a boolean who permits, if true, to count only the most present Wildlife type in speciesCount
	 * @return either the sum of species in speciesCount (with or without a specific animal) or only the most present animal in species count
	 */
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

	/**
	 * count recursively the size of a group of connected cells with the  WildlifeType targetAnimal
	 *
	 * @param cell the Cell which we want to count the size of his group
	 * @param visited a Set of Cell who stock the cells who are already visited
	 * @param player current player which we want to calculate his score
	 * @param targetAnimal the Wildlife type of the animal that we want to count the size of his group
	 * @return the size of the group of the specified animal
	 */
	static int groupAnimal(Cell cell, Set<Cell> visited, Player player, WildlifeType targetAnimal) {
		if (visited.contains(cell) || cell.getAnimal() != targetAnimal) {
			return 0;
		}
		visited.add(cell);
		int score = 1;
		var neighbors = player.getEnvironment().getNeighbors(cell);
		for (var neighbor : neighbors) {
			score += groupAnimal(neighbor, visited, player, targetAnimal);
		}
		return score;
	}

	/**
	 * calculate the size of all the group of connected cells with targetAnimal
	 *
	 * @param cellsWithAnimal A list of Cell with a specific WildlifeType in the player's environment
	 * @param player current player which we want to calculate his score
	 * @param targetAnimal the animal WildlifeType that we want to focus for our research
	 * @return a list of integer where each integer represents the size of a group
	 */
	static List<Integer> calculateGroupScore(List<Cell> cellsWithAnimal, Player player, WildlifeType targetAnimal) { // renvoie un liste avec la taille des groupe constitué
		var visited = new HashSet<Cell>();
		var groupScores = new ArrayList<Integer>();

		for (var cell : cellsWithAnimal) {
			if (!visited.contains(cell)) {
				int groupSize = groupAnimal(cell, visited, player, targetAnimal);
				if (groupSize > 0) {
					groupScores.add(groupSize);
				}
			}
		}
		return groupScores;
	}

	/*
	static public boolean notANeighbor(Cell Animal, WildlifeType type, Player player) {
		List<Cell> neighbors = player.getEnvironment().getNeighbors(Animal);
		for(var each : neighbors){
			if(each.getAnimal().equals(type)){
				return false; // si l'animal fait partie des voisins, il renvoie faux
			}
		}
		return true;
	}
	*/

	/**
	 * Check if cell1 and cell2 are neighbors and have the same Wildlife type
	 *
	 * @param cell1 a cell where we want to verify if cell2 is his neighbor
	 * @param cell2 a cell where we want to verify if cell1 is his neighbor
	 * @param player current player which we want to calculate his score
	 * @return true if cell1 and cell2 have the same Wildlife type and are neighbor, false otherwise
	 */
	static boolean sameWildlifeTypeInNeighborhood(Cell cell1, Cell cell2, Player player) {
		List<Cell> neighbors = player.getEnvironment().getNeighbors(cell1);
		if(neighbors.contains(cell2)){ // vérifie si ils sont voisins
			return cell1.getAnimal().equals(cell2.getAnimal()); // vérifie si ils ont le meme type et return true si c'est le cas
		}
		return false;
	}

	/**
	 * Take the value of each and depend on his value, return his point's conversion
	 *
	 * @param each the value that we want to convert
	 * @return the conversion in point depend on the value of each
	 */
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

	/**
	 * Take the value of each and depend on his value, return his point's conversion
	 *
	 * @param each the value that we want to convert
	 * @return the conversion in point depend on the value of each
	 */
	static private int convertForRow(int each) {
		return switch (each){
			case 1 -> 2;
			case 2 -> 5;
			case 3 -> 9;
			default -> 13;
		};
	}

	/**
	 * Take the value of each and depend on his value and the boolean parameters return his point's conversion
	 *
	 * @param each the value that we want to convert
	 * @param row a boolean who permits to convert each in a different way
	 * @param group a boolean who permits to convert each in a different way
	 * @return the conversion in point depend on the value of each and the boolean parameters
	 */
	static int pointConversionElk(int each, boolean row, boolean group) {
		if (group) {
			return convertForGroup(each);
		}
		if (row) {
			return convertForRow(each);
		}
		return 0;
	}

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
	 * Take the value of each and depend on his value and the boolean parameters return his point's conversion
	 *
	 * @param groupSize a list of integer where we take the number of group of Bear depends on his size
	 * @param pair a boolean who permits to convert each in a different way
	 * @param oneToThree a boolean who permits to convert each in a different way
	 * @param twoToFour a boolean who permits to convert each in a different way
	 * @return the conversion in point depend on the value of each and the boolean parameters
	 */
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

	/**
	 * Take the value of each and depend on his value, return his point's conversion
	 *
	 * @param groupSize a list of integer where we take the number of group of Bear depends on his size
	 * @return the conversion in point depend on the value of each
	 */
	static private int convertForPair(ArrayList<Integer> groupSize) {
		return switch (groupSize.getFirst()){
			case 1 -> 4;
			case 2 -> 11;
			case 3 -> 19;
			default -> 27;
		};
	}

	/**
	 * Take the value of each and depend on his value, return his point's conversion
	 *
	 * @param groupSize a list of integer where we take the number of group of Bear depends on his size
	 * @return the conversion in point depend on the value of each
	 */
	static private int convertForOneToThree(ArrayList<Integer> groupSize) {
		int res = 0;
		if(groupSize.getFirst()!=0 && groupSize.get(1)!=0 && groupSize.get(2)!=0) res = 3;
		res += groupSize.getFirst()*2;
		res += groupSize.get(1)*5;
		res += groupSize.get(2)*8;
		return res;
	}

	/**
	 * Take the value of each and depend on his value, return his point's conversion
	 *
	 * @param groupSize a list of integer where we take the number of group of Bear depends on his size
	 * @return the conversion in point depend on the value of each
	 */
	static private int convertForTwoToFour(ArrayList<Integer> groupSize) {
		int res = 0;
		res += groupSize.get(1)*5;
		res += groupSize.get(2)*8;
		res += groupSize.get(2)*13;
		return res;
	}

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
	 * Take the value of each and depend on his value and the boolean parameters return his point's conversion
	 *
	 * @param each the value that we want to convert
	 * @param alone a boolean who permits to convert each in a different way
	 * @param pair a boolean who permits to convert each in a different way
	 * @param multiplePair a boolean who permits to convert each in a different way
	 * @return the conversion in point depend on the value of each and the boolean parameters
	 */
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

	/**
	 * Take the value of each and depend on his value, return his point's conversion
	 *
	 * @param each the value that we want to convert
	 * @return the conversion in point depend on the value of each
	 */
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

	/**
	 * Take the value of each and depend on his value, return his point's conversion
	 *
	 * @param each the value that we want to convert
	 * @return the conversion in point depend on the value of each
	 */
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

