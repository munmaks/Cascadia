package fr.uge.data;


import fr.uge.data.core.Game;
import fr.uge.data.core.GameBoard;
import fr.uge.data.core.Player;
import fr.uge.data.core.TurnManager;
import fr.uge.data.environment.Cell;
import fr.uge.data.environment.Coordinates;
import fr.uge.data.environment.Tile;
import fr.uge.data.environment.WildlifeType;

import java.util.List;
import java.util.Objects;

/**
 * The CascadiaData class stores all relevant pieces of information for the game
 * status.
 *
 * @author MUNAITPASOV M.
 * @author MAOUCHE M.
 */
public class CascadiaData {

  /**
   * Game instance, which stores all the game data, such as the game board, the
   * players, and the turn manager.
   */
  private final Game game;

  /**
   * Coordinates of the first selected cell.
   *
   * @param players     List of players.
   * @param gameBoard   Game board.
   * @param turnManager Turn manager.
   * @param version     Version of the game.
   */
  public CascadiaData(GameBoard gameBoard, TurnManager turnManager, List<Player> players,
      int version) {
    Objects.requireNonNull(gameBoard);
    Objects.requireNonNull(turnManager);
    Objects.requireNonNull(players);
    this.game = new Game(gameBoard, turnManager, players, version);
  }

  /**
   * Checks if the choice is valid.
   * 
   * @param choice
   * @return
   */
  private boolean isValidChoice(int choice) {
    return choice >= 0 && choice < game.board().getCopyOfTiles().size();
  }

  /**
   * Gets the tile by choice.
   * 
   * @param choice Choice of the tile.
   * @return Tile
   */
  public Tile getTileByChoice(int choice) {
    if (!isValidChoice(choice)) {
      throw new IllegalArgumentException("Invalid Tile choice");
    }
    return game.board().getTile(choice);
  }

  /**
   * Gets the Animal type by choice.
   * 
   * @param choice Choice of the animal.
   * @return Animal
   */
  public WildlifeType getWildlifeTypeByChoice(int choice) {
    if (!isValidChoice(choice)) {
      throw new IllegalArgumentException("Invalid Animal choice");
    }
    return game.board().getToken(choice);
  }

  /**
   * Gets the current player.
   * 
   * @return Current player.
   */
  public Player getCurrentPlayer() {
    var currentPlayerIndex = game.turnManager().getCurrentPlayerIndex();
    return game.getPlayerByIndex(currentPlayerIndex);
  }

  /**
   * Gets the game board.
   * 
   * @return Game board.
   */
  public GameBoard getGameBoard() { return game.board(); }

  /**
   * Gets the players.
   * 
   * @return List of players.
   */
  public List<Player> getPlayers() { return game.getPlayers(); }

  /**
   * Tests whether the tokens must be updated.
   * 
   * @return True if the tokens must be updated.
   */
  public boolean tokensMustBeUpdated() { return game.board().tokensNeedUpdate(); }

  /**
   * Tests whether the tokens could be updated.
   * 
   * @return True if the tokens could be updated.
   */
  public boolean tokensCouldBeUpdated() { return game.board().tokensCanBeUpdated(); }

  /**
   * Updates the tokens.
   * 
   * @return void
   */
  public void updateTokens() { game.board().updateTokens(); }

  /**
   * Handles the turn change.
   * 
   * @return void
   */
  public void handleTurnChange() {
    game.turnManager().changePlayer();
    game.turnManager().nextTurn();
    /* that means, next person can change tokens (if needed) */
    game.board().setDefaultTokensAreUpdated();
  }

  /**
   * Tests whether the game has ended.
   * 
   * @return True if the game has ended.
   */
  public boolean isGameEnd() { return game.turnManager().isGameEnd(); }

  /**
   * Gets the cell from the coordinates.
   * 
   * @param coordinates
   * @return Cell
   */
  public Cell getCellFromCoordinates(Coordinates coordinates) {
    return getCurrentPlayer().getEnvironment().getCell(coordinates);
  }

  /**
   * Places the tile if possible.
   * 
   * @param chosenTile
   * @param chosenCoordinates
   * @return True if the tile was placed successfully.
   */
  public boolean placeTileIfPossible(Tile chosenTile, Coordinates chosenCoordinates) {
    var player = getCurrentPlayer();
    var possibleCells = player.getEnvironment().getPossibleCells();
    var environment = player.getEnvironment();

    return possibleCells.contains(chosenCoordinates)
        && environment.placeTile(getCellFromCoordinates(chosenCoordinates), chosenTile);
  }

  /**
   * Places the animal if possible.
   * 
   * @param chosenAnimal
   * @param chosenCoordinates
   * @return True if the animal was placed successfully.
   */
  public boolean placeAnimalIfPossible(WildlifeType chosenAnimal, Coordinates chosenCoordinates) {
    return getCurrentPlayer().getEnvironment()
        .placeAnimal(getCellFromCoordinates(chosenCoordinates), chosenAnimal);
  }

  /**
   * Gets the current player's cells.
   * 
   * @return List of cells.
   */
  public List<Cell> getCurrentPlayerCells() {
    return getCurrentPlayer().getEnvironment().getCells();
  }

  /**
   * Tests if the player has won.
   *
   * @return True if the player has won by finding all pairs of objects, and False
   *         otherwize.
   */
  public boolean win() { return game.turnManager().isGameEnd(); }
}
