package fr.uge.ui;




import java.awt.Color;
import java.awt.Graphics2D;
// import java.awt.Image;
// import java.awt.Toolkit;

import com.github.forax.zen.*;
//import com.github.forax.zen.Application;
//import com.github.forax.zen.PointerEvent;
//import com.github.forax.zen.Application;
//import com.github.forax.zen.Application;

import java.io.IO;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
// import java.util.function.Consumer;

import java.util.Set;

import fr.uge.environment.Cell;
import fr.uge.environment.Coordinates;
import fr.uge.environment.HabitatTile;
import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeToken;
import fr.uge.environment.WildlifeType;

import fr.uge.scoring.FamilyAndIntermediateScoringCards;
import fr.uge.scoring.WildlifeScoringCard;


import fr.uge.core.*;

import fr.uge.util.Constants;





public final class MainMenu {
  /*
   * Main menu contains, bottoms: Play and Settings (to choose number of players and Wildlife Scoring Cards)
   * 
   * */
  private final int version;
  private final int nbPlayers;
  private final List<WildlifeScoringCard> scoringCards = new ArrayList<WildlifeScoringCard>();
  private final boolean isIntermediateScoringCard;

  public MainMenu(int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.version = version;
    this.nbPlayers = 0;   // to do later
    this.isIntermediateScoringCard = false; // to do, later, 0 is Family Card, 1 is Intermediate Card

    if (this.version == Constants.VERSION_SQUARE){
      playSquareTerminal();
    } else if (this.version == Constants.VERSION_HEXAGONAL) {
      /* playHexagonal(); */  // to do later
    }
    /* gameLoopV2();  */

  }


  private String readName(int num){
    return IO.readln("Player " + num + ", what's your name? ");
  }
  

  private int chooseVersion(){
    System.out.println("Choose scoring card: ");
    var choice = IO.readln("1 for Family\n2 for Intermediate \n");
    int familyOrIntermediate = Integer.parseInt(choice);
    if (familyOrIntermediate != 1 && familyOrIntermediate != 2) {
      throw new IllegalArgumentException("Invalid choice");
    }
    // System.out.println("You have chosen " + ( 1 == familyOrIntermediate ? "Family" : "Intermediate") + " Scoring Card");
    return familyOrIntermediate;
  }


  private void showEnvironment(Player player){
    Objects.requireNonNull(player);
    System.out.println("\n\nIt's " + player.name() + "'s turn!");
    System.out.println("\nHere is " + player.name() + "'s environment: ");
    var listCells = player.environment().getCells();
    if (listCells.isEmpty()){
      System.out.println("No cells in the environment");  /* for tests */
    }

    for (var cell : listCells){
      System.out.println(cell.toString());
    }
  }


  private void showGameBoard(GameBoard board){
    Objects.requireNonNull(board);
    System.out.println("\nHere is the game board: ");
    var tiles = board.getCopyOfTiles();
    var tokens = board.getCopyOfTokens();
    for (var i = 0; i < tiles.length; ++i){
      System.out.println((i + 1) + ") " + tiles[i].toString() + " and " + tokens[i].toString());
    }
    System.out.println("");
  }

  private void showPossibleCoordinates(Player player){
    Objects.requireNonNull(player);
    System.out.println("Here are the possible coordinates:\n`empty (x, y)` - neighbor tile");
    var setOfCells = player.environment().getPossibleCells();
    for (var cell : setOfCells){
      // System.out.println(cell.coordinates().toString());
      player.environment().printAllNeighbors(cell);
    }
  }



  private void showPossibleTokenPlacement(Player player, WildlifeToken token){
    Objects.requireNonNull(player);
    Objects.requireNonNull(token);
    System.out.println("Here are the possible coordinates to place the token: ");
    var listOfCells = player.environment().getCells();
    for (var cell : listOfCells){
      if (cell.getTile() instanceof HabitatTile && cell.getTile().canBePlaced(token)){
        System.out.println( cell.toString() + " :: ");
      }
    }
  }

  /**
   * to do later
   */
  private void showScore(Game game){
    System.out.println("Game is over!\nThank you for playing!");

    // Objects.requireNonNull(game);
    // var players = game.players();
    // for (var player : players){
    //   System.out.println(player.name() + " has " + player.getScore() + " points");
    // }
  }


  private Coordinates getCoordinatesFromUser(String message){
    int x, y;
    try (Scanner s = new Scanner(message).useDelimiter(",\\s*")){
      x = s.nextInt();
      y = s.nextInt();
    }
    return new Coordinates(y, x);
  }


  // don't forget to check if already was changed and if so we don't change it
  // setToDefault in `src/fr/uge/core/TurnManager.java`
  /**
   * java doc ...
   * */
  private void handleTokenChange(Game game){
    Objects.requireNonNull(game);
    if (game.board().tokensNeedUpdate()){
      game.board().updateTokens();
      System.out.println("Tokens were updated, because one token had 4 occurences");
      return;
    }
    if (game.board().tokensCanBeUpdated()){
      // showPossibleChangeTokens(game);
    
      var stringTokensToChange = IO.readln("Enter `yes` if you want to change the tokens, otherwise press enter ");
      try (Scanner s = new Scanner(stringTokensToChange).useDelimiter("\\s*")) {
          if (s.hasNext() && s.next().equals("yes")){
              game.board().updateTokens();
          }
      }
      showGameBoard(game.board());
    }
  }



  private int handleUserChoiceTileAndToken(){
    int choice = Integer.parseInt(IO.readln("Please choose from 1 to 4 to take a couple: (Tile, Token)\n"));
    while(!Constants.isValidChoice(choice)){
      choice = Integer.parseInt(IO.readln("Please choose ONLY from 1 to 4 to take a couple: (Tile, Token)\n"));
    }
    return choice;
  }


  private void handleTurnChange(Game game){
    if (game.turnManager().getNeedToTurn()){
      game.turnManager().nextTurn();
      System.out.println("Turns left: " + (Constants.MAX_GAME_TURNS - game.turnManager().getTotalTurns()));
    }
    game.turnManager().changePlayer();
  }


  private void handleTokenPlacement(Player player, WildlifeToken chosedToken){
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedToken);

    /* chosed token from `choice` */
    System.out.println("Now you need to place the wildlife token: " + chosedToken.toString());
    showPossibleTokenPlacement(player, chosedToken);
    
    var userCoordinatesString = IO.readln("Give me coordinates of tile, that you want to place the token on (format: \"x, y\"): ");

    var userCoordinates = getCoordinatesFromUser(userCoordinatesString);
    var currCell = player.environment().getCell(userCoordinates.y(), userCoordinates.x());
    var tokenWasPlaced = player.environment().placeWildlifeToken(currCell, chosedToken);

    if (!tokenWasPlaced){
      System.err.println("Token wasn't placed");   // need to test this
    }
  }


  private void handleTilePlacement(Player player, Tile chosedTile){
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedTile);
    var possibleCoordinates = player.environment().getPossibleCoordinates();
    String userCoordinatesString = null;
    Coordinates userCoordinates = null;
    do {
      userCoordinatesString = IO.readln("Give me coordinates of tile, that you want to place the token on (format: \"x, y\"): ");
      userCoordinates = getCoordinatesFromUser(userCoordinatesString);
      if (possibleCoordinates.contains(userCoordinates)) {
        break;
      }
    } while (true);
    
    var currCell = player.environment().getCell(userCoordinates.y(), userCoordinates.x());
    var tileWasPlaced = player.environment().placeTile(currCell, chosedTile);
  }


  private void showPlayerEnvironmentAndGameBoard(Player player, GameBoard board){
    showEnvironment(player);
    showPossibleCoordinates(player);
    showGameBoard(board);
  }



  private List<Integer> calculateListOfScores(Game game){
    Objects.requireNonNull(game);
    var listOfPlayerScores = new ArrayList<Integer>();
    for (var i = 0; i < game.getPlayerCount(); ++i){
      var player = game.turnManager().getPlayerByIndex(i);
      var score = player.calculateScore();
      listOfPlayerScores.add(score);
    }
    return listOfPlayerScores;
  }

  private void showPlayerScore(Player player, FamilyAndIntermediateScoringCards card) {
    System.out.println(player.name() + " your final score: " + card.getScore(player) + "\n");
  }
  

  private void calculateAndShowScore(Game game, int familyOrIntermediate){
    var listOfPlayerScores = calculateListOfScores(game);
    var scoringCard = new FamilyAndIntermediateScoringCards(familyOrIntermediate);
    
    for (var i = 0; i < game.getPlayerCount(); ++i) {
      showPlayerScore(game.turnManager().getPlayerByIndex(i), scoringCard);
    }
    
    // showPlayersScores(listOfPlayerScores);
    // showScore(game);
  }


  // under test
  private void gameLoopVersionSquare(Game game){
    Objects.requireNonNull(game);
    while (!game.turnManager().isGameEnd()) {
      var currentPlayer = game.turnManager().getCurrentPlayer();
      showPlayerEnvironmentAndGameBoard(currentPlayer, game.board());

      handleTokenChange(game);  /* if we need to update tokens */

      int choice = handleUserChoiceTileAndToken();
      var chosedTile = game.board().getTile(choice - 1);
      var chosedToken = game.board().getToken(choice - 1);

      handleTilePlacement(currentPlayer, chosedTile);
      handleTokenPlacement(currentPlayer, chosedToken);

      // IO.readln("STOP before the next turn");
      handleTurnChange(game);
    }

  }


  private void playSquareTerminal(){
    System.out.println("Welcome to the Cascadia game (terminal version)!");
    System.out.println("We have two players, please introduce yourselves.\n");
    String firstPlayerName = IO.readln("Player 1, what's your name? ");
    String secondPlayerName = IO.readln("Player 2, what's your name? ");
    int familyOrIntermediate = chooseVersion();
    System.out.println("You have chosen " + (familyOrIntermediate == 1 ? "Family" : "Intermediate") + " Scoring Card");

    System.out.println("The game is starting!");
    var player1 = new Player(firstPlayerName, this.version);
    var player2 = new Player(secondPlayerName, this.version);
    
    var listOfPlayers = List.of(player1, player2);
    var board = new GameBoard(Constants.NB_PLAYERS_SQUARE, this.version);
    var turnManager = new TurnManager(listOfPlayers, this.version);
    Game game = new Game(board, turnManager, listOfPlayers.size(), this.version);
    gameLoopVersionSquare(game);
    calculateAndShowScore(game, familyOrIntermediate);
  }

  
  
  
  /* Medhi part */


  public void displayVersion1(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Version 1", (int) (buttonX / 1.53), (int) (buttonY * 1.1));
    graphics.drawString("With square and terminal display", (int) (buttonX / 1.74), (int) (buttonY * 1.2));
  }

  public void displayVersion2(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("Version 2", (int) (buttonX * 1.15), (int) (buttonY * 1.1));
    graphics.drawString("With square and graphic display", (int) (buttonX * 1.075), (int) (buttonY * 1.2));
  }

  public void displayVersion3(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.BLUE);
    graphics.drawString("Version 3", (int) (buttonX * 1.65), (int) (buttonY * 1.1));
    graphics.drawString("With hexagon and graphic display", (int) (buttonX * 1.57), (int) (buttonY * 1.2));
  }

  public void displayNumberPlayer2(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("2 Players", (int) (buttonX / 1.53), (int) (buttonY * 1.1));
  }

  public void displayNumberPlayer3(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("3 Players", (int) (buttonX * 1.15), (int) (buttonY * 1.1));
  }

  public void displayNumberPlayer4(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.BLUE);
    graphics.drawString("4 Players", (int) (buttonX * 1.65), (int) (buttonY * 1.1));
  }

  public void displayVariantsChoice1(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.GRAY);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Family Variants", (int) (buttonX / 1.57), (int) (buttonY * 1.1));
  }

  public void displayVariantsChoice2(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.GRAY);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("Card Variants", (int) (buttonX * 1.14), (int) (buttonY * 1.1));
  }

  public void displayVariantsChoice3(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.GRAY);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.WHITE);
    graphics.drawString("Intermediate Variants", (int) (buttonX * 1.62), (int) (buttonY * 1.1));
  }

  public void displayCardA(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect(buttonX / 3, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Card A", (int) (buttonX * 0.48), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }

  
  
  public void displayCardB(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect((int) (buttonX * 0.8), buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("Card B", (int) (buttonX * 0.96), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }

  public void displayCardC(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect((int) (buttonX * 1.23), buttonY, 300, 300);
    graphics.setColor(Color.BLUE);
    graphics.drawString("Card C", (int) (buttonX * 1.38), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }

  public void displayCardD(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect((int) (buttonX * 1.7), buttonY, 300, 300);
    graphics.setColor(Color.WHITE);
    graphics.drawString("Card D", (int) (buttonX * 1.86), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }
  
  

  public void checkCardA(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 3 && location.x() <= buttonX / 3 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card A " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }
  
  

  public void checkCardB(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 0.8 && location.x() <= buttonX * 0.8 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card B " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }

  public void checkCardC(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.23 && location.x() <= buttonX * 1.23 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card C " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }

  public void checkCardD(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.7 && location.x() <= buttonX * 1.7 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card D " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }

  public void checkClickFamilyVariants(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, boolean player) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 2 && location.x() <= buttonX / 2 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Family Variants");
      if(player) PlayersChoiceMenu(action, location, buttonX, buttonY);
    }
  }

  public void checkClickCardVariants(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX && location.x() <= buttonX + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card Variants");
      CardsSalmonChoiceMenu(action, location, buttonX, buttonY);
      CardsBearChoiceMenu(action, location, buttonX, buttonY);
      CardsElkChoiceMenu(action, location, buttonX, buttonY);
      CardsHawkChoiceMenu(action, location, buttonX, buttonY);
      CardsFoxChoiceMenu(action, location, buttonX, buttonY);
    }
  }

  public void checkClickIntermediateVariants(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, boolean player) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.5 && location.x() <= buttonX * 1.5 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Intermediate Variants");
      if(player) PlayersChoiceMenu(action, location, buttonX, buttonY);
    }
  }

  public void checkClickVersion1(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 2 && location.x() <= buttonX / 2 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Version 1");
      VariantChoiceMenuVersion1And2(action, location,buttonX,buttonY );
    }
  }

  public void checkClickVersion2(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX && location.x() <= buttonX + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Version 2");
      VariantChoiceMenuVersion1And2(action, location,buttonX,buttonY );
    }
  }

  public void checkClickVersion3(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.5 && location.x() <= buttonX * 1.5 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Version 3");
      VariantChoiceMenuVersion3(action, location,buttonX,buttonY );
    }
  }

  public void checkClickPlayer2(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 2 && location.x() <= buttonX / 2 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("2 Players");
    }
  }

  public void checkClickPlayer3(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX && location.x() <= buttonX + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("3 Players");
    }
  }

=======
  /* Medhi part */


  public void displayVersion1(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Version 1", (int) (buttonX / 1.53), (int) (buttonY * 1.1));
    graphics.drawString("With square and terminal display", (int) (buttonX / 1.74), (int) (buttonY * 1.2));
  }

  public void displayVersion2(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("Version 2", (int) (buttonX * 1.15), (int) (buttonY * 1.1));
    graphics.drawString("With square and graphic display", (int) (buttonX * 1.075), (int) (buttonY * 1.2));
  }

  public void displayVersion3(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.BLUE);
    graphics.drawString("Version 3", (int) (buttonX * 1.65), (int) (buttonY * 1.1));
    graphics.drawString("With hexagon and graphic display", (int) (buttonX * 1.57), (int) (buttonY * 1.2));
  }

  public void displayNumberPlayer2(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("2 Players", (int) (buttonX / 1.53), (int) (buttonY * 1.1));
  }

  public void displayNumberPlayer3(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("3 Players", (int) (buttonX * 1.15), (int) (buttonY * 1.1));
  }

  public void displayNumberPlayer4(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.BLUE);
    graphics.drawString("4 Players", (int) (buttonX * 1.65), (int) (buttonY * 1.1));
  }

  public void displayVariantsChoice1(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.GRAY);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Family Variants", (int) (buttonX / 1.57), (int) (buttonY * 1.1));
  }

  public void displayVariantsChoice2(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.GRAY);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("Card Variants", (int) (buttonX * 1.14), (int) (buttonY * 1.1));
  }

  public void displayVariantsChoice3(Graphics2D graphics, int buttonX, int buttonY) {
    graphics.setColor(Color.GRAY);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.WHITE);
    graphics.drawString("Intermediate Variants", (int) (buttonX * 1.62), (int) (buttonY * 1.1));
  }

  public void displayCardA(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect(buttonX / 3, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Card A", (int) (buttonX * 0.48), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }

  public void displayCardB(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect((int) (buttonX * 0.8), buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("Card B", (int) (buttonX * 0.96), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }

  public void displayCardC(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect((int) (buttonX * 1.23), buttonY, 300, 300);
    graphics.setColor(Color.BLUE);
    graphics.drawString("Card C", (int) (buttonX * 1.38), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }

  public void displayCardD(Graphics2D graphics, int buttonX, int buttonY, WildlifeType animal) {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect((int) (buttonX * 1.7), buttonY, 300, 300);
    graphics.setColor(Color.WHITE);
    graphics.drawString("Card D", (int) (buttonX * 1.86), (int) (buttonY * 1.1));
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
  }

  public void checkCardA(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 3 && location.x() <= buttonX / 3 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card A " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }

  public void checkCardB(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 0.8 && location.x() <= buttonX * 0.8 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card B " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }

  public void checkCardC(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.23 && location.x() <= buttonX * 1.23 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card C " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }

  public void checkCardD(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.7 && location.x() <= buttonX * 1.7 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card D " + animal);
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(action, location, buttonX, buttonY);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(action, location, buttonX, buttonY);
        default -> PlayersChoiceMenu(action, location, buttonX, buttonY);
      }
    }
  }

  public void checkClickFamilyVariants(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, boolean player) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 2 && location.x() <= buttonX / 2 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Family Variants");
      if(player) PlayersChoiceMenu(action, location, buttonX, buttonY);
    }
  }

  public void checkClickCardVariants(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX && location.x() <= buttonX + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Card Variants");
      CardsSalmonChoiceMenu(action, location, buttonX, buttonY);
      CardsBearChoiceMenu(action, location, buttonX, buttonY);
      CardsElkChoiceMenu(action, location, buttonX, buttonY);
      CardsHawkChoiceMenu(action, location, buttonX, buttonY);
      CardsFoxChoiceMenu(action, location, buttonX, buttonY);
    }
  }

  public void checkClickIntermediateVariants(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, boolean player) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.5 && location.x() <= buttonX * 1.5 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Intermediate Variants");
      if(player) PlayersChoiceMenu(action, location, buttonX, buttonY);
    }
  }

  public void checkClickVersion1(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 2 && location.x() <= buttonX / 2 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Version 1");
      VariantChoiceMenuVersion1And2(action, location,buttonX,buttonY );
    }
  }

  public void checkClickVersion2(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX && location.x() <= buttonX + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Version 2");
      VariantChoiceMenuVersion1And2(action, location,buttonX,buttonY );
    }
  }

  public void checkClickVersion3(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.5 && location.x() <= buttonX * 1.5 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("Version 3");
      VariantChoiceMenuVersion3(action, location,buttonX,buttonY );
    }
  }

  public void checkClickPlayer2(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX / 2 && location.x() <= buttonX / 2 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("2 Players");
    }
  }

  public void checkClickPlayer3(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX && location.x() <= buttonX + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("3 Players");
    }
  }


  public void checkClickPlayer4(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * 1.5 && location.x() <= buttonX * 1.5 + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300) {
      System.out.println("4 Players");
    }
  }

  public void VariantChoiceMenuVersion1And2(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayVariantsChoice1(graphics, buttonX, buttonY);
        displayVariantsChoice3(graphics, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action2 = pointerEvent.action();
          PointerEvent.Location location2 = pointerEvent.location();
          checkClickFamilyVariants(action2, location2,buttonX,buttonY, false);
          checkClickIntermediateVariants(action2, location2,buttonX,buttonY, false);
        }}});}

  public void VariantChoiceMenuVersion3(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY){
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayVariantsChoice1(graphics, buttonX, buttonY);
        displayVariantsChoice2(graphics, buttonX, buttonY);
        displayVariantsChoice3(graphics, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action2 = pointerEvent.action();
          PointerEvent.Location location2 = pointerEvent.location();
          checkClickFamilyVariants(action2, location2,buttonX,buttonY, true);
          checkClickCardVariants(action2, location2,buttonX,buttonY);
          checkClickIntermediateVariants(action2, location2,buttonX,buttonY, true);
        }}});}

  public void PlayersChoiceMenu(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY){
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayNumberPlayer2(graphics, buttonX, buttonY);
        displayNumberPlayer3(graphics, buttonX, buttonY);
        displayNumberPlayer4(graphics, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action3 = pointerEvent.action();
          PointerEvent.Location location3 = pointerEvent.location();
          checkClickPlayer2(action3, location3,buttonX,buttonY);
          checkClickPlayer3(action3, location3,buttonX,buttonY);
          checkClickPlayer4(action3, location3,buttonX,buttonY);
        }}});}

  public void CardsSalmonChoiceMenu(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY){
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(graphics, buttonX, buttonY, WildlifeType.SALMON);
        displayCardB(graphics, buttonX, buttonY, WildlifeType.SALMON);
        displayCardC(graphics, buttonX, buttonY, WildlifeType.SALMON);
        displayCardD(graphics, buttonX, buttonY, WildlifeType.SALMON);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCardA(action4, location4,buttonX,buttonY, WildlifeType.SALMON);
          checkCardB(action4, location4,buttonX,buttonY, WildlifeType.SALMON);
          checkCardC(action4, location4,buttonX,buttonY, WildlifeType.SALMON);
          checkCardD(action4, location4,buttonX,buttonY, WildlifeType.SALMON);
        }}});}

  public void CardsBearChoiceMenu(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY){
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(graphics, buttonX, buttonY, WildlifeType.BEAR);
        displayCardB(graphics, buttonX, buttonY, WildlifeType.BEAR);
        displayCardC(graphics, buttonX, buttonY, WildlifeType.BEAR);
        displayCardD(graphics, buttonX, buttonY, WildlifeType.BEAR);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCardA(action4, location4,buttonX,buttonY, WildlifeType.BEAR);
          checkCardB(action4, location4,buttonX,buttonY, WildlifeType.BEAR);
          checkCardC(action4, location4,buttonX,buttonY, WildlifeType.BEAR);
          checkCardD(action4, location4,buttonX,buttonY, WildlifeType.BEAR);
        }}});}

  public void CardsElkChoiceMenu(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY){
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(graphics, buttonX, buttonY, WildlifeType.ELK);
        displayCardB(graphics, buttonX, buttonY, WildlifeType.ELK);
        displayCardC(graphics, buttonX, buttonY, WildlifeType.ELK);
        displayCardD(graphics, buttonX, buttonY, WildlifeType.ELK);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCardA(action4, location4,buttonX,buttonY, WildlifeType.ELK);
          checkCardB(action4, location4,buttonX,buttonY, WildlifeType.ELK);
          checkCardC(action4, location4,buttonX,buttonY, WildlifeType.ELK);
          checkCardD(action4, location4,buttonX,buttonY, WildlifeType.ELK);
        }}});}

  public void CardsHawkChoiceMenu(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY){
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(graphics, buttonX, buttonY, WildlifeType.HAWK);
        displayCardB(graphics, buttonX, buttonY, WildlifeType.HAWK);
        displayCardC(graphics, buttonX, buttonY, WildlifeType.HAWK);
        displayCardD(graphics, buttonX, buttonY, WildlifeType.HAWK);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCardA(action4, location4,buttonX,buttonY, WildlifeType.HAWK);
          checkCardB(action4, location4,buttonX,buttonY, WildlifeType.HAWK);
          checkCardC(action4, location4,buttonX,buttonY, WildlifeType.HAWK);
          checkCardD(action4, location4,buttonX,buttonY, WildlifeType.HAWK);
        }}});}

  public void CardsFoxChoiceMenu(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY){
    Application.run(Color.BLACK, context -> {
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(graphics, buttonX, buttonY, WildlifeType.FOX);
        displayCardB(graphics, buttonX, buttonY, WildlifeType.FOX);
        displayCardC(graphics, buttonX, buttonY, WildlifeType.FOX);
        displayCardD(graphics, buttonX, buttonY, WildlifeType.FOX);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCardA(action4, location4,buttonX,buttonY, WildlifeType.FOX);
          checkCardB(action4, location4,buttonX,buttonY, WildlifeType.FOX);
          checkCardC(action4, location4,buttonX,buttonY, WildlifeType.FOX);
          checkCardD(action4, location4,buttonX,buttonY, WildlifeType.FOX);
        }}});}

  private void gameLoopV2() {
    Application.run(Color.BLACK, context -> {
      int buttonWidth = 300;
      int buttonHeight = 300;
      int buttonX = context.getScreenInfo().width() / 2 - buttonWidth / 2;
      int buttonY = context.getScreenInfo().height() / 2 - buttonHeight / 2;
      context.renderFrame(graphics -> {
        displayVersion1(graphics, buttonX, buttonY);
        displayVersion2(graphics, buttonX, buttonY);
        displayVersion3(graphics, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action = pointerEvent.action();
          PointerEvent.Location location = pointerEvent.location();
          checkClickVersion1(action, location, buttonX, buttonY);
          checkClickVersion2(action, location, buttonX, buttonY);
          checkClickVersion3(action, location, buttonX, buttonY);
        }}});
    
  }
  

  public static void main(String[] args) {
    MainMenu test = new MainMenu(1);
  }




}
