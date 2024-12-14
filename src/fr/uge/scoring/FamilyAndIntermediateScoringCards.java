package fr.uge.scoring;

// import fr.uge.core.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.uge.core.Player;
import fr.uge.environment.Cell;
import fr.uge.environment.Environment;
// import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;


public final class FamilyAndIntermediateScoringCards implements WildlifeScoringCard {

  // private Environment env;
  // private final int version;
  private final int isIntermediateScoringCard;

  private static final Map<Integer, Integer> FAMILY_GROUP_SIZE_TO_POINTS = Map.of(
    0, 0, /* normally we don't need */
    1, 2,
    2, 5,
    3, 9
  );

  private static final Map<Integer, Integer> INTERMEDIATE_GROUP_SIZE_TO_POINTS = Map.of(
    0, 0, /* don't need */
    1, 0, /* don't need */
    2, 5,
    3, 8,
    4, 12
  );


  /**
   * Constructs a new instance of the FamilyAndIntermediateScoringCards class.
   *
   * @param env The player's environment.
   * @param version The game version.
   * @param isIntermediateScoringCard <b>true</b> if the card is an intermediate scoring card, <b>false</b> if it is a family scoring card.
   */
  public FamilyAndIntermediateScoringCards(
      // int version,
      int isIntermediateScoringCard
    ){
    // if (!Constants.isValidVersion(version)) {
    //   throw new IllegalArgumentException(Constants.IllegalVersion);
    // }
    // this.version = version;
    this.isIntermediateScoringCard = isIntermediateScoringCard;
  }
  
  
  /**
   * Returns a list of cells containing the given wildlife token.
   * 
   * @param token Wildlife token to search for.
   * @return List of cells containing the given token.
   */
  private List<Cell> getCellsWithToken(Environment env, WildlifeType token){
    var listOfCells = env.getCells();
    var cellsWithToken = new ArrayList<Cell>();
    for (var cell : listOfCells){
      if (token.equals(cell.getAnimal())){
        cellsWithToken.add(cell);
      }
    }
    return List.copyOf(cellsWithToken);
  }



  public Map<Integer, Integer> getWildlifeTokenMap(Environment env, WildlifeType token) {
    Objects.requireNonNull(env);
    Objects.requireNonNull(token);

    var cellsWithToken = getCellsWithToken(env, token); // List of cells containing the given token
    var visited = new HashSet<Cell>(); // set to track visited cells

    return cellsWithToken.stream()
                        .filter(cell -> !visited.contains(cell))
                        .map(cell -> calculateGroupSize(env, cell, token, visited))
                        .collect(Collectors.groupingBy(
                          Function.identity(),
                          Collectors.summingInt(size -> 1)
                        ));
  } 


  /**
   * Returns a map of group sizes to their respective counts for a given wildlife token.
   * 
   * @return A map of group sizes to their respective counts.
   * 
   * @param token The wildlife token to search for.
   */
  // public Map<Integer, Integer> getWildlifeTokenMap(Environment env, WildlifeType token) {
  //   Objects.requireNonNull(env);
  //   Objects.requireNonNull(token);
  //   var cellsWithToken = getCellsWithToken(env, token); // List of cells containing the given token
  //   var map = new HashMap<Integer, Integer>();
  //   var visited = new HashSet<Cell>();    // set to track visited cells
  //   for (var cell : cellsWithToken) {
  //     if (visited.contains(cell)) { continue; }   // skip already processed cells
  //     var groupSize = calculateGroupSize(env, cell, token, visited);   // find all connected cells in this group
  //     map.put(groupSize, map.getOrDefault(groupSize, 0) + 1);     // update the map with the group size
  //   }
  //   return Map.copyOf(map);   // copy of the map
  // }



  private static boolean isValidNeighbor(Cell neighbor, WildlifeType token, Set<Cell> visited) {
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
  private int calculateGroupSize(Environment env, Cell start, WildlifeType token, Set<Cell> visited){
    int size = 0;
    Queue<Cell> queue = new LinkedList<>();
    visited.add(start);
    queue.add(start);
    while (!queue.isEmpty()) { // Cell current = queue.poll();
      ++size;
      var neighbors = env.getNeighbors(queue.poll());  // get neighbors with the same token
      var validNeighbors = new ArrayList<Cell>();
      for (var neighbor : neighbors) {
        if (isValidNeighbor(neighbor, token, visited)) { validNeighbors.add(neighbor); }
      }
      for (Cell neighbor : validNeighbors){   // add neighbors to the queue and mark them as visited
        visited.add(neighbor);
        queue.add(neighbor);
      }
    }
    return size;
  }


  /**
   * Calculates the score for a set of contiguous wildlife groups based on the Intermediate scoring rules.
   *
   * @param wildlifeGroups Map of wildlife group sizes to their respective counts.
   * @return Total score for the wildlife groups.
   */
  public int calculateScore(Map<Integer, Integer> wildlifeGroups) {
    int totalScore = 0;

    // Calculate score by multiplying each group size's score with its count
    for (var entry : wildlifeGroups.entrySet()) {
      int groupSize = entry.getKey();
      int count = entry.getValue();
      int pointsForGroup;
      if (isIntermediateScoringCard == 2) {
        pointsForGroup = INTERMEDIATE_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize, Constants.INTERMEDIATE_FOUR_AND_PLUS);
      } else {
        pointsForGroup = FAMILY_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize, Constants.FAMILY_THREE_AND_PLUS);
      }
      totalScore += pointsForGroup * count;
    }
    return totalScore;
  }


  public final int getFamilyAndIntermediateGroupSizeToPoints(int groupSize) {
    if (isIntermediateScoringCard == 2) {
      return INTERMEDIATE_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize, Constants.INTERMEDIATE_FOUR_AND_PLUS);
    } else {
      return FAMILY_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize, Constants.FAMILY_THREE_AND_PLUS);
    }
  }



  public final int getScore(Environment environment) {
    int score = 0;
    var wildlifeTokens = WildlifeType.values();  /* get all wildlife tokens */

    for (int i = 0; i < wildlifeTokens.length; ++i) {
      var token = wildlifeTokens[i];
      var map = getWildlifeTokenMap(environment, token);
      score += calculateScore(map);
    }

    return score;
  }
  
}
