package fr.uge.scoring;

/*
Elk are scored for forming groups that match the specific shape or formation depicted on the card.
Elk groups can be placed adjacent to each other, but each elk may only score once for a single group or formation.
When scoring connected elk groups, always choose the interpretation that yields the highest point total.
There are card clarifications:
- (A) Score for groups arranged in straight lines,
      connecting flat sides of hexagons in any orientation.
- (B) Score for groups matching the exact shapes shown, in any orientation.
- (C) Score for each contiguous group of elk, with points increasing based on size and shape,
      which can be of any configuration.
- (D) Elk groups must be arranged in a circular formation as illustrated.
*/


import fr.uge.core.Player;
import fr.uge.environment.Cell;
import java.util.*;
import static fr.uge.environment.WildlifeType.ELK;

enum ElkScoringType {

  FIRST,    /* Score for groups arranged in straight lines,
               connecting flat sides of hexagons in any orientation. */

  SECOND,   /* Score for groups matching the exact shapes shown, in any orientation. */

  THIRD,    /* Score for each contiguous group of elk, with points increasing based on size and shape,
               which can be of any configuration. */

  FOURTH;   /* Elk groups must be arranged in a circular formation as illustrated. */
}

public record ElkScoringCard(ElkScoringType version, Player player) implements WildlifeScoringCard {

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
   * Check for all the directions possible if centerCell is part of a triangle of Elk
   *
   * @param centerCell The cell we check if it's part of a triangle
   * @param onlyElk A list of Cell withal the Elk in the player's environment
   * @return true if the centerCell is part of a triangle of Elk, false otherwise
   */
  private boolean isPartOfTriangle(Cell centerCell, List<Cell> onlyElk) {
    int[][] directions = {{-1, 0}, {1, 0},{0, -1}, {0, 1},{1, -1}, {-1, 1}}; // direction pour une grille hexagonal
    for (int i = 0; i < directions.length; i++) {
      for (int j = i + 1; j < directions.length; j++) {
        Cell cell1 = getNextCell(centerCell, directions[i][0], directions[i][1], onlyElk);
        Cell cell2 = getNextCell(centerCell, directions[j][0], directions[j][1], onlyElk);
        if (cell1 != null && cell2 != null && areAdjacent(cell1, cell2)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Check with the directions and the nearly cells if centerCell is a part of a diamond of Elk
   * Make a list of possible value for diamond then confirm it and return the result
   *
   * @param centerCell The cell we check if it's part of a diamond
   * @param onlyElk A list of Cell with all the Elk's tile in the player's environment
   * @return true if the centerCell is a part of a diamond of Elk, false otherwise
   */
  private boolean isPartOfDiamond(Cell centerCell, List<Cell> onlyElk) {
    int[][] directions = {{-1, 0}, {1, 0},{0, -1}, {0, 1},{1, -1}, {-1, 1}};
    List<Cell> potentialDiamond = new ArrayList<>();
    for (var dir : directions) {
      Cell adjacentCell = getNextCell(centerCell, dir[0], dir[1], onlyElk);
      if (adjacentCell != null) {
        potentialDiamond.add(adjacentCell);
      }
    }
    if (potentialDiamond.size() == 4) {
      return areDiamondAdjacent(potentialDiamond);
    }
    return false;
  }

  /**
   * Check if the cell in cells have the shape of a diamond
   *
   * @param cells A list of cells who is maybe a part of a diamond
   * @return true if all the cell in cells are adjacent between them, false otherwise
   */
  private boolean areDiamondAdjacent(List<Cell> cells) {
    for (int i = 0; i < cells.size(); i++) {
      for (int j = i + 1; j < cells.size(); j++) {
        if (!areAdjacent(cells.get(i), cells.get(j))) {
          return false;
        }
      }
    }
    return true;
  }


  /**
   * Take two cells and determine if they are adjacent or not
   *
   * @param cell1 First cell
   * @param cell2 Second cell
   * @return true if adjacent and false if not
   */
  private boolean areAdjacent(Cell cell1, Cell cell2) {
    int dx = Math.abs(cell1.getCoordinates().x() - cell2.getCoordinates().x());
    int dy = Math.abs(cell1.getCoordinates().y() - cell2.getCoordinates().y());
    return (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);
  }

  /**
   * Fill visitedInDiamonds if centerCell is a part of a diamond
   * Check for each direction possible in a hexagonal grid if other cell is present
   * Then if they exist, check if these two cells are adjacent
   *
   * @param centerCell A cell that we want to check if it belongs to a diamond
   * @param onlyElk A list of Cell with all the Elk's tile in the player's environment
   * @param visitedInDiamonds A Set of cell who stock the cells who make a diamond
   */
  private void addDiamondCellsToVisited(Cell centerCell, List<Cell> onlyElk, Set<Cell> visitedInDiamonds) {
    int[][] directions = {{-1, 0}, {1, 0},{0, -1}, {0, 1},{1, -1}, {-1, 1}};
    for (int i = 0; i < directions.length; i++) {
      for (int j = i + 1; j < directions.length; j++) {
        Cell cell1 = getNextCell(centerCell, directions[i][0], directions[i][1], onlyElk);
        Cell cell2 = getNextCell(centerCell, directions[j][0], directions[j][1], onlyElk);
        if (cell1 != null && cell2 != null && areAdjacent(cell1, cell2)) {
          visitedInDiamonds.add(centerCell);
          visitedInDiamonds.add(cell1);
          visitedInDiamonds.add(cell2);
        }
      }
    }
  }

  /**
   * Search and count in the Elk's Cells whose are part of diamond and triangle shape in player's environment
   * Take care of firstly count the diamond one and stock it in a list.
   * Thanks to this, we're sure that the triangle count don't count the diamond one as a triangle
   *
   * @param onlyElk A list of Cell with all the Elk's tile in the player's environment
   * @return a list who count the number of triangle and diamond in the player's environment
   */
  public List<Integer> countTrianglesAndDiamonds(List<Cell> onlyElk) {
    List<Integer> groupCounts = new ArrayList<>();
    Set<Cell> visitedInDiamonds = new HashSet<>();
    int triangleCount = 0;
    int diamondCount = 0;
    for (Cell cell : onlyElk) {
      // vérifier les losanges en priorité
      if (!visitedInDiamonds.contains(cell) && isPartOfDiamond(cell, onlyElk)) {
        diamondCount++;
        addDiamondCellsToVisited(cell, onlyElk, visitedInDiamonds);
      } else if (isPartOfTriangle(cell, onlyElk)) {
        triangleCount++;
      }
    }
    groupCounts.add(triangleCount);
    groupCounts.add(diamondCount);
    return groupCounts;
  }

  /**
   * Search and count for each Elk's cells whose is a part of a line of 4 Elk or less
   *
   * @param onlyElk A list of Cell with all the Elk's tile in the player's environment
   * @return a map with the length of the line for the key and the number of it in the value
   */
  public Map<Integer, Integer> countAlignedElk(List<Cell> onlyElk) {
    Map<Cell, Integer> cellToLineLength = new HashMap<>();
    Map<Integer, Integer> lineLengthCountMap = new HashMap<>();
    for (Cell currentCell : onlyElk) {
      List<Integer> lineLengths = findAlignedLines(currentCell, onlyElk, cellToLineLength);
      for (int length : lineLengths) {
        if (length >= 1 && length <= 4) {
          lineLengthCountMap.put(length, lineLengthCountMap.getOrDefault(length, 0) + 1);
        }
      }
    }
    return lineLengthCountMap;
  }

  /**
   * Finds all the lines of Elk aligned in a specific direction from a given starting cell.
   *
   * @param startCell A cell where the aligned line may start
   * @param onlyElk A list of Cell with all the Elk's tile in the player's environment
   * @param cellToLineLength A map that stores the length of the line to which a given cell belongs
   * @return A list representing the lengths of the aligned lines. Their lengths are between 1 and 4
   */
  private List<Integer> findAlignedLines(Cell startCell, List<Cell> onlyElk, Map<Cell, Integer> cellToLineLength) {
    List<Integer> lineLengths = new ArrayList<>();
    int[] directionsX = { -1, 1, -1, -1, 1, 1 };
    int[] directionsY = { 0, 0, -1, 1, -1, 1 };
    for (int i = 0; i < 6; i++) {
      List<Cell> alignedCells = new ArrayList<>();
      Cell currentCell = startCell;
      while (onlyElk.contains(currentCell) && !alignedCells.contains(currentCell)) {
        alignedCells.add(currentCell);
        currentCell = getNextCell(currentCell, directionsX[i], directionsY[i], onlyElk);
      }
      if (alignedCells.size() >= 1 && alignedCells.size() <= 4) {
        boolean canAddLine = true;
        for (Cell cell : alignedCells) {
          if (cellToLineLength.containsKey(cell) && cellToLineLength.get(cell) >= alignedCells.size()) {
            canAddLine = false;
            break;
          }
        }
        if (canAddLine) {
          for (Cell cell : alignedCells) {
            cellToLineLength.put(cell, alignedCells.size());
          }
          lineLengths.add(alignedCells.size());
        }
      }
    }
    return lineLengths;
  }

  /**
   * Search for a given cell a following cell who is aligned with it
   *
   * @param currentCell a cell who we search if his following cell is aligned
   * @param dx direction for x
   * @param dy direction for y
   * @param onlyElk A list of Cell with all the Elk's tile in the player's environment
   * @return the first cell which is aligned with currentCell
   */
  private Cell getNextCell(Cell currentCell, int dx, int dy, List<Cell> onlyElk) {
    int nextX = currentCell.getCoordinates().x() + dx;
    int nextY = currentCell.getCoordinates().y() + dy;
    return onlyElk.stream()
            .filter(cell -> cell.getCoordinates().x() == nextX && cell.getCoordinates().y() == nextY)
            .findFirst()
            .orElse(null);
  }

  /**
   * Check all the cells in player's environment and count the number of Elk whose in a row, group or shape's formation.
   * Convert directly the result in points and stock them in a List
   *
   * @param player current player which we want to calculate his score
   * @return a list of integer with the points earned for the row, shape, and group's formation
   */
  public ArrayList<Integer> numberElk(Player player){
    ArrayList<Integer> numbers = new ArrayList<>();
    int row = 0, shape = 0, group =0;
    List<Cell> cells = player.getEnvironment().getCells();
    var onlyElk = cells.stream().filter(cell -> cell.getAnimal() == ELK)
            .toList();
    List<Integer> numberElk = WildlifeScoringCard.calculateGroupScore(onlyElk, player, ELK);
    for(var each : numberElk) { // convertis pour chaque groupe son equivalent en point
      group += pointConversionElk(each, false, true);
    }
    Map<Integer, Integer>elkOnRow = countAlignedElk(onlyElk);
    List<Integer>elkOnShape = countTrianglesAndDiamonds(onlyElk);
    row = elkOnRow.entrySet().stream()
                    .mapToInt(line -> pointConversionElk(line.getKey(), true, false) * line.getValue())
                    .sum();
    shape = elkOnRow.entrySet().stream()
            .filter(line -> line.getKey() < 3)
            .mapToInt(line -> pointConversionElk(line.getKey(), true, false) * line.getValue())
            .sum();
    shape += pointConversionElk(3, true, false) * elkOnShape.getFirst(); // meme point gagné pour Lignes que pour Formations
    shape += pointConversionElk(4, true, false) * elkOnShape.getLast();
    numbers.add(row);
    numbers.add(shape);
    numbers.add(group);
    return numbers;
  }

  /**
   * Take the list of points for each formation and associate in function of the version chose
   *
   * @param version the version of card that the player choose
   * @param player the player that we want to actualise his score
   */
  public ElkScoringCard {
    ArrayList<Integer> numbers = numberElk(player);
    switch (version) {
      case FIRST: player.score += numbers.get(0); // line
      case SECOND: player.score += numbers.get(1); // shape
      case THIRD: player.score += numbers.get(2); // group
    }
  }
}
