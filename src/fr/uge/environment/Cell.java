package fr.uge.environment;

import java.util.List;
import java.util.Objects;

/**
 * Cell class for represent a cell where a tile can be placed */
public final class Cell {

  /**
   *  ID of the cell.
   */
  private final int id;


  /**
   *  Occupation status of tile represented by the cell.
   */
  private static boolean occupied = false;

  private static int version = 0;

  /**
   * By default it's empty type of tile on this cellule
   * */
  private static Tile tile = new EmptyTile();


  /* if it's last version - 3, so there 6 neighbors, otherwise it's only 4 neighbors
   * */
  private static final int maxRotations = 6;


  /** <p>for version 1 - 2, we needn't rotation</p>
   * <p>for version 3:</p>
   * <p>1 turn clockwise = 60 degrees, 6 turns clockwise = 360 == 0 degrees.<br>
   * 1 turn counter clockwise = 300 degrees, 6 turns clockwise 360 == 0 degrees</p>
   * */
  private static int currentRotation = 0;

  
  /** <p>4 - 6 neighbors depends from game version </p>*/ 
  private static Tile[] neighbors; 

  private static int maxNeighbors;

  /**
   * Creates a new cell containing a card to to remember.
   * 
   * @param i ID of the card to remember.
   */
  public Cell(int i, int gameVersion) {
    if (gameVersion <= 0 || gameVersion > 4) {
      throw new IllegalArgumentException("Game Version can only be 1, 2 or 3");
    }
    maxNeighbors = (gameVersion == 3) ? (6) : (4);
    neighbors = new Tile[maxNeighbors];
    id = i;
    version = gameVersion;

  }

  
  public final List<Tile> neighbors(int x, int y){
    var list = new ArrayList<Tile>();
    if (version == 3) {   /* hexagonal, so 6 neighbors */
      
      list.add(tile);
    }
    
    return List.copyOf(list);
  }
  
  
  /**
   * Makes the card invisible, which shall result in showing the card's back.
   */
  public void hide() {
    visible = false;
  }

  /**
   * Makes the card visible, which shall result in showing the card's face.
   */
  public void show() {
    visible = true;
  }

  /**
   * Gets the ID of the object to remember.
   * 
   * @return ID of the object to remember.
   */
  public int id() {
    return id;
  }

  /**
   * Gets the state (visible or invisible) of the cell.
   * 
   * @return True if the card is visible, false otherwise.
   */
  public boolean visible() {
    return visible;
  }
  
  
  
  
  public final void turnСlockwise() {
    if (version == 1) {
      return;
    }
    currentRotation = (currentRotation + 1) % maxRotations;
  }


  public final void turnСounterСlockwise() {
    if (version == 1) {
      return;
    }
    currentRotation = (currentRotation - 1 + maxRotations) % maxRotations;
  }


  public final int getRotation() {
    if (version == 1) {
      return 0;
    }
    return currentRotation;
  }


}
  