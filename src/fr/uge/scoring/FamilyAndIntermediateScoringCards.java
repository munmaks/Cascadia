package fr.uge.scoring;

import fr.uge.core.Player;
import fr.uge.environment.Cell;
import fr.uge.environment.Coordinates;
import fr.uge.environment.Environment;
import fr.uge.environment.HabitatTile;
import fr.uge.environment.TileType;
import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;
import fr.uge.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;


public final class FamilyAndIntermediateScoringCards implements WildlifeScoringCard {

  private final Environment env;
  private final int version;
  private final boolean isIntermediateScoringCard;
  private int group = 0;
  private int count = 0;

  private static final Map<Integer, Integer> FAMILY_GROUP_SIZE_TO_POINTS = Map.of(
    1, 2,
    2, 5,
    3, 9
  );

  private static final Map<Integer, Integer> INTERMEDIATE_GROUP_SIZE_TO_POINTS = Map.of(
    2, 5,
    3, 8,
    4, 12
  );

  public FamilyAndIntermediateScoringCards(Environment env, int version, boolean isIntermediateScoringCard) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.env = Objects.requireNonNull(env);
    this.version = version;
    this.isIntermediateScoringCard = isIntermediateScoringCard;
  }
  
  
  
  private List<Cell> getCellsWithToken(WildlifeToken token){
    var cellsWithToken = new ArrayList<Cell>();
    var listOfCells = env.getCells();
    for (var cell : listOfCells) {
      if (token.equals(cell.getTile().getAnimal())) {
        cellsWithToken.add(cell);
      }
    }
    return List.copyOf(cellsWithToken);
  }



//  @Override
  public Map<Integer, Integer> returnWildlifeTokenMap(WildlifeToken token) {
    // List of cells containing the given token
    var cellsWithToken = new ArrayList<Cell>(getCellsWithToken(token));


    var map = new HashMap<Integer, Integer>();
    var visited = new HashSet<Cell>(); // Set to track visited cells

    for (Cell cell : cellsWithToken) {
        if (visited.contains(cell)) {
            continue; // Skip already processed cells
        }

        // Perform a BFS or DFS to find all connected cells in this group
        int groupSize = calculateGroupSize(cell, token, visited);

        // Update the map with the group size
        map.put(groupSize, map.getOrDefault(groupSize, 0) + 1);
    }

    return Map.copyOf(map); // Return an immutable copy of the map
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
  private int calculateGroupSize(Cell start, WildlifeToken token, Set<Cell> visited) {
    int size = 0;
    Queue<Cell> queue = new LinkedList<>();
    queue.add(start);
    visited.add(start);

    while (!queue.isEmpty()) {
        Cell current = queue.poll();
        size++;

        // Get neighbors with the same token
        var neighbors = env.getNeighborsCells(current);
        var validNeighbors = new ArrayList<Cell>();

        for (var neighbor : neighbors) {
            if (!visited.contains(neighbor) && token.equals(neighbor.getTile().getAnimal())) {
              validNeighbors.add(neighbor);
            }
        }

        // Add neighbors to the queue and mark them as visited
        for (Cell neighbor : validNeighbors) {
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
          int pointsForGroup = FAMILY_GROUP_SIZE_TO_POINTS.getOrDefault(groupSize, 9);  /* if */

          totalScore += pointsForGroup * count;
      }
      return totalScore;
  }


  public final int getScore() {
    return 0;
  }
  
  public static void main(String[] args) {
    // Test the FamilyScoringCard class
    var version = Constants.VERSION_SQUARE;
    var player = new Player("Alice", version);
    var player2 = new Player("Bob", version);

    var bearToken = new WildlifeToken(WildlifeType.BEAR);
    var riverTile = new HabitatTile(
        new TileType[]{ TileType.RIVER },
        new WildlifeType[]{ WildlifeType.ELK, WildlifeType.BEAR }
      );
    riverTile.placeAnimal(bearToken);

    var bearToken2 = new WildlifeToken(WildlifeType.BEAR);
    var forestTile = new HabitatTile(
        new TileType[]{ TileType.FOREST },
        new WildlifeType[]{ WildlifeType.HAWK, WildlifeType.BEAR }
      );
    forestTile.placeAnimal(bearToken2);


    var bearToken3 = new WildlifeToken(WildlifeType.BEAR);
    var wetlandTile = new HabitatTile(
        new TileType[]{ TileType.WETLAND },
        new WildlifeType[]{ WildlifeType.HAWK, WildlifeType.BEAR }
      );
    wetlandTile.placeAnimal(bearToken3);



    var bearToken4 = new WildlifeToken(WildlifeType.BEAR);
    var mountainTile = new HabitatTile(
        new TileType[]{ TileType.MOUNTAIN },
        new WildlifeType[]{ WildlifeType.HAWK, WildlifeType.BEAR }
      );
    mountainTile.placeAnimal(bearToken4);

    var bearToken5 = new WildlifeToken(WildlifeType.BEAR);
    var mountainTile2 = new HabitatTile(
        new TileType[]{ TileType.MOUNTAIN },
        new WildlifeType[]{ WildlifeType.HAWK, WildlifeType.BEAR }
      );
    mountainTile2.placeAnimal(bearToken5);

    var bearToken6 = new WildlifeToken(WildlifeType.BEAR);
    var mountainTile3 = new HabitatTile(
        new TileType[]{ TileType.MOUNTAIN },
        new WildlifeType[]{ WildlifeType.HAWK, WildlifeType.BEAR }
      );
    mountainTile3.placeAnimal(bearToken6);
  


    var list = new Coordinates[] {
      new Coordinates(0, 0),
      new Coordinates(0, 1),
      new Coordinates(1, 0),
      new Coordinates(1, 1),
      new Coordinates(5, 5),
      new Coordinates(4, 4),
      new Coordinates(4, 5)
    };
    var coords1 = new Coordinates(1, 1);
    var coords2 = new Coordinates(0, 1);

    var env = player.getEnvironment();
    // env.getCell(coords1).placeTile(riverTile);
    // env.getCell(coords2).placeTile(forestTile);

    env.placeTile(env.getCell(list[0]), riverTile);
    env.placeTile(env.getCell(list[1]), forestTile);
    env.placeTile(env.getCell(list[2]), wetlandTile);
    env.placeTile(env.getCell(list[4]), mountainTile);
    env.placeTile(env.getCell(list[5]), mountainTile2);
    env.placeTile(env.getCell(list[6]), mountainTile3);

    // System.out.println("Player 1: " + player.toString());
    // System.out.println("Player 2: " + player2.getEnvironment().toString());

    // System.out.println("Neighbors for coords ( " + coords2.y() + ", " + coords2.x() + " )\n"
    //                   + env.getNeighborsCells(env.getCell(coords2)));


    var card = new FamilyAndIntermediateScoringCards(env, version, false);
    // Test the returnWildlifeTokenMap method
    var map = card.returnWildlifeTokenMap(new WildlifeToken(WildlifeType.BEAR));
    System.out.println("Bear groups: " + map);

    System.out.println("Score for bears: " + card.calculateScore(map));

    map = card.returnWildlifeTokenMap(new WildlifeToken(WildlifeType.ELK));
    System.out.println("ELK groups: " + map);

    map = card.returnWildlifeTokenMap(new WildlifeToken(WildlifeType.HAWK));
    System.out.println("Hawk groups: " + map);
  }

}
