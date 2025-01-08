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

public record ElkScoringCard(ElkScoringType version) implements WildlifeScoringCard {


  private boolean isPartOfTriangle(Cell centerCell, List<Cell> onlyElk) {
    int[][] directions = {
            {-1, 0}, {1, 0},
            {-1, -1}, {1, 1},
            {0, -1}, {0, 1}
    };
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

  private boolean isPartOfDiamond(Cell centerCell, List<Cell> onlyElk) {
    int[][] directions = {
            {-1, 0}, {1, 0},
            {0, -1}, {0, 1}
    };
    List<Cell> potentialDiamond = new ArrayList<>();
    for (int[] dir : directions) {
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

  private boolean areAdjacent(Cell cell1, Cell cell2) {
    int dx = Math.abs(cell1.getCoordinates().x() - cell2.getCoordinates().x());
    int dy = Math.abs(cell1.getCoordinates().y() - cell2.getCoordinates().y());
    return (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);
  }



  public List <Integer> countTrianglesAndDiamonds(List<Cell> onlyElk) {
    List <Integer> groupCounts = new ArrayList<>();
    int triangleCount = 0;
    int diamondCount = 0;
    for (Cell cell : onlyElk) {
      if (isPartOfTriangle(cell, onlyElk)) {
        triangleCount++;
      }
      if (isPartOfDiamond(cell, onlyElk)) {
        diamondCount++;
      }
    }
    groupCounts.add(triangleCount);
    groupCounts.add(diamondCount);
    return groupCounts;
  }

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

  private Cell getNextCell(Cell currentCell, int dx, int dy, List<Cell> onlyElk) {
    int nextX = currentCell.getCoordinates().x() + dx;
    int nextY = currentCell.getCoordinates().y() + dy;
    return onlyElk.stream()
            .filter(cell -> cell.getCoordinates().x() == nextX && cell.getCoordinates().y() == nextY)
            .findFirst()
            .orElse(null);
  }

  public ArrayList<Integer> numberElk(Player player){
    ArrayList<Integer> numbers = new ArrayList<>();
    int row = 0, shape = 0, group =0;
    List<Cell> cells = player.getEnvironment().getCells();
    var onlyElk = cells.stream().filter(cell -> cell.getAnimal() == ELK)
            .toList();
    List<Integer> numberElk = WildlifeScoringCard.calculateGroupScore(onlyElk, player);
    for(var each : numberElk) { // convertis pour chaque groupe son equivalent en point
      group += WildlifeScoringCard.pointConversionElk(each, false, true);
    }
    Map<Integer, Integer>elkOnRow = countAlignedElk(onlyElk);
    List<Integer>elkOnShape = countTrianglesAndDiamonds(onlyElk);
    row = elkOnRow.entrySet().stream()
                    .mapToInt(line -> WildlifeScoringCard.pointConversionElk(line.getKey(), true, false) * line.getValue())
                    .sum();
    shape = elkOnRow.entrySet().stream()
            .filter(line -> line.getKey() < 3)
            .mapToInt(line -> WildlifeScoringCard.pointConversionElk(line.getKey(), true, false) * line.getValue())
            .sum();
    shape += WildlifeScoringCard.pointConversionElk(3, true, false) * elkOnShape.getFirst(); // meme point gagné pour Lignes que pour Formations
    shape += WildlifeScoringCard.pointConversionElk(4, true, false) * elkOnShape.getLast();
    numbers.add(row);
    numbers.add(shape);
    numbers.add(group);
    return numbers;
  }

  public ElkScoringCard {
    switch (version) {
      case FIRST:
      case SECOND:
      case THIRD:
      case FOURTH:
    }
  }
}
