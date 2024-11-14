package fr.uge.scoring;

import java.util.Objects;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import fr.uge.environment.Environment;
import fr.uge.environment.WildlifeToken;
import fr.uge.environment.Cell;

import fr.uge.util.Constants;


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
  public int calculateScore(WildlifeToken token) {
    int totalScore = 0;
    var cellsWithToken = getCellsWithToken(token);  /* list of cell where similaire token is present */

    var iterator = cellsWithToken.iterator();
    /* we need to check coordinates to determine
     * if there some neighbors with current cell and
     * check them if they're connected */
    while (iterator.hasNext()) {
      
      /* now we must check if there any neighbors that connected with another similar token,
       * so we add in hashmap: their occurrences and their size (for exemple two groups of size 3)*/
      var neighbors = env.getNeighborsCells(iterator.next());
      iterator.remove();  // delete current
      // totalScore += count;// pointsForGroup * count;
    }
    return totalScore;
  }
  
  
  public final int getScore() {
    return 0;
  }
  
}
