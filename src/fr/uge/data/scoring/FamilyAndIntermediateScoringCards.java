package fr.uge.data.scoring;


// import fr.uge.core.Player;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.uge.data.environment.Cell;
import fr.uge.data.environment.Environment;
import fr.uge.data.environment.WildlifeType;
import fr.uge.data.util.Constants;

public final class FamilyAndIntermediateScoringCards implements WildlifeScoringCard {

  // private Environment env;
  // private final int version;
  private final int isIntermediateScoringCard;

  private static final Map<Integer, Integer> FAMILY_GROUP_SIZE_TO_POINTS = Map.of(0, 0, 1,
      2, 2, 5, 3, 9);

  private static final Map<Integer, Integer> INTERMEDIATE_GROUP_SIZE_TO_POINTS = Map.of(0,
      0, 1, 0, 2, 5, 3, 8, 4, 12);

  /**
   * Constructs a new instance of the FamilyAndIntermediateScoringCards class.
   *
   * @param isIntermediateScoringCard <b>true</b> if the card is an intermediate scoring
   *                                  card, <b>false</b> if it is a family scoring card.
   */
  public FamilyAndIntermediateScoringCards(int isIntermediateScoringCard) {
    this.isIntermediateScoringCard = isIntermediateScoringCard;
  }

  /**
   * Returns a list of cells containing the given wildlife token.
   * 
   * @param token Wildlife token to search for.
   * @return List of cells containing the given token.
   */
  private List<Cell> getCellsWithToken(Environment env, WildlifeType token) {
    var listOfCells = env.getCells();
    var cellsWithToken = new ArrayList<Cell>();
    for (var cell : listOfCells) {
      if (token.equals(cell.getAnimal())) {
        cellsWithToken.add(cell);
      }
    }
    return List.copyOf(cellsWithToken);
  }

  /**
   * Returns a map of group sizes to their respective counts for a given wildlife token.
   * 
   * @param env   Environment
   * @param token Wildlife token to search for.
   * @return A map of group sizes to their respective counts.
   */
  public Map<Integer, Integer> getWildlifeTokenMap(Environment env, WildlifeType token) {
    Objects.requireNonNull(env);
    Objects.requireNonNull(token);

    var cellsWithToken = getCellsWithToken(env, token);
    var visited = new HashSet<Cell>(); // set to track visited cells

    return cellsWithToken.stream().filter(cell -> !visited.contains(cell))
        .map(cell -> calculateGroupSize(env, cell, token, visited)).collect(
            Collectors.groupingBy(Function.identity(), Collectors.summingInt(size -> 1)));
  }

  /**
   * Checks if a neighbor is valid.
   * 
   * @param neighbor cell to check
   * @param token    wildlife token
   * @param visited  set of visited cells
   * @return true if the neighbor is valid, false otherwise
   */
  private static boolean isValidNeighbor(Cell neighbor, WildlifeType token,
      Set<Cell> visited) {
    return visited.contains(neighbor) && token.equals(neighbor.getAnimal());
  }

  /**
   * Calculates the size of the group of connected cells with the same wildlife token.
   * Uses BFS to explore all connected neighbors.
   *
   * @param start   The starting cell.
   * @param token   The wildlife token to search for.
   * @param visited A set to track visited cells.
   * @return The size of the group.
   */
  private int calculateGroupSize(Environment env, Cell start, WildlifeType token,
      Set<Cell> visited) {

    Queue<Cell> queue = new LinkedList<>();
    visited.add(start);
    queue.add(start);
    return processQueue(env, token, visited, queue);
  }

  /**
   * Processes the queue of cells to calculate the size of the group.
   * 
   * @param env     environment
   * @param token   wildlife token
   * @param visited set of visited cells
   * @param queue   queue of cells to process
   * @return size of the group
   */
  private int processQueue(Environment env, WildlifeType token, Set<Cell> visited,
      Queue<Cell> queue) {
    return Stream.iterate(queue.poll(), Objects::nonNull, cell -> queue.poll())
        .peek(cell -> addValidNeighborsToQueue(env, cell, token, visited, queue))
        .mapToInt(cell -> 1).sum();
  }

  /**
   * Adds valid neighbors to the queue.
   * 
   * @param env     environment
   * @param cell    cell
   * @param token   wildlife token
   * @param visited set of visited cells
   * @param queue   queue of cells
   */
  private void addValidNeighborsToQueue(Environment env, Cell cell, WildlifeType token,
      Set<Cell> visited, Queue<Cell> queue) {
    env.getNeighbors(cell).stream()
        .filter(neighbor -> isValidNeighbor(neighbor, token, visited))
        .forEach(neighbor -> {
          visited.add(neighbor);
          queue.add(neighbor);
        });
  }

  /**
   * Calculates the score for a set of contiguous wildlife groups based on the
   * Intermediate scoring rules.
   *
   * @param wildlifeGroups Map of wildlife group sizes to their respective counts.
   * @return Total score for the wildlife groups.
   */
  public int calculateScore(Map<Integer, Integer> wildlifeGroups) {
    Objects.requireNonNull(wildlifeGroups, "Wildlife groups cannot be null");
    var totalScore = 0;

    /* calculate score by multiplying each group size's score with its count */
    for (var entry : wildlifeGroups.entrySet()) {
      var groupSize = entry.getKey();
      var count = entry.getValue();
      int pointsForGroup;
      if (isIntermediateScoringCard == 2) {
        pointsForGroup = INTERMEDIATE_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize,
            Constants.INTERMEDIATE_FOUR_AND_PLUS);
      } else {
        pointsForGroup = FAMILY_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize,
            Constants.FAMILY_THREE_AND_PLUS);
      }
      totalScore += pointsForGroup * count;
    }
    return totalScore;
  }

  /**
   * Returns the score for a group of a given size based on the card type.
   * 
   * @param groupSize Size of the group.
   * @return Score for the group.
   */
  public final int getFamilyAndIntermediateGroupSizeToPoints(int groupSize) {
    if (isIntermediateScoringCard == 2) {
      return INTERMEDIATE_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize,
          Constants.INTERMEDIATE_FOUR_AND_PLUS);
    } else {
      return FAMILY_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize,
          Constants.FAMILY_THREE_AND_PLUS);
    }
  }

  /**
   * Returns the score for a group of a given size based on the card type.
   * 
   * @param environment Environment
   * @return Score for the group.
   */
  public final int getScore(Environment environment) {
    Objects.requireNonNull(environment, "Environment cannot be null");

    return Arrays.stream(WildlifeType.values())
        .map(token -> getWildlifeTokenMap(environment, token))
        .mapToInt(this::calculateScore).sum();
  }

}
