package fr.uge.ui;


import fr.uge.core.Game;
import fr.uge.core.GameBoard;
import fr.uge.core.Player;
import fr.uge.core.TurnManager;
import fr.uge.environment.Cell;
import fr.uge.environment.Coordinates;
import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeType;

import java.util.List;

/**
 * The CascadiaData class stores all relevant pieces of information for the game
 * status.
 *
 * @author MUNAITPASOV M.
 * @author NOM M. (MEHDI)
 */
public class CascadiaData {

  // game data
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
    this.game = new Game(gameBoard, turnManager, players, version);

  }

  /**
   * Gets the tile by choice.
   * 
   * @param choice Choice of the tile.
   * @return Tile
   */
  public Tile getTileByChoice(int choice) { return game.board().getTile(choice); }

  /**
   * Gets the Animal type by choice.
   * 
   * @param choice Choice of the animal.
   * @return Animal
   */
  public WildlifeType getWildlifeTypeByChoice(int choice) {
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

    // if (possibleCells.stream().anyMatch(coordinates ->
    // coordinates.equals(chosedCoordinates))) {
    // var currCell = environment.getCell(chosedCoordinates);
    // if (environment.placeTile(currCell, chosedTile)) {
    // // System.out.println("Tile was placed successfully"); // for test, to delete
    // return true;
    // }
    // }
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
   * Tests if the player has won.
   *
   * @return True if the player has won by finding all pairs of objects, and False
   *         otherwize.
   */
  public boolean win() { return game.turnManager().isGameEnd(); }
}
