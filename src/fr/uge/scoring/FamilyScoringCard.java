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


public record FamilyScoringCard(
      Environment env,
      int version
    ) implements WildlifeScoringCard {

  private static int group = 0;
  private static int count = 0;
  
  public FamilyScoringCard {
    Objects.requireNonNull(env);
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    
  }
  
  
  
  private List<Cell> getCellsWithToken(WildlifeToken token){
    ArrayList<Cell> cellsWithToken = new ArrayList<Cell>();
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
        var neighbors = env.getNeighborsCells(current).stream()
            .filter(neighbor -> !visited.contains(neighbor) && 
                                neighbor.getTile().getAnimal().equals(token))
            .toList();

        // Add neighbors to the queue and mark them as visited
        for (Cell neighbor : neighbors) {
            visited.add(neighbor);
            queue.add(neighbor);
        }
    }

    return size;
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
        new TileType[]{ TileType.RIVER, TileType.WETLAND },
        new WildlifeType[]{ WildlifeType.ELK, WildlifeType.BEAR }
      );
    riverTile.placeAnimal(bearToken);

    var bearToken2 = new WildlifeToken(WildlifeType.BEAR);
    var forestTile = new HabitatTile(
        new TileType[]{ TileType.FOREST, TileType.WETLAND },
        new WildlifeType[]{ WildlifeType.ELK, WildlifeType.BEAR }
      );
    forestTile.placeAnimal(bearToken2);

    var coords1 = new Coordinates(0, 0);
    var coords2 = new Coordinates(1, 0);

    var env = player.getEnvironment();
    env.getCell(coords1).placeTile(riverTile);
    env.getCell(coords2).placeTile(forestTile);


    System.out.println("Player 1: " + player.getEnvironment().toString());
    // System.out.println("Player 2: " + player2.getEnvironment().toString());

    var card = new FamilyScoringCard(env, version);
    // Test the returnWildlifeTokenMap method
    var map = card.returnWildlifeTokenMap(new WildlifeToken(WildlifeType.BEAR));
    System.out.println("Bear groups: " + map);

    map = card.returnWildlifeTokenMap(new WildlifeToken(WildlifeType.ELK));
    System.out.println("ELK groups: " + map);

    map = card.returnWildlifeTokenMap(new WildlifeToken(WildlifeType.HAWK));
    System.out.println("Hawk groups: " + map);
  }

}
