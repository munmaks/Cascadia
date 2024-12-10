package fr.uge.ui;


import fr.uge.core.*;
import fr.uge.environment.Coordinates;
import fr.uge.environment.Tile;  // enable preview
import fr.uge.environment.WildlifeType;
import fr.uge.scoring.FamilyAndIntermediateScoringCards;
import fr.uge.scoring.WildlifeScoringCard;
import fr.uge.util.Constants;
import java.io.IO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
// import java.util.Arrays;

// import java.awt.*;
// import com.github.forax.zen.*;
// import com.github.forax.zen.Event;  // do we need it? because you already imported all classes from `com.github.forax.zen.*`



public final class MainMenu {
  /*
   * Main menu contains, bottoms: Play and Settings (to choose number of players and Wildlife Scoring Cards)
   * 
   * */
  private final int version;
  private final int nbPlayers;
  private final List<WildlifeScoringCard> scoringCards = new ArrayList<WildlifeScoringCard>();
  private final boolean isIntermediateScoringCard;



  // public MainMenu() {
  //   Application.run(Color.BLACK, context -> {
  //     int buttonWidth = 300;
  //     int buttonHeight = 300;
  //     int buttonX = context.getScreenInfo().width() / 2 - buttonWidth / 2;
  //     int buttonY = context.getScreenInfo().height() / 2 - buttonHeight / 2;
  //     context.renderFrame(graphics -> {
  //       displayVersion(context, buttonX, buttonY);
  //     });
  //     while (true) {
  //       Event event = context.pollEvent();
  //       if (event instanceof PointerEvent pointerEvent) {
  //         PointerEvent.Action action = pointerEvent.action();
  //         PointerEvent.Location location = pointerEvent.location();
  //         checkClickVersion(context, action, location, buttonX, buttonY);
  //       }}});}




  public MainMenu(int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    this.version = version;
    this.nbPlayers = 0;   // to do later
    this.isIntermediateScoringCard = false; // to do, later, 0 is Family Card, 1 is Intermediate Card

    if (this.version == Constants.VERSION_SQUARE){
      playSquareTerminal();
    } else if (this.version == Constants.VERSION_HEXAGONAL) {
      // playHexagonal();  // to do later
    }
    // gameLoopV2();
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
    return familyOrIntermediate;
  }


  // sort by x and y
  // listCells.sort((cell1, cell2) -> {
  //   if (cell1.getCoordinates().x() == cell2.getCoordinates().x()){
  //     return cell1.getCoordinates().y() - cell2.getCoordinates().y();
  //   }
  //   return cell1.getCoordinates().x() - cell2.getCoordinates().x();
  // });



  private void showEnvironment(Player player){
    Objects.requireNonNull(player);
    System.out.println("\n\nIt's " + player.name() + "'s turn!");
    System.out.println("\nHere is " + player.name() + "'s environment: ");
    var listCells = player.environment().getCells();

    // System.err.println("Size of list : " + listCells.size());  // for test, to delete later
    for (var cell : listCells){
      if (cell.isOccupiedByTile()){
        System.out.println(cell.toString());
      }
    }
  }


  private void showGameBoard(GameBoard board){
    Objects.requireNonNull(board);
    System.out.println("\nHere is the game board: ");
    var tiles = board.getCopyOfTiles();
    var tokens = board.getCopyOfTokens();
    for (var i = 0; i < tiles.size(); ++i){
      var builder = new StringBuilder();
      builder.append(i+1).append(") ").append(tiles.get(i).toString()).append(" and ").append(tokens.get(i).toString());
      System.out.println(builder.toString());
    }
    System.out.println("");
  }


  private void showPossibleCoordinates(Player player){
    Objects.requireNonNull(player);
    System.out.println("\nHere are the possible coordinates:\n`empty (x, y)` - neighbor tile");
    var setOfCells = player.environment().getPossibleCells();
    for (var cell : setOfCells){
      player.environment().printAllNeighbors(cell);
    }
  }



  private void showPossibleTokenPlacement(Player player, WildlifeType token){
    Objects.requireNonNull(player, "player is null in showPossibleTokenPlacement()");
    Objects.requireNonNull(token, "token is null in showPossibleTokenPlacement()");

    System.out.println("Here are the possible coordinates to place the token: ");
    var listOfCells = player.environment().getCells();

    for (var cell : listOfCells){
      if (cell == null){
        throw new IllegalArgumentException("cell is null in showPossibleTokenPlacement()");
      }
      if (cell.canBePlaced(token)){
        System.out.println(cell.toString());
      }
    }
  }


  /**
   * to do later
   */
  private void showScore(Game game){
    System.out.println("Game is over!\nThank you for playing!");

    //
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
   * */
  private void handleTokenChange(Game game){
    Objects.requireNonNull(game);
    if (game.board().tokensNeedUpdate()){
      game.board().updateTokens();
      System.out.println("Tokens were updated, because one token had 4 occurences");
      showGameBoard(game.board());
    }
    else if (game.board().tokensCanBeUpdated()){
      var stringTokensToChange = IO.readln("Enter `yes` if you want to change the tokens, otherwise press enter ");
      try (Scanner s = new Scanner(stringTokensToChange)) {
        if (s.hasNext() && "yes".equals(s.next())){
          System.out.println("tokens are now updated");
          game.board().updateTokens();
        }
      }
      showGameBoard(game.board());
    }
  }



  private int handleUserChoiceTileAndToken(){
    int choice;
    do {
      choice = Integer.parseInt(IO.readln("Please choose ONLY from 1 to 4 to take a couple: (Tile, Token)\n"));
    } while(!Constants.isValidChoice(choice));
    return choice;
  }


  private void handleTurnChange(Game game){
    game.board().setDefaultTokensAreUpdated();  // that means, next person can change tokens (if needed)
    game.turnManager().changePlayer();
    game.turnManager().nextTurn();
    // System.out.println("Turns left: " + (Constants.MAX_GAME_TURNS - game.turnManager().getTotalTurns()));
  }


  private void handleTokenPlacement(Player player, WildlifeType chosedToken){
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedToken);

    /* chosed token from `choice` */
    System.out.println("Now you need to place the wildlife token: " + chosedToken.toString());
    showPossibleTokenPlacement(player, chosedToken);

    var userCoordinatesString = IO.readln("Give me coordinates of tile, that you want to place the token on (format: \"x, y\"): ");

    var userCoordinates = getCoordinatesFromUser(userCoordinatesString);
    var currCell = player.environment().getCell(userCoordinates);
    var tokenWasPlaced = player.environment().placeAnimal(currCell, chosedToken);

    if (!tokenWasPlaced){
      System.err.println("Token wasn't placed");  /* for tests, to delete later */
    }
  }


  private void handleTilePlacement(Player player, Tile chosedTile){
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedTile);
    var possibleCoordinates = player.environment().getPossibleCells();

    /* player has to place tile correctly */
    do {
      var userCoordinatesString = IO.readln("Give me coordinates of cell, that you want to place the tile on (format: \"x, y\"): ");
      var userCoordinates = getCoordinatesFromUser(userCoordinatesString);

      if (possibleCoordinates.stream()
                             .anyMatch(coordinates -> coordinates.equals(userCoordinates))) {
        var currCell = player.environment().getCell(userCoordinates);
        if (player.environment().placeTile(currCell, chosedTile)){
          System.out.println("Tile was placed successfully (for test Main Menu)");  // for test, to delete later
          break;
        }
      }
    } while (true);
  }


  private void showPlayerEnvironmentAndGameBoard(Player player, GameBoard board){
    showEnvironment(player);
    showPossibleCoordinates(player);
    showGameBoard(board);
  }



//  private List<Integer> calculateListOfScores(Game game){
//    Objects.requireNonNull(game);
//    var listOfPlayerScores = new ArrayList<Integer>();
//    for (var i = 0; i < game.getPlayerCount(); ++i){
//      var player = game.turnManager().getPlayerByIndex(i);
//      var score = player.calculateScore();
//      listOfPlayerScores.add(score);
//    }
//    return listOfPlayerScores;
//  }


  private void showPlayerScore(Player player, FamilyAndIntermediateScoringCards card) {
    System.out.println(player.name() + " your final score: " + card.getScore(player) + "\n");
    System.out.println(player.name() + " your tiles score: " + player.environment().calculateTileScore() + "\n");
  }


  private void calculateAndShowScore(Game game, int familyOrIntermediate){
    // var listOfPlayerScores = calculateListOfScores(game);
    var scoringCard = new FamilyAndIntermediateScoringCards(familyOrIntermediate);

    for (var i = 0; i < game.getPlayerCount(); ++i) {
      showPlayerScore(game.turnManager().getPlayerByIndex(i), scoringCard);
    }
    // showPlayersScores(listOfPlayerScores);
    // showScore(game);
  }

  
  
  private void resetForNextTurn(Game game) {
    Objects.requireNonNull(game);
    game.board().setDefaultTokensAreUpdated();  // that means, next person can change
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
      resetForNextTurn(game);
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

  
  
  /**********************************************************
   **********************************************************
   ************************ GRAPHICS ************************
   **********************************************************
   **********************************************************/
/*
  public void displayVersion(ApplicationContext context, int buttonX, int buttonY) {
    context.renderFrame(graphics -> {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Version 1", (int) (buttonX / 1.53), (int) (buttonY * 1.1));
    graphics.drawString("With square and terminal display", (int) (buttonX / 1.74), (int) (buttonY * 1.2));
    graphics.setColor(Color.RED);
    graphics.drawString("Version 2", (int) (buttonX * 1.15), (int) (buttonY * 1.1));
    graphics.drawString("With square and graphic display", (int) (buttonX * 1.075), (int) (buttonY * 1.2));
    graphics.setColor(Color.BLUE);
    graphics.drawString("Version 3", (int) (buttonX * 1.65), (int) (buttonY * 1.1));
    graphics.drawString("With hexagon and graphic display", (int) (buttonX * 1.57), (int) (buttonY * 1.2));
    });
  }

  public void displayNumberPlayer(ApplicationContext context, int buttonX, int buttonY){
    context.renderFrame(graphics -> {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.fillRect(buttonX, buttonY, 300, 300);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("2 Players", (int) (buttonX / 1.53), (int) (buttonY * 1.1));
    graphics.setColor(Color.RED);
    graphics.drawString("3 Players", (int) (buttonX * 1.15), (int) (buttonY * 1.1));
    graphics.setColor(Color.BLUE);
    graphics.drawString("4 Players", (int) (buttonX * 1.65), (int) (buttonY * 1.1));
    });
  }

  public void displayVariantsChoice1(ApplicationContext context, int buttonX, int buttonY) {
    context.renderFrame(graphics -> {
    graphics.setColor(Color.GRAY);
    graphics.fillRect(buttonX / 2, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Family Variants", (int) (buttonX / 1.57), (int) (buttonY * 1.1));
    });
  }

  public void displayVariantsChoice2(ApplicationContext context, int buttonX, int buttonY) {
    context.renderFrame(graphics -> {
      graphics.setColor(Color.GRAY);
      graphics.fillRect(buttonX, buttonY, 300, 300);
      graphics.setColor(Color.RED);
      graphics.drawString("Card Variants", (int) (buttonX * 1.14), (int) (buttonY * 1.1));
    });

  }

  public void displayVariantsChoice3(ApplicationContext context, int buttonX, int buttonY) {
    context.renderFrame(graphics -> {
    graphics.setColor(Color.GRAY);
    graphics.fillRect((int) (buttonX * 1.5), buttonY, 300, 300);
    graphics.setColor(Color.WHITE);
    graphics.drawString("Intermediate Variants", (int) (buttonX * 1.62), (int) (buttonY * 1.1));
    });
  }

  public void displayCardA(ApplicationContext context, int buttonX, int buttonY, WildlifeType animal) {
    context.renderFrame(graphics -> {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect(buttonX / 3, buttonY, 300, 300);
    graphics.setColor(Color.BLACK);
    graphics.drawString("Card A", (int) (buttonX * 0.48), (int) (buttonY * 1.1));
    makeCardMorePretty(graphics, animal);
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
    });
  }

  
  
  public void displayCardB(ApplicationContext context, int buttonX, int buttonY, WildlifeType animal) {
    context.renderFrame(graphics -> {
    graphics.setColor(Color.DARK_GRAY);
    graphics.fillRect((int) (buttonX * 0.8), buttonY, 300, 300);
    graphics.setColor(Color.RED);
    graphics.drawString("Card B", (int) (buttonX * 0.96), (int) (buttonY * 1.1));
    makeCardMorePretty(graphics, animal);
    graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
    graphics.setColor(Color.BLACK);
    graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
    });
  }

  public void displayCardC(ApplicationContext context, int buttonX, int buttonY, WildlifeType animal) {
    context.renderFrame(graphics -> {
      graphics.setColor(Color.DARK_GRAY);
      graphics.fillRect((int) (buttonX * 1.23), buttonY, 300, 300);
      graphics.setColor(Color.BLUE);
      graphics.drawString("Card C", (int) (buttonX * 1.38), (int) (buttonY * 1.1));
      makeCardMorePretty(graphics, animal);
      graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
      graphics.setColor(Color.BLACK);
      graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
    });
  }

  public void displayCardD(ApplicationContext context, int buttonX, int buttonY, WildlifeType animal) {
    context.renderFrame(graphics -> {
      graphics.setColor(Color.DARK_GRAY);
      graphics.fillRect((int) (buttonX * 1.7), buttonY, 300, 300);
      graphics.setColor(Color.WHITE);
      graphics.drawString("Card D", (int) (buttonX * 1.86), (int) (buttonY * 1.1));
      makeCardMorePretty(graphics, animal);
      graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
      graphics.setColor(Color.BLACK);
      graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY /1.9));
    });
  }
  


  public void displayPreviousChoice(ApplicationContext context, int bottomY, int bottomButtonHeight, int bottomButtonWidth, final int version, ArrayList<String> cardAnimalChoice, final int playerNumber){
    context.renderFrame(graphics -> {
      ArrayList<String> buttons = new ArrayList<>(Arrays.asList("Version", "Salmon", "Bear", "Elk", "Hawk", "Fox", "Player"));
      ArrayList<String> texts = new ArrayList<>(Arrays.asList(String.valueOf(version), cardAnimalChoice.getFirst(), cardAnimalChoice.get(1), cardAnimalChoice.get(2), cardAnimalChoice.get(3), cardAnimalChoice.get(4),String.valueOf(playerNumber)));
    for (int i = 0; i < 7; i++) {
      int x = (int) (context.getScreenInfo().width() * (0.07 + i * 0.14));
      graphics.setColor(Color.DARK_GRAY);
      graphics.fillRect(x, bottomY, bottomButtonWidth, bottomButtonHeight);
      graphics.setColor(Color.WHITE);
      graphics.drawString(buttons.get(i), (int) (x + bottomButtonWidth * 0.25), (int) (bottomY + bottomButtonHeight * 0.4));
      graphics.drawString(texts.get(i), (int) (x + bottomButtonWidth * 0.25), (int) (bottomY + bottomButtonHeight * 0.75));
    }
    });
  }

  public void displayEndMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice, final int playerNumber) {
    context.renderFrame(graphics -> {
      graphics.setColor(Color.BLACK);
      graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
      graphics.setColor(Color.GRAY);
      graphics.fillRect(buttonX, buttonY, 300, 300);
      graphics.setColor(Color.WHITE);
      graphics.drawString("Start", (int) (buttonX * 1.14), (int) (buttonY * 1.1));
      graphics.fillRect((int) (buttonX*1.01), (int) (buttonY /2.5), 300, 100);
      graphics.setColor(Color.BLACK);
      graphics.drawString("Reset", (int) (buttonX * 1.17), (int) (buttonY /1.9));
      int bottomY = (int) (context.getScreenInfo().height() * 0.8);
      displayPreviousChoice(context, bottomY, 120, 120, version, cardAnimalChoice, playerNumber);
    });
  }



  public void checkStartOrReset(ApplicationContext context, PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice, final int playerNumber) {
    if (clickInRange(action, location, buttonX, buttonY, 1)) {
      // besoin de 7 choses, type de version (square ou hexagonal) / 5 card (Wildlife type ou famille ou intermediaire) / int nombre de joueurs
      System.out.println(version);
      System.out.println(cardAnimalChoice.get(0));
      System.out.println(cardAnimalChoice.get(1));
      System.out.println(cardAnimalChoice.get(2));
      System.out.println(cardAnimalChoice.get(3));
      System.out.println(cardAnimalChoice.get(4));
      System.out.println(playerNumber);
    }
    if (action == PointerEvent.Action.POINTER_UP && location.x() >= (int) (buttonX * 1.01) && location.x() <= (int) (buttonX * 1.01) + 300 && location.y() >= (int) (buttonY / 2.5) && location.y() <= (int) (buttonY / 2.5) + 100) {
      new MainMenu();
    }
  }


  public boolean clickInRange(PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, double position){
    return action == PointerEvent.Action.POINTER_UP &&
            location.x() >= buttonX * position && location.x() <= buttonX * position + 300 &&
            location.y() >= buttonY && location.y() <= buttonY + 300;
  }



  public void checkCard(ApplicationContext context, PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal, final int version, ArrayList<String> cardAnimalChoice) {
      if (clickInRange(action, location, buttonX, buttonY, 0.33)) {
        cardAnimalChoice.add("Card A");
      }
      if (clickInRange(action, location, buttonX, buttonY, 0.8)) {
        cardAnimalChoice.add("Card B");
      }
      if (clickInRange(action, location, buttonX, buttonY, 1.23)) {
        cardAnimalChoice.add("Card C");
      }
      if (clickInRange(action, location, buttonX, buttonY, 1.7)) {
        cardAnimalChoice.add("Card D");
      }
    switchAnimalDisplay(context, action, location, buttonX, buttonY, animal, version, cardAnimalChoice);
  }



  public void switchAnimalDisplay(ApplicationContext context, PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, WildlifeType animal, final int version, ArrayList<String> cardAnimalChoice) {
    if (action == PointerEvent.Action.POINTER_UP) {
      switch (animal) {
        case WildlifeType.SALMON -> CardsBearChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
        case WildlifeType.BEAR -> CardsElkChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
        case WildlifeType.ELK -> CardsHawkChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
        case WildlifeType.HAWK -> CardsFoxChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
        default -> PlayersChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
      }
    }
  }



  public void makeCardMorePretty( Graphics2D graphics, WildlifeType animal) {
    switch (animal) {
      case WildlifeType.SALMON -> graphics.setColor(Color.PINK);
      case WildlifeType.BEAR -> graphics.setColor(Color.CYAN);
      case WildlifeType.ELK -> graphics.setColor(Color.RED);
      case WildlifeType.HAWK -> graphics.setColor(Color.BLUE);
      default -> graphics.setColor(Color.ORANGE);
    }
  }



  public void checkVariants(ApplicationContext context, PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, boolean player, final int version){
    ArrayList<String> cardAnimalChoice = new ArrayList<>();
    if (clickInRange(action, location, buttonX, buttonY, 0.5)) {
      for(int i=0; i<5; i++) {
        cardAnimalChoice.add("Families");
      }
      if(player) PlayersChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
    }
    if (clickInRange(action, location, buttonX, buttonY, 1)) {
      CardsSalmonChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
    }
    if (clickInRange(action, location, buttonX, buttonY, 1.5)) {
      for(int i=0; i<5; i++) {
        cardAnimalChoice.add("Intermediate");
      }
      if(player) PlayersChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
    }
  }



  public void checkClickVersion(ApplicationContext context, PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY) {
    if (clickInRange(action, location, buttonX, buttonY, 0.5)) {
      VariantChoiceMenuVersion1And2(context, action, location,buttonX,buttonY, 1);
    }
    if (clickInRange(action, location, buttonX, buttonY, 1)) {
      VariantChoiceMenuVersion1And2(context, action, location,buttonX,buttonY, 2);
    }
    if (clickInRange(action, location, buttonX, buttonY, 1.5)) {
      VariantChoiceMenuVersion3(context, buttonX,buttonY, 3);
    }
  }



  public void checkClickPlayer(ApplicationContext context, PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice) {
    if (clickInRange(action, location, buttonX, buttonY, 0.5)) {
      StartOrResetMenu(context, buttonX, buttonY, version, cardAnimalChoice, NB_PLAYERS_SQUARE); // must do this call in a if, otherwise, it makes problem
    }
    if (clickInRange(action, location, buttonX, buttonY, 1)) {
      StartOrResetMenu(context, buttonX, buttonY, version, cardAnimalChoice, 3);
    }
    if (clickInRange(action, location, buttonX, buttonY, 1.5)) {
      StartOrResetMenu(context, buttonX, buttonY, version, cardAnimalChoice, 4);
    }
  }


  public void VariantChoiceMenuVersion1And2(ApplicationContext context, PointerEvent.Action action, PointerEvent.Location location, int buttonX, int buttonY, final int version) {
        context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayVariantsChoice1(context, buttonX, buttonY);
        displayVariantsChoice3(context, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action2 = pointerEvent.action();
          PointerEvent.Location location2 = pointerEvent.location();
          checkVariants(context, action2, location2,buttonX,buttonY, false, version);
        }}}




  public void VariantChoiceMenuVersion3(ApplicationContext context, int buttonX, int buttonY, final int version){
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayVariantsChoice1(context, buttonX, buttonY);
        displayVariantsChoice2(context, buttonX, buttonY);
        displayVariantsChoice3(context, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action2 = pointerEvent.action();
          PointerEvent.Location location2 = pointerEvent.location();
          checkVariants(context, action2, location2,buttonX,buttonY, true, version);
        }}}




  public void StartOrResetMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice, final int playerNumber){
    context.renderFrame(graphics -> {
      graphics.setColor(Color.BLACK);
      graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
      displayEndMenu(context, buttonX, buttonY, version, cardAnimalChoice, playerNumber);
    });
    while (true) {
      Event event = context.pollEvent();
      if (event instanceof PointerEvent pointerEvent) {
        PointerEvent.Action action5 = pointerEvent.action();
        PointerEvent.Location location5 = pointerEvent.location();
        checkStartOrReset(context, action5, location5, buttonX, buttonY, version, cardAnimalChoice, playerNumber);
      }}}




  public void PlayersChoiceMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice){
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayNumberPlayer(context, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action3 = pointerEvent.action();
          PointerEvent.Location location3 = pointerEvent.location();
          checkClickPlayer(context, action3, location3,buttonX,buttonY, version, cardAnimalChoice);
        }}}




  public void CardsSalmonChoiceMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice){
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(context, buttonX, buttonY, WildlifeType.SALMON);
        displayCardB(context, buttonX, buttonY, WildlifeType.SALMON);
        displayCardC(context, buttonX, buttonY, WildlifeType.SALMON);
        displayCardD(context, buttonX, buttonY, WildlifeType.SALMON);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCard(context, action4, location4,buttonX,buttonY, WildlifeType.SALMON, version, cardAnimalChoice);
        }}}

  public void CardsBearChoiceMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice){
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(context, buttonX, buttonY, WildlifeType.BEAR);
        displayCardB(context, buttonX, buttonY, WildlifeType.BEAR);
        displayCardC(context, buttonX, buttonY, WildlifeType.BEAR);
        displayCardD(context, buttonX, buttonY, WildlifeType.BEAR);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCard(context, action4, location4,buttonX,buttonY, WildlifeType.BEAR, version, cardAnimalChoice);
        }}}

  public void CardsElkChoiceMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice){
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(context, buttonX, buttonY, WildlifeType.ELK);
        displayCardB(context, buttonX, buttonY, WildlifeType.ELK);
        displayCardC(context, buttonX, buttonY, WildlifeType.ELK);
        displayCardD(context, buttonX, buttonY, WildlifeType.ELK);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCard(context, action4, location4,buttonX,buttonY, WildlifeType.ELK, version, cardAnimalChoice);
        }}}

  public void CardsHawkChoiceMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice){
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(context, buttonX, buttonY, WildlifeType.HAWK);
        displayCardB(context, buttonX, buttonY, WildlifeType.HAWK);
        displayCardC(context, buttonX, buttonY, WildlifeType.HAWK);
        displayCardD(context, buttonX, buttonY, WildlifeType.HAWK);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCard(context, action4, location4,buttonX,buttonY, WildlifeType.HAWK, version, cardAnimalChoice);
        }}}

  public void CardsFoxChoiceMenu(ApplicationContext context, int buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice){
      context.renderFrame(graphics -> {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
        displayCardA(context, buttonX, buttonY, WildlifeType.FOX);
        displayCardB(context, buttonX, buttonY, WildlifeType.FOX);
        displayCardC(context, buttonX, buttonY, WildlifeType.FOX);
        displayCardD(context, buttonX, buttonY, WildlifeType.FOX);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action4 = pointerEvent.action();
          PointerEvent.Location location4 = pointerEvent.location();
          checkCard(context, action4, location4,buttonX,buttonY, WildlifeType.FOX, version, cardAnimalChoice);
        }}}



  private void gameLoopV2() {
    Application.run(Color.BLACK, context -> {
      int buttonX = context.getScreenInfo().width() / 2 - buttonWidth / 2;
      int buttonY = context.getScreenInfo().height() / 2 - buttonHeight / 2;
      context.renderFrame(graphics -> {
        displayVersion(context, buttonX, buttonY);
      });
      while (true) {
        Event event = context.pollEvent();
        if (event instanceof PointerEvent pointerEvent) {
          PointerEvent.Action action = pointerEvent.action();
          PointerEvent.Location location = pointerEvent.location();
          checkClickVersion(context, action, location, buttonX, buttonY);
        }}});

  }


  public static void main(String[] args) {
    new MainMenu().gameLoopV2();
  }
 */

}
